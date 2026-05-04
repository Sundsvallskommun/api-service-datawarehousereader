package se.sundsvall.datawarehousereader.api.model.invoice;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import se.sundsvall.datawarehousereader.api.model.CustomerType;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Schema(description = "Customer invoice model")
public class CustomerInvoice {

	@Schema(description = "Customer number", examples = "123456", accessMode = READ_ONLY)
	private String customerNumber;

	@Schema(implementation = CustomerType.class, accessMode = READ_ONLY)
	private CustomerType customerType;

	@Schema(description = "Facility id", examples = "735999109425048010", accessMode = READ_ONLY)
	private String facilityId;

	@Schema(description = "Invoice number", examples = "123456789", accessMode = READ_ONLY)
	private Long invoiceNumber;

	@Schema(description = "Invoice id", examples = "1062916396", accessMode = READ_ONLY)
	private Long invoiceId;

	@Schema(description = "Joint invoice id", examples = "123", accessMode = READ_ONLY)
	private Long jointInvoiceId;

	@Schema(description = "Invoice date", examples = "2025-10-08", accessMode = READ_ONLY)
	private LocalDate invoiceDate;

	@Schema(description = "Invoice name", examples = "123456789.pdf", accessMode = READ_ONLY)
	private String invoiceName;

	@Schema(description = "Invoice type", examples = "Faktura", accessMode = READ_ONLY)
	private String invoiceType;

	@Schema(description = "Invoice description", examples = "El", accessMode = READ_ONLY)
	private String invoiceDescription;

	@Schema(description = "Invoice status", examples = "Betalad", accessMode = READ_ONLY)
	private String invoiceStatus;

	@Schema(description = "OCR number", examples = "295334999", accessMode = READ_ONLY)
	private Long ocrNumber;

	@Schema(description = "Due date", examples = "2025-10-28", accessMode = READ_ONLY)
	private LocalDate dueDate;

	@Schema(description = "Invoice period start", examples = "2025-09-01", accessMode = READ_ONLY)
	private LocalDate periodFrom;

	@Schema(description = "Invoice period end", examples = "2025-09-30", accessMode = READ_ONLY)
	private LocalDate periodTo;

	@Schema(description = "Total amount", examples = "1234.00", accessMode = READ_ONLY)
	private BigDecimal totalAmount;

	@Schema(description = "Amount included VAT", examples = "1233.51", accessMode = READ_ONLY)
	private BigDecimal amountVatIncluded;

	@Schema(description = "Amount excluded VAT", examples = "986.81", accessMode = READ_ONLY)
	private BigDecimal amountVatExcluded;

	@Schema(description = "Amount eligible for VAT", examples = "986.81", accessMode = READ_ONLY)
	private BigDecimal vatEligibleAmount;

	@Schema(description = "Rounding", examples = "0.49", accessMode = READ_ONLY)
	private BigDecimal rounding;

	@Schema(description = "Organization group", examples = "stadsbacken", accessMode = READ_ONLY)
	private String organizationGroup;

	@Schema(description = "Organization number of invoice issuer", examples = "5565027223", accessMode = READ_ONLY)
	private String organizationNumber;

	@Schema(description = "Administration", examples = "Sundsvall Elnät", accessMode = READ_ONLY)
	private String administration;

	@Schema(description = "Street", examples = "Ankeborgsvägen 11", accessMode = READ_ONLY)
	private String street;

	@Schema(description = "Postal code", examples = "87654", accessMode = READ_ONLY)
	private String postCode;

	@Schema(description = "City", examples = "Sundsvall", accessMode = READ_ONLY)
	private String city;

	@Schema(description = "Care of address", examples = "Anka Kalle", accessMode = READ_ONLY)
	private String careOf;

	@Schema(description = "Invoice reference", accessMode = READ_ONLY)
	private String invoiceReference;

	@Schema(description = "Is pdf-version of invoice available", examples = "true", accessMode = READ_ONLY)
	private Boolean pdfAvailable;

	@ArraySchema(schema = @Schema(implementation = InvoiceDetail.class, accessMode = READ_ONLY))
	private List<InvoiceDetail> details;

	public static CustomerInvoice create() {
		return new CustomerInvoice();
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(final String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public CustomerInvoice withCustomerNumber(final String customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(final CustomerType customerType) {
		this.customerType = customerType;
	}

	public CustomerInvoice withCustomerType(final CustomerType customerType) {
		this.customerType = customerType;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(final String facilityId) {
		this.facilityId = facilityId;
	}

	public CustomerInvoice withFacilityId(final String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public Long getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(final Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public CustomerInvoice withInvoiceNumber(final Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
		return this;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(final Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public CustomerInvoice withInvoiceId(final Long invoiceId) {
		this.invoiceId = invoiceId;
		return this;
	}

	public Long getJointInvoiceId() {
		return jointInvoiceId;
	}

	public void setJointInvoiceId(final Long jointInvoiceId) {
		this.jointInvoiceId = jointInvoiceId;
	}

	public CustomerInvoice withJointInvoiceId(final Long jointInvoiceId) {
		this.jointInvoiceId = jointInvoiceId;
		return this;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(final LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public CustomerInvoice withInvoiceDate(final LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
		return this;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(final String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public CustomerInvoice withInvoiceName(final String invoiceName) {
		this.invoiceName = invoiceName;
		return this;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(final String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public CustomerInvoice withInvoiceType(final String invoiceType) {
		this.invoiceType = invoiceType;
		return this;
	}

	public String getInvoiceDescription() {
		return invoiceDescription;
	}

	public void setInvoiceDescription(final String invoiceDescription) {
		this.invoiceDescription = invoiceDescription;
	}

	public CustomerInvoice withInvoiceDescription(final String invoiceDescription) {
		this.invoiceDescription = invoiceDescription;
		return this;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(final String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public CustomerInvoice withInvoiceStatus(final String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
		return this;
	}

	public Long getOcrNumber() {
		return ocrNumber;
	}

	public void setOcrNumber(final Long ocrNumber) {
		this.ocrNumber = ocrNumber;
	}

	public CustomerInvoice withOcrNumber(final Long ocrNumber) {
		this.ocrNumber = ocrNumber;
		return this;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(final LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public CustomerInvoice withDueDate(final LocalDate dueDate) {
		this.dueDate = dueDate;
		return this;
	}

	public LocalDate getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(final LocalDate periodFrom) {
		this.periodFrom = periodFrom;
	}

	public CustomerInvoice withPeriodFrom(final LocalDate periodFrom) {
		this.periodFrom = periodFrom;
		return this;
	}

	public LocalDate getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(final LocalDate periodTo) {
		this.periodTo = periodTo;
	}

	public CustomerInvoice withPeriodTo(final LocalDate periodTo) {
		this.periodTo = periodTo;
		return this;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(final BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public CustomerInvoice withTotalAmount(final BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	public BigDecimal getAmountVatIncluded() {
		return amountVatIncluded;
	}

	public void setAmountVatIncluded(final BigDecimal amountVatIncluded) {
		this.amountVatIncluded = amountVatIncluded;
	}

	public CustomerInvoice withAmountVatIncluded(final BigDecimal amountVatIncluded) {
		this.amountVatIncluded = amountVatIncluded;
		return this;
	}

	public BigDecimal getAmountVatExcluded() {
		return amountVatExcluded;
	}

	public void setAmountVatExcluded(final BigDecimal amountVatExcluded) {
		this.amountVatExcluded = amountVatExcluded;
	}

	public CustomerInvoice withAmountVatExcluded(final BigDecimal amountVatExcluded) {
		this.amountVatExcluded = amountVatExcluded;
		return this;
	}

	public BigDecimal getVatEligibleAmount() {
		return vatEligibleAmount;
	}

	public void setVatEligibleAmount(final BigDecimal vatEligibleAmount) {
		this.vatEligibleAmount = vatEligibleAmount;
	}

	public CustomerInvoice withVatEligibleAmount(final BigDecimal vatEligibleAmount) {
		this.vatEligibleAmount = vatEligibleAmount;
		return this;
	}

	public BigDecimal getRounding() {
		return rounding;
	}

	public void setRounding(final BigDecimal rounding) {
		this.rounding = rounding;
	}

	public CustomerInvoice withRounding(final BigDecimal rounding) {
		this.rounding = rounding;
		return this;
	}

	public String getOrganizationGroup() {
		return organizationGroup;
	}

	public void setOrganizationGroup(final String organizationGroup) {
		this.organizationGroup = organizationGroup;
	}

	public CustomerInvoice withOrganizationGroup(final String organizationGroup) {
		this.organizationGroup = organizationGroup;
		return this;
	}

	public String getOrganizationNumber() {
		return organizationNumber;
	}

	public void setOrganizationNumber(final String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	public CustomerInvoice withOrganizationNumber(final String organizationNumber) {
		this.organizationNumber = organizationNumber;
		return this;
	}

	public String getAdministration() {
		return administration;
	}

	public void setAdministration(final String administration) {
		this.administration = administration;
	}

	public CustomerInvoice withAdministration(final String administration) {
		this.administration = administration;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(final String street) {
		this.street = street;
	}

	public CustomerInvoice withStreet(final String street) {
		this.street = street;
		return this;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(final String postCode) {
		this.postCode = postCode;
	}

	public CustomerInvoice withPostCode(final String postCode) {
		this.postCode = postCode;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public CustomerInvoice withCity(final String city) {
		this.city = city;
		return this;
	}

	public String getCareOf() {
		return careOf;
	}

	public void setCareOf(final String careOf) {
		this.careOf = careOf;
	}

	public CustomerInvoice withCareOf(final String careOf) {
		this.careOf = careOf;
		return this;
	}

	public String getInvoiceReference() {
		return invoiceReference;
	}

	public void setInvoiceReference(final String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}

	public CustomerInvoice withInvoiceReference(final String invoiceReference) {
		this.invoiceReference = invoiceReference;
		return this;
	}

	public Boolean getPdfAvailable() {
		return pdfAvailable;
	}

	public void setPdfAvailable(final Boolean pdfAvailable) {
		this.pdfAvailable = pdfAvailable;
	}

	public CustomerInvoice withPdfAvailable(final Boolean pdfAvailable) {
		this.pdfAvailable = pdfAvailable;
		return this;
	}

	public List<InvoiceDetail> getDetails() {
		return details;
	}

	public void setDetails(final List<InvoiceDetail> details) {
		this.details = details;
	}

	public CustomerInvoice withDetails(final List<InvoiceDetail> details) {
		this.details = details;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final CustomerInvoice that = (CustomerInvoice) o;
		return Objects.equals(customerNumber, that.customerNumber) && customerType == that.customerType
			&& Objects.equals(facilityId, that.facilityId) && Objects.equals(invoiceNumber, that.invoiceNumber)
			&& Objects.equals(invoiceId, that.invoiceId) && Objects.equals(jointInvoiceId, that.jointInvoiceId)
			&& Objects.equals(invoiceDate, that.invoiceDate) && Objects.equals(invoiceName, that.invoiceName)
			&& Objects.equals(invoiceType, that.invoiceType) && Objects.equals(invoiceDescription, that.invoiceDescription)
			&& Objects.equals(invoiceStatus, that.invoiceStatus) && Objects.equals(ocrNumber, that.ocrNumber)
			&& Objects.equals(dueDate, that.dueDate) && Objects.equals(periodFrom, that.periodFrom)
			&& Objects.equals(periodTo, that.periodTo) && Objects.equals(totalAmount, that.totalAmount)
			&& Objects.equals(amountVatIncluded, that.amountVatIncluded) && Objects.equals(amountVatExcluded, that.amountVatExcluded)
			&& Objects.equals(vatEligibleAmount, that.vatEligibleAmount) && Objects.equals(rounding, that.rounding)
			&& Objects.equals(organizationGroup, that.organizationGroup) && Objects.equals(organizationNumber, that.organizationNumber)
			&& Objects.equals(administration, that.administration) && Objects.equals(street, that.street)
			&& Objects.equals(postCode, that.postCode) && Objects.equals(city, that.city)
			&& Objects.equals(careOf, that.careOf) && Objects.equals(invoiceReference, that.invoiceReference)
			&& Objects.equals(pdfAvailable, that.pdfAvailable) && Objects.equals(details, that.details);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerNumber, customerType, facilityId, invoiceNumber, invoiceId, jointInvoiceId,
			invoiceDate, invoiceName, invoiceType, invoiceDescription, invoiceStatus, ocrNumber, dueDate,
			periodFrom, periodTo, totalAmount, amountVatIncluded, amountVatExcluded, vatEligibleAmount, rounding,
			organizationGroup, organizationNumber, administration, street, postCode, city, careOf,
			invoiceReference, pdfAvailable, details);
	}

	@Override
	public String toString() {
		return "CustomerInvoice{" +
			"customerNumber='" + customerNumber + '\'' +
			", customerType=" + customerType +
			", facilityId='" + facilityId + '\'' +
			", invoiceNumber=" + invoiceNumber +
			", invoiceId=" + invoiceId +
			", jointInvoiceId=" + jointInvoiceId +
			", invoiceDate=" + invoiceDate +
			", invoiceName='" + invoiceName + '\'' +
			", invoiceType='" + invoiceType + '\'' +
			", invoiceDescription='" + invoiceDescription + '\'' +
			", invoiceStatus='" + invoiceStatus + '\'' +
			", ocrNumber=" + ocrNumber +
			", dueDate=" + dueDate +
			", periodFrom=" + periodFrom +
			", periodTo=" + periodTo +
			", totalAmount=" + totalAmount +
			", amountVatIncluded=" + amountVatIncluded +
			", amountVatExcluded=" + amountVatExcluded +
			", vatEligibleAmount=" + vatEligibleAmount +
			", rounding=" + rounding +
			", organizationGroup='" + organizationGroup + '\'' +
			", organizationNumber='" + organizationNumber + '\'' +
			", administration='" + administration + '\'' +
			", street='" + street + '\'' +
			", postCode='" + postCode + '\'' +
			", city='" + city + '\'' +
			", careOf='" + careOf + '\'' +
			", invoiceReference='" + invoiceReference + '\'' +
			", pdfAvailable=" + pdfAvailable +
			", details=" + details +
			'}';
	}
}
