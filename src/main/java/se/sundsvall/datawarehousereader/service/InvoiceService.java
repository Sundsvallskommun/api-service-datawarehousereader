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
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceDetailRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceJdbcRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;
import se.sundsvall.dept44.problem.Problem;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper.toDetails;
import static se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper.toInvoices;

@Service
public class InvoiceService {

	private static final String INVOICE_DETAIL_NOT_FOUND = "No invoicedetails found for invoice issuer '%s' and invoicenumber '%s'";

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

	public CustomerInvoiceResponse getInvoicesForCustomer(final String customerNumber, final CustomerInvoiceParameters parameters) {
		final var organizationIds = ofNullable(parameters.getOrganizationIds())
			.filter(ids -> !ids.isEmpty())
			.map(ids -> String.join(",", ids))
			.orElse(null);

		final var response = invoiceJdbcRepository.getInvoices(
			parameters.getPage(),
			parameters.getLimit(),
			organizationIds,
			customerNumber,
			parameters.getPeriodFrom(),
			parameters.getPeriodTo(),
			parameters.getSortBy());

		ofNullable(response.getInvoices()).orElse(emptyList())
			.forEach(invoice -> invoice.setDetails(toDetails(
				invoiceDetailRepository.findAllByOrganizationIdAndInvoiceNumber(invoice.getOrganizationNumber(), invoice.getInvoiceNumber()))));

		return response;
	}

	public List<InvoiceDetail> getInvoiceDetails(final String organizationNumber, final long invoiceNumber) {
		final var entities = invoiceDetailRepository.findAllByOrganizationIdAndInvoiceNumber(organizationNumber, invoiceNumber);
		if (entities.isEmpty()) {
			throw Problem.valueOf(NOT_FOUND, String.format(INVOICE_DETAIL_NOT_FOUND, organizationNumber, invoiceNumber));
		}

		return toDetails(entities);
	}
}
