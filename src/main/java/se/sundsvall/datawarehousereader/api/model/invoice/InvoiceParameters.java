package se.sundsvall.datawarehousereader.api.model.invoice;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;
import se.sundsvall.dept44.models.api.paging.AbstractParameterPagingAndSortingBase;
import se.sundsvall.dept44.models.api.paging.validation.ValidSortByProperty;

@Schema(description = "Invoice request parameters model")
@ValidSortByProperty(InvoiceEntity.class)
public class InvoiceParameters extends AbstractParameterPagingAndSortingBase {

	@ArraySchema(schema = @Schema(description = "Customer numbers", examples = "39195"))
	private List<String> customerNumber;

	@Schema(implementation = CustomerType.class)
	private CustomerType customerType;

	@ArraySchema(schema = @Schema(description = "Facility ids", examples = "735999109151401011"))
	private List<String> facilityIds;

	@Schema(description = "Invoice number", examples = "767915994")
	private Long invoiceNumber;

	@Schema(description = "Earliest invoice date. Format is YYYY-MM-DD.", examples = "2022-01-01")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate invoiceDateFrom;

	@Schema(description = "Latest invoice date. Format is YYYY-MM-DD.", examples = "2022-01-31")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate invoiceDateTo;

	@Schema(description = "Invoice name", examples = "765801493.pdf")
	private String invoiceName;

	@Schema(description = "Invoice type", examples = "Faktura")
	private String invoiceType;

	@Schema(description = "Invoice status", examples = "Skickad")
	private String invoiceStatus;

	@Schema(description = "OCR number", examples = "767915994")
	private Long ocrNumber;

	@Schema(description = "Earliest due date. Format is YYYY-MM-DD.", examples = "2022-01-01")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dueDateFrom;

	@Schema(description = "Latest due date. Format is YYYY-MM-DD.", examples = "2022-01-31")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dueDateTo;

	@Schema(description = "Organization group", examples = "stadsbacken")
	private String organizationGroup;

	@Schema(description = "Organization number of invoice issuer", examples = "5565027223")
	private String organizationNumber;

	@Schema(description = "Administration", examples = "Sundsvall Elnät")
	private String administration;

	public InvoiceParameters(final int limit) {
		super(limit);
	}

	public InvoiceParameters() {
		super(100);
	}

	public static InvoiceParameters createWithLimit(final int limit) {
		return new InvoiceParameters(limit);
	}

	public static InvoiceParameters create() {
		return new InvoiceParameters();
	}

	public List<String> getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(final List<String> customerNumber) {
		this.customerNumber = customerNumber;
	}

	public InvoiceParameters withCustomerNumber(final List<String> customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(final CustomerType customerType) {
		this.customerType = customerType;
	}

	public InvoiceParameters withCustomerType(final CustomerType customerType) {
		this.customerType = customerType;
		return this;
	}

	public List<String> getFacilityIds() {
		return facilityIds;
	}

	public void setFacilityIds(final List<String> facilityIds) {
		this.facilityIds = facilityIds;
	}

	public InvoiceParameters withFacilityIds(final List<String> facilityId) {
		this.facilityIds = facilityId;
		return this;
	}

	public Long getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(final Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public InvoiceParameters withInvoiceNumber(final Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
		return this;
	}

	public LocalDate getInvoiceDateFrom() {
		return invoiceDateFrom;
	}

	public void setInvoiceDateFrom(final LocalDate invoiceDateFrom) {
		this.invoiceDateFrom = invoiceDateFrom;
	}

	public InvoiceParameters withInvoiceDateFrom(final LocalDate invoiceDateFrom) {
		this.invoiceDateFrom = invoiceDateFrom;
		return this;
	}

	public LocalDate getInvoiceDateTo() {
		return invoiceDateTo;
	}

	public void setInvoiceDateTo(final LocalDate invoiceDateTo) {
		this.invoiceDateTo = invoiceDateTo;
	}

	public InvoiceParameters withInvoiceDateTo(final LocalDate invoiceDateTo) {
		this.invoiceDateTo = invoiceDateTo;
		return this;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(final String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public InvoiceParameters withInvoiceName(final String invoiceName) {
		this.invoiceName = invoiceName;
		return this;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(final String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public InvoiceParameters withInvoiceType(final String invoiceType) {
		this.invoiceType = invoiceType;
		return this;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(final String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public InvoiceParameters withInvoiceStatus(final String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
		return this;
	}

	public Long getOcrNumber() {
		return ocrNumber;
	}

	public void setOcrNumber(final Long ocrNumber) {
		this.ocrNumber = ocrNumber;
	}

	public InvoiceParameters withOcrNumber(final Long ocrNumber) {
		this.ocrNumber = ocrNumber;
		return this;
	}

	public LocalDate getDueDateFrom() {
		return dueDateFrom;
	}

	public void setDueDateFrom(final LocalDate dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
	}

	public InvoiceParameters withDueDateFrom(final LocalDate dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
		return this;
	}

	public LocalDate getDueDateTo() {
		return dueDateTo;
	}

	public void setDueDateTo(final LocalDate dueDateTo) {
		this.dueDateTo = dueDateTo;
	}

	public InvoiceParameters withDueDateTo(final LocalDate dueDateTo) {
		this.dueDateTo = dueDateTo;
		return this;
	}

	public String getOrganizationGroup() {
		return organizationGroup;
	}

	public void setOrganizationGroup(final String organizationGroup) {
		this.organizationGroup = organizationGroup;
	}

	public InvoiceParameters withOrganizationGroup(final String organizationGroup) {
		this.organizationGroup = organizationGroup;
		return this;
	}

	public String getOrganizationNumber() {
		return organizationNumber;
	}

	public void setOrganizationNumber(final String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	public InvoiceParameters withOrganizationNumber(final String organizationNumber) {
		this.organizationNumber = organizationNumber;
		return this;
	}

	public String getAdministration() {
		return administration;
	}

	public void setAdministration(final String administration) {
		this.administration = administration;
	}

	public InvoiceParameters withAdministration(final String administration) {
		this.administration = administration;
		return this;
	}

	public InvoiceParameters withSortBy(final List<String> sortBy) {
		setSortBy(sortBy);
		return this;
	}

	public InvoiceParameters withSortDirection(final Sort.Direction sortDirection) {
		setSortDirection(sortDirection);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + Objects.hash(administration, customerNumber, customerType, dueDateFrom, dueDateTo,
			facilityIds, invoiceDateFrom, invoiceDateTo, invoiceName, invoiceNumber, invoiceStatus, invoiceType,
			ocrNumber, organizationGroup, organizationNumber);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final InvoiceParameters other = (InvoiceParameters) obj;
		return Objects.equals(administration, other.administration)
			&& Objects.equals(customerNumber, other.customerNumber) && (customerType == other.customerType)
			&& Objects.equals(dueDateFrom, other.dueDateFrom) && Objects.equals(dueDateTo, other.dueDateTo)
			&& Objects.equals(facilityIds, other.facilityIds)
			&& Objects.equals(invoiceDateFrom, other.invoiceDateFrom)
			&& Objects.equals(invoiceDateTo, other.invoiceDateTo) && Objects.equals(invoiceName, other.invoiceName)
			&& Objects.equals(invoiceNumber, other.invoiceNumber)
			&& Objects.equals(invoiceStatus, other.invoiceStatus) && Objects.equals(invoiceType, other.invoiceType)
			&& Objects.equals(ocrNumber, other.ocrNumber)
			&& Objects.equals(organizationGroup, other.organizationGroup)
			&& Objects.equals(organizationNumber, other.organizationNumber);
	}

	@Override
	public String toString() {
		return "InvoiceParameters{" +
			"customerNumber=" + customerNumber +
			", customerType=" + customerType +
			", facilityIds=" + facilityIds +
			", invoiceNumber=" + invoiceNumber +
			", invoiceDateFrom=" + invoiceDateFrom +
			", invoiceDateTo=" + invoiceDateTo +
			", invoiceName='" + invoiceName + '\'' +
			", invoiceType='" + invoiceType + '\'' +
			", invoiceStatus='" + invoiceStatus + '\'' +
			", ocrNumber=" + ocrNumber +
			", dueDateFrom=" + dueDateFrom +
			", dueDateTo=" + dueDateTo +
			", organizationGroup='" + organizationGroup + '\'' +
			", organizationNumber='" + organizationNumber + '\'' +
			", administration='" + administration + '\'' +
			'}';
	}
}
