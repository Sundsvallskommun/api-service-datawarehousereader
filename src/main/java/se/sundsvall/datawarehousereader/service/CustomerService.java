package se.sundsvall.datawarehousereader.service;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerDetailMapper.toPagingAndSortingMetaData;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerDetailMapper.toSortString;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerMapper.toCustomerEngagements;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerMapper.toPartyType;
import static se.sundsvall.datawarehousereader.service.util.ServiceUtil.removeHyphen;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetails;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsParameters;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsResponse;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagement;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagementParameters;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagementResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.CustomerDetailRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.CustomerRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.MetadataEmbeddable;
import se.sundsvall.datawarehousereader.service.logic.PartyProvider;
import se.sundsvall.datawarehousereader.service.mapper.CustomerDetailMapper;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

@Service
public class CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	private final CustomerRepository customerRepository;
	private final CustomerDetailRepository customerDetailRepository;
	private final PartyProvider partyProvider;

	CustomerService(
		final CustomerDetailRepository customerDetailRepository,
		final CustomerRepository customerRepository,
		final PartyProvider partyProvider) {

		this.customerDetailRepository = customerDetailRepository;
		this.customerRepository = customerRepository;
		this.partyProvider = partyProvider;
	}

	public CustomerEngagementResponse getCustomerEngagements(final String municipalityId, final CustomerEngagementParameters parameters) {
		final var matches = customerRepository.findAllByParameters(parameters, getCustomerOrgIdList(municipalityId, parameters.getPartyId()), PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));

		LOGGER.debug("Database query results: {} with content: {}", matches, matches.getContent());

		// If page larger than last page is requested, an empty list is returned otherwise the current page
		final List<CustomerEngagement> customerEngagements = matches.getTotalPages() < parameters.getPage() ? emptyList() : switchToPartyId(municipalityId, toCustomerEngagements(matches.getContent()));

		return CustomerEngagementResponse.create()
			.withMetaData(PagingAndSortingMetaData.create().withPageData(matches))
			.withCustomerEngagements(customerEngagements);
	}

	/**
	 * Fetch customer details
	 * If fromDate in {@link CustomerDetailsParameters} is empty, we fetch last years changes.
	 *
	 * @param  parameters request parameters
	 * @return            {@link CustomerDetailsResponse}
	 */
	public CustomerDetailsResponse getCustomerDetails(final CustomerDetailsParameters parameters) {
		final var fromDateTime = Optional.ofNullable(parameters.getFromDateTime())
			.map(OffsetDateTime::toLocalDateTime)
			.orElse(LocalDateTime.now().minusYears(1L));

		final var result = getCustomerDetailsWithParameters(fromDateTime, parameters);
		final var metadata = result.isEmpty() ? MetadataEmbeddable.create() : result.getFirst().getMetadata();

		return CustomerDetailsResponse.create()
			.withMetadata(toPagingAndSortingMetaData(parameters, metadata))
			.withCustomerDetails(mapCustomerDetailsEntities(result));
	}

	// Since we can't use specifications, make sure we only query data that we have.
	private List<CustomerDetailEntity> getCustomerDetailsWithParameters(final LocalDateTime fromDateTime, final CustomerDetailsParameters parameters) {
		// We have both orgId and partyIds
		if (!isEmpty(parameters.getPartyId())) {
			return customerDetailRepository.findWithCustomerEngagementOrgIdAndPartyIds(fromDateTime, parameters.getCustomerEngagementOrgId(), String.join(",", parameters.getPartyId()),
				parameters.getPage(),
				parameters.getLimit(),
				toSortString(parameters.getSortBy(), parameters.getSortDirection()));

		}
		return customerDetailRepository.findWithCustomerEngagementOrgId(fromDateTime, parameters.getCustomerEngagementOrgId(),
			parameters.getPage(),
			parameters.getLimit(),
			toSortString(parameters.getSortBy(), parameters.getSortDirection()));
	}

	private List<CustomerDetails> mapCustomerDetailsEntities(List<CustomerDetailEntity> matches) {
		return matches.stream()
			.map(CustomerDetailMapper::toCustomerDetails)
			.toList();
	}

	private List<String> getCustomerOrgIdList(final String municipalityId, final List<String> partyIds) {
		return ofNullable(partyIds).orElse(emptyList()).stream()
			.map(partyId -> partyProvider.translateToLegalId(municipalityId, partyId))
			.toList();
	}

	private List<CustomerEngagement> switchToPartyId(final String municipalityId, final List<CustomerEngagement> customerEngagements) {
		customerEngagements.forEach(engagement -> engagement
			.withPartyId(fetchPartyId(engagement.getCustomerType(), municipalityId, engagement.getCustomerOrgNumber()))
			.withCustomerOrgNumber(null)); // Needs to be reset to not expose person/organization number in response

		return customerEngagements;
	}

	private String fetchPartyId(final CustomerType type, final String municipalityId, final String orgNumber) {
		if (!hasText(orgNumber)) {
			LOGGER.info("CustomerEngagement did not contain a 'customerOrgNumber'. Skipping call to Party-service. {}", orgNumber);
			return null;
		}
		return partyProvider.translateToPartyId(toPartyType(type), municipalityId, removeHyphen(orgNumber));
	}
}
