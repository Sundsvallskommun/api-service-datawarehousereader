package se.sundsvall.datawarehousereader.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoice;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoiceParameters;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoiceResponse;
import se.sundsvall.datawarehousereader.api.model.invoice.Invoice;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceDetail;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceParameters;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.CustomerInvoiceQuery;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceDetailRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceJdbcRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceDetailEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper.toDetails;
import static se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper.toInvoices;

@Service
public class InvoiceService {

	private final InvoiceRepository invoiceRepository;

	private final InvoiceDetailRepository invoiceDetailRepository;

	private final InvoiceJdbcRepository invoiceJdbcRepository;

	InvoiceService(final InvoiceRepository invoiceRepository, final InvoiceDetailRepository invoiceDetailRepository,
		final InvoiceJdbcRepository invoiceJdbcRepository) {
		this.invoiceRepository = invoiceRepository;
		this.invoiceDetailRepository = invoiceDetailRepository;
		this.invoiceJdbcRepository = invoiceJdbcRepository;
	}

	public InvoiceResponse getInvoices(final InvoiceParameters parameters) {
		var pageable = PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort());

		Page<Long> invoiceNumbers = invoiceRepository.findDistinctInvoiceNumbers(parameters, pageable);

		var invoiceEntities = invoiceRepository.findAllByInvoiceNumberIn(invoiceNumbers.getContent());

		var invoiceMap = invoiceEntities.stream()
			.collect(groupingBy(InvoiceEntity::getInvoiceNumber, Collectors.toList()));

		List<Invoice> invoices = invoiceNumbers.getTotalPages() < parameters.getPage() ? Collections.emptyList() : toInvoices(invoiceMap);

		return InvoiceResponse.create()
			.withMetaData(PagingAndSortingMetaData.create().withPageData(invoiceNumbers))
			.withInvoices(invoices);
	}

	public CustomerInvoiceResponse getInvoicesForCustomer(final CustomerInvoiceParameters parameters) {
		final var query = CustomerInvoiceQuery.create()
			.withPage(parameters.getPage())
			.withLimit(parameters.getLimit())
			.withCustomerIds(toCommaSeparated(parameters.getCustomerNumbers()))
			.withOrganizationIds(toCommaSeparated(parameters.getOrganizationIds()))
			.withFacilityIds(toCommaSeparated(parameters.getFacilityIds()))
			.withInvoiceNumbers(toCommaSeparatedNumbers(parameters.getInvoiceNumbers()))
			.withStatus(parameters.getStatus())
			.withPeriodFrom(parameters.getPeriodFrom())
			.withPeriodTo(parameters.getPeriodTo())
			.withSortBy(parameters.getSortBy())
			.withSortDirection(parameters.getSortDirection());

		final var response = invoiceJdbcRepository.getInvoices(query);

		final var invoices = ofNullable(response.getInvoices()).orElse(emptyList());
		final var detailsByInvoiceNumber = fetchDetails(invoices);

		invoices.forEach(invoice -> invoice.setDetails(toDetails(
			detailsByInvoiceNumber.getOrDefault(invoice.getInvoiceNumber(), emptyList()))));

		return response;
	}

	/**
	 * Fetches the details for every invoice on the page in a single query, keyed by invoice number. Invoice numbers are
	 * unique across organizations, so the organization number is not needed to identify the details of an invoice.
	 * <p>
	 * Note that the primary keys in {@code src/test/resources/db/schema/schema.sql} are generated from the entity
	 * mappings and therefore say nothing about the third party database. The assumption was instead verified directly
	 * against Stadsbacken on 2026-08-25: no invoice number in {@code kundinfo.vInvoiceDetail} carries rows for more than
	 * one {@code OrganizationId}, and no row has a null {@code OrganizationId}. Should that ever change, this lookup
	 * would attach the details of every organization sharing an invoice number to the same invoice.
	 */
	private Map<Long, List<InvoiceDetailEntity>> fetchDetails(final List<CustomerInvoice> invoices) {
		final var invoiceNumbers = invoices.stream()
			.map(CustomerInvoice::getInvoiceNumber)
			.filter(Objects::nonNull)
			.distinct()
			.toList();

		if (invoiceNumbers.isEmpty()) {
			return emptyMap();
		}

		return invoiceDetailRepository.findAllByInvoiceNumberIn(invoiceNumbers).stream()
			.collect(groupingBy(InvoiceDetailEntity::getInvoiceNumber));
	}

	private static String toCommaSeparated(final List<String> values) {
		return ofNullable(values)
			.filter(list -> !list.isEmpty())
			.map(list -> String.join(",", list))
			.orElse(null);
	}

	private static String toCommaSeparatedNumbers(final List<Long> values) {
		return ofNullable(values)
			.filter(list -> !list.isEmpty())
			.map(list -> list.stream().map(String::valueOf).collect(joining(",")))
			.orElse(null);
	}

	public List<InvoiceDetail> getInvoiceDetails(final String organizationNumber, final long invoiceNumber) {
		return toDetails(invoiceDetailRepository.findAllByOrganizationIdAndInvoiceNumber(organizationNumber, invoiceNumber));
	}
}
