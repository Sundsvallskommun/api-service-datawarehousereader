package se.sundsvall.datawarehousereader.service;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerMapper.toCustomerEngagements;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerMapper.toPartyType;
import static se.sundsvall.datawarehousereader.service.util.ServiceUtil.removeHyphen;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetails;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsParameters;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsResponse;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagement;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagementParameters;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagementResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.CustomerDetailsRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.CustomerRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailsEntity;
import se.sundsvall.datawarehousereader.service.logic.PartyProvider;
import se.sundsvall.datawarehousereader.service.mapper.CustomerDetailsMapper;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;


@Service
public class CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	private final CustomerRepository repository;

	private final CustomerDetailsRepository detailsRepository;

	private final PartyProvider partyProvider;

	public CustomerService(final CustomerDetailsRepository detailsRepository,
			final CustomerRepository repository, final PartyProvider partyProvider) {
		this.detailsRepository = detailsRepository;
		this.repository = repository;
		this.partyProvider = partyProvider;
	}

	public CustomerEngagementResponse getCustomerEngagements(final CustomerEngagementParameters parameters) {
		final var matches = repository.findAllByParameters(parameters, getCustomerOrgIdList(parameters.getPartyId()), PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));

		LOGGER.debug("Database query results: {} with content: {}", matches, matches.getContent());

		// If page larger than last page is requested, an empty list is returned otherwise the current page
		final List<CustomerEngagement> customerEngagements = matches.getTotalPages() < parameters.getPage() ? emptyList() : switchToPartyId(toCustomerEngagements(matches.getContent()));

		return CustomerEngagementResponse.create()
			.withMetaData(PagingAndSortingMetaData.create().withPageData(matches))
			.withCustomerEngagements(customerEngagements);
	}

	/**
	 * Fetch customer details
	 * @param parameters request parameters
	 * @return {@link CustomerDetailsResponse}
	 */
	public CustomerDetailsResponse getCustomerDetails(final CustomerDetailsParameters parameters) {
		final var fromDateTime = Optional.ofNullable(parameters.getFromDateTime())
			.map(OffsetDateTime::toLocalDateTime)
			.orElse(null);

		var pageable = toPageRequest(parameters);

		var pagedResult = getCustomerDetailsWithParameters(fromDateTime, parameters, pageable);

		return CustomerDetailsResponse.create()
			.withMetadata(PagingAndSortingMetaData.create().withPageData(pagedResult))
			.withCustomerDetails(mapCustomerDetailsEntities(pagedResult));
	}

	//Since we can't use specifications, make sure we only query data that we have.
	private Page<CustomerDetailsEntity> getCustomerDetailsWithParameters(final LocalDateTime fromDateTime, final CustomerDetailsParameters parameters, Pageable pageable) {
		//We have both orgId and partyIds
		if (isNotBlank(parameters.getCustomerEngagementOrgId()) && !isEmpty(parameters.getPartyId())) {
			return detailsRepository.findWithCustomerEngagementOrgIdAndPartyIds(fromDateTime, parameters.getCustomerEngagementOrgId(), parameters.getPartyId(), pageable);
		}
		//We have only orgId
		else {
			return detailsRepository.findWithCustomerEngagementOrgId(fromDateTime, parameters.getCustomerEngagementOrgId(), pageable);
		}
	}

	private List<CustomerDetails> mapCustomerDetailsEntities(Page<CustomerDetailsEntity> matches) {
		var details = matches.getContent();
		return details.stream()
				.map(CustomerDetailsMapper::toCustomerDetails)
				.toList();
	}

	private PageRequest toPageRequest(final CustomerDetailsParameters parameters) {
		return PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort());
	}

	private List<String> getCustomerOrgIdList(final List<String> partyIds) {
		return ofNullable(partyIds).orElse(emptyList()).stream()
			.map(partyProvider::translateToLegalId)
			.toList();
	}

	private List<CustomerEngagement> switchToPartyId(final List<CustomerEngagement> customerEngagements) {
		customerEngagements.forEach(engagement -> engagement
			.withPartyId(fetchPartyId(engagement.getCustomerType(), engagement.getCustomerOrgNumber()))
			.withCustomerOrgNumber(null)); // Needs to be reset to not expose person/organization number in response

		return customerEngagements;
	}

	private String fetchPartyId(final CustomerType type, final String orgNumber) {
		if (!hasText(orgNumber)) {
			LOGGER.info("CustomerEngagement did not contain a 'customerOrgNumber'. Skipping call to Party-service. {}", orgNumber);
			return null;
		}
		return partyProvider.translateToPartyId(toPartyType(type), removeHyphen(orgNumber));
	}
}
