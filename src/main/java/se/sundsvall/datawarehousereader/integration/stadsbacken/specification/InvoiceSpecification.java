package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.nonNull;

public interface InvoiceSpecification {

	SpecificationBuilder<InvoiceEntity> BUILDER = new SpecificationBuilder<>();
	static Specification<InvoiceEntity> withCustomerIds(List<Integer> customerIds) {
		return BUILDER.buildInFilterForInteger("customerId", customerIds);
	}

	static Specification<InvoiceEntity> withCustomerType(CustomerType customerType) {
		return BUILDER.buildEqualFilter("customerType", nonNull(customerType) ? customerType.getStadsbackenTranslation() : null);
	}

	static Specification<InvoiceEntity> withFacilityIds(List<String> facilityIds) {
		return BUILDER.buildInFilterForString("facilityId", facilityIds);
	}

	static Specification<InvoiceEntity> withAdministration(String administration) {
		return BUILDER.buildEqualFilter("administration", administration);
	}
	
	static Specification<InvoiceEntity> withOcrNumber(Long ocrNumber) {
		return BUILDER.buildEqualFilter("ocrNumber", ocrNumber);
	}

	static Specification<InvoiceEntity> withInvoiceDate(LocalDate dateFrom, LocalDate dateTo) {
		return BUILDER.buildDateFilter("invoiceDate", dateFrom, dateTo);
	}

	static Specification<InvoiceEntity> withInvoiceName(String invoiceName) {
		return BUILDER.buildEqualFilter("invoiceName", invoiceName);
	}
	
	static Specification<InvoiceEntity> withInvoiceNumber(Long invoiceNumber) {
		return BUILDER.buildEqualFilter("invoiceNumber", invoiceNumber);
	}

	static Specification<InvoiceEntity> withInvoiceType(String invoiceType) {
		return BUILDER.buildEqualFilter("invoiceType", invoiceType);
	}

	static Specification<InvoiceEntity> withInvoiceStatus(String invoiceStatus) {
		return BUILDER.buildEqualFilter("invoiceStatus", invoiceStatus);
	}

	static Specification<InvoiceEntity> withDueDate(LocalDate dateFrom, LocalDate dateTo) {
		return BUILDER.buildDateFilter("dueDate", dateFrom, dateTo);
	}

	static Specification<InvoiceEntity> withOrganizationGroup(String organizationGroup) {
		return BUILDER.buildEqualFilter("organizationGroup", organizationGroup);
	}

	static Specification<InvoiceEntity> withOrganizationId(String organizationId) {
		return BUILDER.buildEqualFilter("organizationId", organizationId);
	}
}
