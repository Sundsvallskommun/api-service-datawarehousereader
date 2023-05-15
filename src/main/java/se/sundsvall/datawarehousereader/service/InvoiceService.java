package se.sundsvall.datawarehousereader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.datawarehousereader.api.model.MetaData;
import se.sundsvall.datawarehousereader.api.model.invoice.Invoice;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceDetail;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceParameters;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceDetailRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceRepository;

import java.util.Collections;
import java.util.List;

import static se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper.toDetails;
import static se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper.toInvoices;

@Service
public class InvoiceService {
	private static final String INVOICE_DETAIL_NOT_FOUND = "No invoicedetails found for invoice issuer '%s' and invoicenumber '%s'";

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private InvoiceDetailRepository invoiceDetailRepository;

	public InvoiceResponse getInvoices(final InvoiceParameters parameters) {
		final var matches = invoiceRepository.findAllByParameters(parameters, PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));

		// If page larger than last page is requested, a empty list is returned otherwise the current page
		final List<Invoice> invoices = matches.getTotalPages() < parameters.getPage() ? Collections.emptyList() : toInvoices(matches.getContent());

		return InvoiceResponse.create()
			.withMetaData(MetaData.create()
				.withPage(parameters.getPage())
				.withSortBy(parameters.getSortBy())
				.withSortDirection(parameters.getSortDirection())
				.withTotalPages(matches.getTotalPages())
				.withTotalRecords(matches.getTotalElements())
				.withCount(invoices.size())
				.withLimit(parameters.getLimit()))
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
