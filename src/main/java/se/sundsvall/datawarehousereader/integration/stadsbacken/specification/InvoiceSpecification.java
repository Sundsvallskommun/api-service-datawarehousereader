package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;

import static java.util.Objects.nonNull;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.ADMINISTRATION;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.CUSTOMER_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.CUSTOMER_TYPE;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.DUE_DATE;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.FACILITY_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.INVOICE_DATE;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.INVOICE_NAME;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.INVOICE_NUMBER;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.INVOICE_STATUS;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.INVOICE_TYPE;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.OCR_NUMBER;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.ORGANIZATION_GROUP;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity_.ORGANIZATION_ID;
import static se.sundsvall.datawarehousereader.service.util.ServiceUtil.toIntegers;

public interface InvoiceSpecification {

	SpecificationBuilder<InvoiceEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<InvoiceEntity> createSpecification(final InvoiceParameters parameters) {
		return Specification.allOf(withAdministration(parameters.getAdministration())
			.and(withCustomerIds(toIntegers(parameters.getCustomerNumber()))
				.and(withCustomerType(parameters.getCustomerType()))
				.and(withDueDate(parameters.getDueDateFrom(), parameters.getDueDateTo()))
				.and(withFacilityIds(parameters.getFacilityIds())))
			.and(withInvoiceDate(parameters.getInvoiceDateFrom(), parameters.getInvoiceDateTo()))
			.and(withInvoiceName(parameters.getInvoiceName())).and(withInvoiceNumber(parameters.getInvoiceNumber()))
			.and(withInvoiceStatus(parameters.getInvoiceStatus()))
			.and(withInvoiceType(parameters.getInvoiceType()))
			.and(withOcrNumber(parameters.getOcrNumber()))
			.and(withOrganizationGroup(parameters.getOrganizationGroup()))
			.and(withOrganizationIds(parameters.getOrganizationNumbers())));
	}

	static Specification<InvoiceEntity> withCustomerIds(final List<Integer> customerIds) {
		return BUILDER.buildInFilterForInteger(CUSTOMER_ID, customerIds);
	}

	static Specification<InvoiceEntity> withCustomerType(final CustomerType customerType) {
		return BUILDER.buildEqualFilter(CUSTOMER_TYPE, nonNull(customerType) ? customerType.getStadsbackenTranslation() : null);
	}

	static Specification<InvoiceEntity> withFacilityIds(final List<String> facilityIds) {
		return BUILDER.buildInFilterForString(FACILITY_ID, facilityIds);
	}

	static Specification<InvoiceEntity> withAdministration(final String administration) {
		return BUILDER.buildEqualFilter(ADMINISTRATION, administration);
	}

	static Specification<InvoiceEntity> withOcrNumber(final Long ocrNumber) {
		return BUILDER.buildEqualFilter(OCR_NUMBER, ocrNumber);
	}

	static Specification<InvoiceEntity> withInvoiceDate(final LocalDate dateFrom, final LocalDate dateTo) {
		return BUILDER.buildDateFilter(INVOICE_DATE, dateFrom, dateTo);
	}

	static Specification<InvoiceEntity> withInvoiceName(final String invoiceName) {
		return BUILDER.buildEqualFilter(INVOICE_NAME, invoiceName);
	}

	static Specification<InvoiceEntity> withInvoiceNumber(final Long invoiceNumber) {
		return BUILDER.buildEqualFilter(INVOICE_NUMBER, invoiceNumber);
	}

	static Specification<InvoiceEntity> withInvoiceType(final String invoiceType) {
		return BUILDER.buildEqualFilter(INVOICE_TYPE, invoiceType);
	}

	static Specification<InvoiceEntity> withInvoiceStatus(final String invoiceStatus) {
		return BUILDER.buildEqualFilter(INVOICE_STATUS, invoiceStatus);
	}

	static Specification<InvoiceEntity> withDueDate(final LocalDate dateFrom, final LocalDate dateTo) {
		return BUILDER.buildDateFilter(DUE_DATE, dateFrom, dateTo);
	}

	static Specification<InvoiceEntity> withOrganizationGroup(final String organizationGroup) {
		return BUILDER.buildEqualFilter(ORGANIZATION_GROUP, organizationGroup);
	}

	static Specification<InvoiceEntity> withOrganizationIds(final List<String> organizationIds) {
		return BUILDER.buildInFilterForString(ORGANIZATION_ID, organizationIds);
	}
}
