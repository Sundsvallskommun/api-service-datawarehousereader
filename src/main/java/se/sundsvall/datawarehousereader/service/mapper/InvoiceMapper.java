package se.sundsvall.datawarehousereader.service.mapper;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static se.sundsvall.datawarehousereader.Constants.UNKNOWN_CUSTOMER_TYPE;
import static se.sundsvall.datawarehousereader.api.model.CustomerType.fromValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import se.sundsvall.datawarehousereader.api.model.invoice.Invoice;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceDetail;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceDetailEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;
import se.sundsvall.datawarehousereader.service.util.ServiceUtil;

public class InvoiceMapper {

	private InvoiceMapper() {}

	public static List<Invoice> toInvoices(List<InvoiceEntity> entities) {
		return ofNullable(entities).orElse(emptyList()).stream()
			.map(InvoiceMapper::toInvoice)
			.toList();
	}

	public static List<Invoice> toInvoices(Map<Long, List<InvoiceEntity>> invoiceMap) {
		return invoiceMap.values().stream()
			.map(InvoiceMapper::toInvoice)
			.toList();
	}

	private static Invoice toInvoice(List<InvoiceEntity> entities) {
		var facilityIds = entities.stream()
			.map(InvoiceEntity::getFacilityId)
			.collect(Collectors.toSet());
		var invoiceDescriptions = entities.stream()
			.map(InvoiceEntity::getInvoiceDescription)
			.collect(Collectors.toSet());

		return Invoice.create()
			.withAdministration(entities.getFirst().getAdministration())
			.withAmountVatExcluded(entities.getFirst().getAmountVatExcluded())
			.withAmountVatIncluded(entities.getFirst().getAmountVatIncluded())
			.withCareOf(entities.getFirst().getCareOf())
			.withCity(entities.getFirst().getCity())
			.withCurrency(entities.getFirst().getCurrency())
			.withCustomerNumber(ServiceUtil.toString(entities.getFirst().getCustomerId()))
			.withCustomerType(fromValue(entities.getFirst().getCustomerType(), INTERNAL_SERVER_ERROR, UNKNOWN_CUSTOMER_TYPE))
			.withDueDate(entities.getFirst().getDueDate())
			.withFacilityIds(facilityIds)
			.withInvoiceDate(entities.getFirst().getInvoiceDate())
			.withInvoiceDescription(invoiceDescriptions)
			.withInvoiceName(entities.getFirst().getInvoiceName())
			.withInvoiceNumber(entities.getFirst().getInvoiceNumber())
			.withInvoiceStatus(entities.getFirst().getInvoiceStatus())
			.withInvoiceType(entities.getFirst().getInvoiceType())
			.withOcrNumber(entities.getFirst().getOcrNumber())
			.withOrganizationGroup(entities.getFirst().getOrganizationGroup())
			.withOrganizationNumber(entities.getFirst().getOrganizationId())
			.withPostCode(entities.getFirst().getPostCode())
			.withReversedVat(entities.getFirst().getReversedVat())
			.withRounding(entities.getFirst().getRounding())
			.withStreet(entities.getFirst().getStreet())
			.withTotalAmount(entities.getFirst().getTotalAmount())
			.withVat(entities.getFirst().getVat())
			.withVatEligibleAmount(entities.getFirst().getVatEligibleAmount())
			.withPdfAvailable(entities.getFirst().getPdfAvailable());
	}

	private static Invoice toInvoice(InvoiceEntity entity) {
		return Invoice.create()
			.withAdministration(entity.getAdministration())
			.withAmountVatExcluded(entity.getAmountVatExcluded())
			.withAmountVatIncluded(entity.getAmountVatIncluded())
			.withCareOf(entity.getCareOf())
			.withCity(entity.getCity())
			.withCurrency(entity.getCurrency())
			.withCustomerNumber(ServiceUtil.toString(entity.getCustomerId()))
			.withCustomerType(fromValue(entity.getCustomerType(), INTERNAL_SERVER_ERROR, UNKNOWN_CUSTOMER_TYPE))
			.withDueDate(entity.getDueDate())
			.withFacilityIds(Collections.singleton(entity.getFacilityId()))
			.withInvoiceDate(entity.getInvoiceDate())
			.withInvoiceDescription(Collections.singleton(entity.getInvoiceDescription()))
			.withInvoiceName(entity.getInvoiceName())
			.withInvoiceNumber(entity.getInvoiceNumber())
			.withInvoiceStatus(entity.getInvoiceStatus())
			.withInvoiceType(entity.getInvoiceType())
			.withOcrNumber(entity.getOcrNumber())
			.withOrganizationGroup(entity.getOrganizationGroup())
			.withOrganizationNumber(entity.getOrganizationId())
			.withPostCode(entity.getPostCode())
			.withReversedVat(entity.getReversedVat())
			.withRounding(entity.getRounding())
			.withStreet(entity.getStreet())
			.withTotalAmount(entity.getTotalAmount())
			.withVat(entity.getVat())
			.withVatEligibleAmount(entity.getVatEligibleAmount())
			.withPdfAvailable(entity.getPdfAvailable());
	}

	public static List<InvoiceDetail> toDetails(List<InvoiceDetailEntity> entities) {
		return ofNullable(entities).orElse(emptyList()).stream()
			.map(InvoiceMapper::toDetail)
			.toList();
	}

	private static InvoiceDetail toDetail(InvoiceDetailEntity entity) {
		return InvoiceDetail.create()
			.withAmount(entity.getAmount())
			.withAmountVatExcluded(entity.getAmountVatExcluded())
			.withDescription(entity.getDescription())
			.withInvoiceNumber(entity.getInvoiceNumber())
			.withOrganizationNumber(entity.getOrganizationId())
			.withPeriodFrom(entity.getPeriodFrom())
			.withPeriodTo(entity.getPeriodTo())
			.withProductCode(entity.getProductCode())
			.withProductName(entity.getProductName())
			.withQuantity(entity.getQuantity())
			.withUnit(entity.getUnit())
			.withUnitPrice(entity.getUnitPrice())
			.withVat(entity.getVat())
			.withVatRate(entity.getVatRate());
	}
}
