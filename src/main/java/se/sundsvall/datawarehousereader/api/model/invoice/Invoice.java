package se.sundsvall.datawarehousereader.api.model.invoice;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import se.sundsvall.datawarehousereader.api.model.CustomerType;

@Schema(description = "Invoice model")
public class Invoice {

	@Schema(description = "Customer number", example = "39195", accessMode = READ_ONLY)
	private String customerNumber;

	@Schema(implementation = CustomerType.class, accessMode = READ_ONLY)
	private CustomerType customerType;

	@Schema(description = "List of facility ids", example = "[\"735999109151404321\", \"735999109151401234\"]", accessMode = READ_ONLY)
	private Set<String> facilityIds;

	@Schema(description = "List of descriptions", example = "[\"Fjärrvärme\", \"Elnät\"]", accessMode = READ_ONLY)
	private Set<String> invoiceDescriptions;

	@Schema(description = "Invoice number", example = "767915994", accessMode = READ_ONLY)
	private Long invoiceNumber;

	@Schema(description = "Invoice date", example = "2022-01-15", accessMode = READ_ONLY)
	private LocalDate invoiceDate;

	@Schema(description = "invoice name", example = "765801493.pdf", accessMode = READ_ONLY)
	private String invoiceName;

	@Schema(description = "Invoice type", example = "Faktura", accessMode = READ_ONLY)
	private String invoiceType;

	@Schema(description = "Invoice status", example = "Skickad", accessMode = READ_ONLY)
	private String invoiceStatus;

	@Schema(description = "Ocr number", example = "767915994", accessMode = READ_ONLY)
	private Long ocrNumber;

	@Schema(description = "Due date", example = "2022-01-31", accessMode = READ_ONLY)
	private LocalDate dueDate;

	@Schema(description = "Total amount", example = "1116.00", accessMode = READ_ONLY)
	private BigDecimal totalAmount;

	@Schema(description = "Amount included VAT", example = "1115.62", accessMode = READ_ONLY)
	private BigDecimal amountVatIncluded;

	@Schema(description = "Amount excluded VAT", example = "892.50", accessMode = READ_ONLY)
	private BigDecimal amountVatExcluded;

	@Schema(description = "Amount eligible for VAT", example = "892.50", accessMode = READ_ONLY)
	private BigDecimal vatEligibleAmount;

	@Schema(description = "Rounding", example = "0.38", accessMode = READ_ONLY)
	private BigDecimal rounding;

	@Schema(description = "VAT", example = "892.50", accessMode = READ_ONLY)
	private BigDecimal vat;

	@Schema(description = "Reversed VAT", example = "false", accessMode = READ_ONLY)
	private Boolean reversedVat;

	@Schema(description = "Currency", example = "sek", accessMode = READ_ONLY)
	private String currency;

	@Schema(description = "Organization group", example = "stadsbacken", accessMode = READ_ONLY)
	private String organizationGroup;

	@Schema(description = "Organization number of invoice issuer", example = "5565027223", accessMode = READ_ONLY)
	private String organizationNumber;

	@Schema(description = "Adminstration", example = "Sundsvall Elnät", accessMode = READ_ONLY)
	private String administration;

	@Schema(description = "Street", example = "Storgatan 44", accessMode = READ_ONLY)
	private String street;

	@Schema(description = "Postal code", example = "85230", accessMode = READ_ONLY)
	private String postCode;

	@Schema(description = "City", example = "Sundsvall", accessMode = READ_ONLY)
	private String city;

	@Schema(description = "Care of address", example = "Agatha Malm", accessMode = READ_ONLY)
	private String careOf;

	@Schema(description = "Is pdf-version of invoice available", example = "false", accessMode = READ_ONLY)
	private Boolean pdfAvailable;

	public static Invoice create() {
		return new Invoice();
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public Invoice withCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public Invoice withCustomerType(CustomerType customerType) {
		this.customerType = customerType;
		return this;
	}

	public Set<String> getFacilityIds() {
		return facilityIds;
	}

	public Invoice withFacilityIds(Set<String> facilityIds) {
		this.facilityIds = facilityIds;
		return this;
	}

	public void setFacilityIds(Set<String> facilityIds) {
		this.facilityIds = facilityIds;
	}

	public Long getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Invoice withInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
		return this;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Invoice withInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
		return this;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public Invoice withInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
		return this;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Invoice withInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
		return this;
	}

	public Set<String> getInvoiceDescriptions() {
		return invoiceDescriptions;
	}

	public Invoice withInvoiceDescription(Set<String> invoiceDescriptions) {
		this.invoiceDescriptions = invoiceDescriptions;
		return this;
	}

	public void setInvoiceDescriptions(Set<String> invoiceDescriptions) {
		this.invoiceDescriptions = invoiceDescriptions;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Invoice withInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
		return this;
	}

	public Long getOcrNumber() {
		return ocrNumber;
	}

	public void setOcrNumber(Long ocrNumber) {
		this.ocrNumber = ocrNumber;
	}

	public Invoice withOcrNumber(Long ocrNumber) {
		this.ocrNumber = ocrNumber;
		return this;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public Invoice withDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
		return this;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Invoice withTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	public BigDecimal getAmountVatIncluded() {
		return amountVatIncluded;
	}

	public void setAmountVatIncluded(BigDecimal amountVatIncluded) {
		this.amountVatIncluded = amountVatIncluded;
	}

	public Invoice withAmountVatIncluded(BigDecimal amountVatIncluded) {
		this.amountVatIncluded = amountVatIncluded;
		return this;
	}

	public BigDecimal getAmountVatExcluded() {
		return amountVatExcluded;
	}

	public void setAmountVatExcluded(BigDecimal amountVatExcluded) {
		this.amountVatExcluded = amountVatExcluded;
	}

	public Invoice withAmountVatExcluded(BigDecimal amountVatExcluded) {
		this.amountVatExcluded = amountVatExcluded;
		return this;
	}

	public BigDecimal getVatEligibleAmount() {
		return vatEligibleAmount;
	}

	public void setVatEligibleAmount(BigDecimal vatEligibleAmount) {
		this.vatEligibleAmount = vatEligibleAmount;
	}

	public Invoice withVatEligibleAmount(BigDecimal vatEligibleAmount) {
		this.vatEligibleAmount = vatEligibleAmount;
		return this;
	}

	public BigDecimal getRounding() {
		return rounding;
	}

	public void setRounding(BigDecimal rounding) {
		this.rounding = rounding;
	}

	public Invoice withRounding(BigDecimal rounding) {
		this.rounding = rounding;
		return this;
	}

	public BigDecimal getVat() {
		return vat;
	}

	public void setVat(BigDecimal vat) {
		this.vat = vat;
	}

	public Invoice withVat(BigDecimal vat) {
		this.vat = vat;
		return this;
	}

	public Boolean getReversedVat() {
		return reversedVat;
	}

	public void setReversedVat(Boolean reversedVat) {
		this.reversedVat = reversedVat;
	}

	public Invoice withReversedVat(Boolean reversedVat) {
		this.reversedVat = reversedVat;
		return this;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Invoice withCurrency(String currency) {
		this.currency = currency;
		return this;
	}

	public String getOrganizationGroup() {
		return organizationGroup;
	}

	public void setOrganizationGroup(String organizationGroup) {
		this.organizationGroup = organizationGroup;
	}

	public Invoice withOrganizationGroup(String organizationGroup) {
		this.organizationGroup = organizationGroup;
		return this;
	}

	public String getOrganizationNumber() {
		return organizationNumber;
	}

	public void setOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	public Invoice withOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
		return this;
	}

	public String getAdministration() {
		return administration;
	}

	public void setAdministration(String administration) {
		this.administration = administration;
	}

	public Invoice withAdministration(String administration) {
		this.administration = administration;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Invoice withStreet(String street) {
		this.street = street;
		return this;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Invoice withPostCode(String postCode) {
		this.postCode = postCode;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Invoice withCity(String city) {
		this.city = city;
		return this;
	}

	public String getCareOf() {
		return careOf;
	}

	public void setCareOf(String careOf) {
		this.careOf = careOf;
	}

	public Invoice withCareOf(String careOf) {
		this.careOf = careOf;
		return this;

	}

	public Boolean getPdfAvailable() {
		return pdfAvailable;
	}

	public void setPdfAvailable(Boolean pdfAvailable) {
		this.pdfAvailable = pdfAvailable;
	}

	public Invoice withPdfAvailable(Boolean pdfAvailable) {
		this.pdfAvailable = pdfAvailable;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		Invoice invoice = (Invoice) o;
		return Objects.equals(customerNumber, invoice.customerNumber) && customerType == invoice.customerType && Objects.equals(facilityIds, invoice.facilityIds) && Objects.equals(invoiceDescriptions, invoice.invoiceDescriptions)
			&& Objects.equals(invoiceNumber, invoice.invoiceNumber) && Objects.equals(invoiceDate, invoice.invoiceDate) && Objects.equals(invoiceName, invoice.invoiceName) && Objects.equals(invoiceType,
				invoice.invoiceType) && Objects.equals(invoiceStatus, invoice.invoiceStatus) && Objects.equals(ocrNumber, invoice.ocrNumber) && Objects.equals(dueDate, invoice.dueDate) && Objects.equals(totalAmount,
					invoice.totalAmount) && Objects.equals(amountVatIncluded, invoice.amountVatIncluded) && Objects.equals(amountVatExcluded, invoice.amountVatExcluded) && Objects.equals(vatEligibleAmount, invoice.vatEligibleAmount)
			&& Objects.equals(rounding, invoice.rounding) && Objects.equals(vat, invoice.vat) && Objects.equals(reversedVat, invoice.reversedVat) && Objects.equals(currency, invoice.currency) && Objects.equals(
				organizationGroup, invoice.organizationGroup) && Objects.equals(organizationNumber, invoice.organizationNumber) && Objects.equals(administration, invoice.administration) && Objects.equals(street, invoice.street)
			&& Objects.equals(postCode, invoice.postCode) && Objects.equals(city, invoice.city) && Objects.equals(careOf, invoice.careOf) && Objects.equals(pdfAvailable, invoice.pdfAvailable);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerNumber, customerType, facilityIds, invoiceDescriptions, invoiceNumber, invoiceDate, invoiceName, invoiceType, invoiceStatus, ocrNumber, dueDate, totalAmount, amountVatIncluded, amountVatExcluded, vatEligibleAmount,
			rounding,
			vat, reversedVat, currency, organizationGroup, organizationNumber, administration, street, postCode, city, careOf, pdfAvailable);
	}

	@Override
	public String toString() {
		return "Invoice{" +
			"customerNumber='" + customerNumber + '\'' +
			", customerType=" + customerType +
			", facilityIds=" + facilityIds +
			", invoiceDescriptions=" + invoiceDescriptions +
			", invoiceNumber=" + invoiceNumber +
			", invoiceDate=" + invoiceDate +
			", invoiceName='" + invoiceName + '\'' +
			", invoiceType='" + invoiceType + '\'' +
			", invoiceStatus='" + invoiceStatus + '\'' +
			", ocrNumber=" + ocrNumber +
			", dueDate=" + dueDate +
			", totalAmount=" + totalAmount +
			", amountVatIncluded=" + amountVatIncluded +
			", amountVatExcluded=" + amountVatExcluded +
			", vatEligibleAmount=" + vatEligibleAmount +
			", rounding=" + rounding +
			", vat=" + vat +
			", reversedVat=" + reversedVat +
			", currency='" + currency + '\'' +
			", organizationGroup='" + organizationGroup + '\'' +
			", organizationNumber='" + organizationNumber + '\'' +
			", administration='" + administration + '\'' +
			", street='" + street + '\'' +
			", postCode='" + postCode + '\'' +
			", city='" + city + '\'' +
			", careOf='" + careOf + '\'' +
			", pdfAvailable=" + pdfAvailable +
			'}';
	}
}
