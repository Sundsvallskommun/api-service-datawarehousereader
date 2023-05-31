package se.sundsvall.datawarehousereader.service;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.springframework.util.StringUtils.hasText;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerDetailsMapper.toCustomerDetails;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerMapper.toCustomerEngagements;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerMapper.toPartyType;
import static se.sundsvall.datawarehousereader.service.util.ServiceUtil.removeHyphen;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.api.model.MetaData;
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

@Service
public class CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	private final CustomerRepository repository;

	private final CustomerDetailsRepository detailsRepository;

	private final PartyProvider partyProvider;

	public CustomerService(final CustomerDetailsRepository detailsRepository, final CustomerRepository repository, final PartyProvider partyProvider) {
		this.detailsRepository = detailsRepository;
		this.repository = repository;
		this.partyProvider = partyProvider;
	}

	public CustomerEngagementResponse getCustomerEngagements(CustomerEngagementParameters parameters) {
		final var matches = repository.findAllByParameters(parameters, getCustomerOrgIdList(parameters.getPartyId()), PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));

		LOGGER.debug("Database query results: {} with content: {}", matches, matches.getContent());

		// If page larger than last page is requested, an empty list is returned otherwise the current page
		List<CustomerEngagement> customerEngagements = matches.getTotalPages() < parameters.getPage() ? emptyList() : switchToPartyId(toCustomerEngagements(matches.getContent()));

		return CustomerEngagementResponse.create()
			.withMetaData(MetaData.create()
				.withPage(parameters.getPage())
				.withSortBy(parameters.getSortBy())
				.withSortDirection(parameters.getSortDirection())
				.withTotalPages(matches.getTotalPages())
				.withTotalRecords(matches.getTotalElements())
				.withCount(customerEngagements.size())
				.withLimit(parameters.getLimit()))
			.withCustomerEngagements(customerEngagements);
	}


	public CustomerDetailsResponse getCustomerDetails(CustomerDetailsParameters parameters) {

		final var fromDateTime = Optional.ofNullable(parameters.getFromDateTime()).map(OffsetDateTime::toLocalDateTime).orElse(null);

		final var matches = toPage(parameters, detailsRepository.findAllMatching(fromDateTime));

		List<CustomerDetails> customerDetails = matches
			.getTotalPages() < parameters.getPage() ? emptyList() : toCustomerDetails(matches.getContent());

		customerDetails.forEach(details -> {
			details.setPartyId(fetchPartyId(details.getCustomerType(), details.getCustomerOrgNumber()));
			details.setCustomerOrgNumber(null);

		});

		customerDetails = customerDetails.stream()
			.filter(details -> parameters.getPartyId().contains(details.getPartyId()))
			.toList();

		return CustomerDetailsResponse.create()
			.withMetadata(MetaData.create()
				.withPage(parameters.getPage())
				.withSortBy(parameters.getSortBy())
				.withSortDirection(parameters.getSortDirection())
				.withTotalPages(matches.getTotalPages())
				.withTotalRecords(matches.getTotalElements())
				.withCount(customerDetails.size())
				.withLimit(parameters.getLimit()))
			.withCustomerDetails(customerDetails);
	}

	/**
	 * Method for converting result list into a Page object with sub list for requested page. Convertion must be done
	 * explicitly as stored procedures can not produce a return object of type Page and cant sort result list.
	 *
	 * @param parameters object containing input for calculating the current requested sub page for the result list
	 * @param matches with result to be converted to a paged list
	 * @return a Page object representing the sublist for the requested page of the list
	 */
	private Page<CustomerDetailsEntity> toPage(CustomerDetailsParameters parameters, List<CustomerDetailsEntity> matches) {

		// Convert list into a list of pages
		PagedListHolder<CustomerDetailsEntity> page = toPagedListHolder(parameters, matches);

		if (page.getPageCount() < parameters.getPage()) {
			return new PageImpl<>(Collections.emptyList(), toPageRequest(parameters), page.getNrOfElements());
		}
		return new PageImpl<>(page.getPageList(), PageRequest.of(page.getPage(), page.getPageSize(), parameters.sort()), page.getNrOfElements());
	}

	private PagedListHolder<CustomerDetailsEntity> toPagedListHolder(CustomerDetailsParameters parameters, List<CustomerDetailsEntity> matches) {
		PagedListHolder<CustomerDetailsEntity> page = new PagedListHolder<>(matches);
		page.setPage(parameters.getPage() - 1);
		page.setPageSize(parameters.getLimit());
		return page;
	}

	private PageRequest toPageRequest(CustomerDetailsParameters parameters) {
		return PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort());
	}

	private List<String> getCustomerOrgIdList(List<String> partyIds) {
		return ofNullable(partyIds).orElse(emptyList()).stream()
			.map(partyProvider::translateToLegalId)
			.toList();
	}

	private List<CustomerEngagement> switchToPartyId(List<CustomerEngagement> customerEngagements) {
		customerEngagements.forEach(engagement -> engagement
			.withPartyId(fetchPartyId(engagement.getCustomerType(), engagement.getCustomerOrgNumber()))
			.withCustomerOrgNumber(null)); // Needs to be reset to not expose person/organization number in response

		return customerEngagements;
	}

	private String fetchPartyId(CustomerType type, String orgNumber) {
		if (!hasText(orgNumber)) {
			LOGGER.info("CustomerEngagement did not contain a 'customerOrgNumber'. Skipping call to Party-service. {}", orgNumber);
			return null;
		}
		return partyProvider.translateToPartyId(toPartyType(type), removeHyphen(orgNumber));
	}
}
