package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

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

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;

public interface InvoiceSpecification {

	SpecificationBuilder<InvoiceEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<InvoiceEntity> withCustomerIds(List<Integer> customerIds) {
		return BUILDER.buildInFilterForInteger(CUSTOMER_ID, customerIds);
	}

	static Specification<InvoiceEntity> withCustomerType(CustomerType customerType) {
		return BUILDER.buildEqualFilter(CUSTOMER_TYPE, nonNull(customerType) ? customerType.getStadsbackenTranslation() : null);
	}

	static Specification<InvoiceEntity> withFacilityIds(List<String> facilityIds) {
		return BUILDER.buildInFilterForString(FACILITY_ID, facilityIds);
	}

	static Specification<InvoiceEntity> withAdministration(String administration) {
		return BUILDER.buildEqualFilter(ADMINISTRATION, administration);
	}

	static Specification<InvoiceEntity> withOcrNumber(Long ocrNumber) {
		return BUILDER.buildEqualFilter(OCR_NUMBER, ocrNumber);
	}

	static Specification<InvoiceEntity> withInvoiceDate(LocalDate dateFrom, LocalDate dateTo) {
		return BUILDER.buildDateFilter(INVOICE_DATE, dateFrom, dateTo);
	}

	static Specification<InvoiceEntity> withInvoiceName(String invoiceName) {
		return BUILDER.buildEqualFilter(INVOICE_NAME, invoiceName);
	}

	static Specification<InvoiceEntity> withInvoiceNumber(Long invoiceNumber) {
		return BUILDER.buildEqualFilter(INVOICE_NUMBER, invoiceNumber);
	}

	static Specification<InvoiceEntity> withInvoiceType(String invoiceType) {
		return BUILDER.buildEqualFilter(INVOICE_TYPE, invoiceType);
	}

	static Specification<InvoiceEntity> withInvoiceStatus(String invoiceStatus) {
		return BUILDER.buildEqualFilter(INVOICE_STATUS, invoiceStatus);
	}

	static Specification<InvoiceEntity> withDueDate(LocalDate dateFrom, LocalDate dateTo) {
		return BUILDER.buildDateFilter(DUE_DATE, dateFrom, dateTo);
	}

	static Specification<InvoiceEntity> withOrganizationGroup(String organizationGroup) {
		return BUILDER.buildEqualFilter(ORGANIZATION_GROUP, organizationGroup);
	}

	static Specification<InvoiceEntity> withOrganizationId(String organizationId) {
		return BUILDER.buildEqualFilter(ORGANIZATION_ID, organizationId);
	}
}
