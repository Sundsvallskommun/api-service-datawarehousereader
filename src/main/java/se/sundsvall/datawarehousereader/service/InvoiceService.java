package se.sundsvall.datawarehousereader.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
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
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
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
			.withStatus(parameters.getStatus())
			.withPeriodFrom(parameters.getPeriodFrom())
			.withPeriodTo(parameters.getPeriodTo())
			.withSortBy(parameters.getSortBy())
			.withSortDirection(parameters.getSortDirection());

		final var response = invoiceJdbcRepository.getInvoices(query);

		ofNullable(response.getInvoices()).orElse(emptyList())
			.forEach(invoice -> invoice.setDetails(toDetails(
				invoiceDetailRepository.findAllByOrganizationIdAndInvoiceNumber(invoice.getOrganizationNumber(), invoice.getInvoiceNumber()))));

		return response;
	}

	private static String toCommaSeparated(final List<String> values) {
		return ofNullable(values)
			.filter(list -> !list.isEmpty())
			.map(list -> String.join(",", list))
			.orElse(null);
	}

	public List<InvoiceDetail> getInvoiceDetails(final String organizationNumber, final long invoiceNumber) {
		return toDetails(invoiceDetailRepository.findAllByOrganizationIdAndInvoiceNumber(organizationNumber, invoiceNumber));
	}
}
