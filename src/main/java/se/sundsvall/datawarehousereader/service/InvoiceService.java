package se.sundsvall.datawarehousereader.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.datawarehousereader.api.model.invoice.Invoice;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceDetail;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceParameters;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceDetailRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import static java.util.stream.Collectors.groupingBy;
import static se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper.toDetails;
import static se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper.toInvoices;

@Service
public class InvoiceService {

	private static final String INVOICE_DETAIL_NOT_FOUND = "No invoicedetails found for invoice issuer '%s' and invoicenumber '%s'";

	private final InvoiceRepository invoiceRepository;

	private final InvoiceDetailRepository invoiceDetailRepository;

	InvoiceService(final InvoiceRepository invoiceRepository, final InvoiceDetailRepository invoiceDetailRepository) {
		this.invoiceRepository = invoiceRepository;
		this.invoiceDetailRepository = invoiceDetailRepository;
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

	public List<InvoiceDetail> getInvoiceDetails(final String organizationNumber, final long invoiceNumber) {
		final var entities = invoiceDetailRepository.findAllByOrganizationIdAndInvoiceNumber(organizationNumber, invoiceNumber);
		if (entities.isEmpty()) {
			throw Problem.valueOf(Status.NOT_FOUND, String.format(INVOICE_DETAIL_NOT_FOUND, organizationNumber, invoiceNumber));
		}

		return toDetails(entities);
	}
}
