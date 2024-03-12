package se.sundsvall.datawarehousereader.service;

import static se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper.toDetails;
import static se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper.toInvoices;

import java.util.Collections;
import java.util.List;

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
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

@Service
public class InvoiceService {

	private static final String INVOICE_DETAIL_NOT_FOUND = "No invoicedetails found for invoice issuer '%s' and invoicenumber '%s'";

	private final InvoiceRepository invoiceRepository;

	private final InvoiceDetailRepository invoiceDetailRepository;

	public InvoiceService(final InvoiceRepository invoiceRepository, final InvoiceDetailRepository invoiceDetailRepository) {
		this.invoiceRepository = invoiceRepository;
		this.invoiceDetailRepository = invoiceDetailRepository;
	}

	public InvoiceResponse getInvoices(final InvoiceParameters parameters) {
		final var matches = invoiceRepository.findAllByParameters(parameters, PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));

		// If page larger than last page is requested, a empty list is returned otherwise the current page
		final List<Invoice> invoices = matches.getTotalPages() < parameters.getPage() ? Collections.emptyList() : toInvoices(matches.getContent());

		return InvoiceResponse.create()
			.withMetaData(PagingAndSortingMetaData.create().withPageData(matches))
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
