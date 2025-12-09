package se.sundsvall.datawarehousereader.api.model.invoice;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;
import se.sundsvall.dept44.models.api.paging.AbstractParameterPagingAndSortingBase;
import se.sundsvall.dept44.models.api.paging.validation.ValidSortByProperty;

@Schema(description = "Invoice request parameters model")
@ValidSortByProperty(InvoiceEntity.class)
@ParameterObject
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

	public InvoiceParameters(int limit) {
		super(limit);
	}

	public InvoiceParameters() {
		super(100);
	}

	public static InvoiceParameters createWithLimit(int limit) {
		return new InvoiceParameters(limit);
	}

	public static InvoiceParameters create() {
		return new InvoiceParameters();
	}

	public List<String> getCustomerNumber() {
		return customerNumber;
	}

	public InvoiceParameters withCustomerNumber(List<String> customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

	public void setCustomerNumber(List<String> customerNumber) {
		this.customerNumber = customerNumber;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public InvoiceParameters withCustomerType(CustomerType customerType) {
		this.customerType = customerType;
		return this;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public List<String> getFacilityIds() {
		return facilityIds;
	}

	public InvoiceParameters withFacilityIds(List<String> facilityId) {
		this.facilityIds = facilityId;
		return this;
	}

	public void setFacilityIds(List<String> facilityIds) {
		this.facilityIds = facilityIds;
	}

	public Long getInvoiceNumber() {
		return invoiceNumber;
	}

	public InvoiceParameters withInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
		return this;
	}

	public void setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public LocalDate getInvoiceDateFrom() {
		return invoiceDateFrom;
	}

	public InvoiceParameters withInvoiceDateFrom(LocalDate invoiceDateFrom) {
		this.invoiceDateFrom = invoiceDateFrom;
		return this;
	}

	public void setInvoiceDateFrom(LocalDate invoiceDateFrom) {
		this.invoiceDateFrom = invoiceDateFrom;
	}

	public LocalDate getInvoiceDateTo() {
		return invoiceDateTo;
	}

	public InvoiceParameters withInvoiceDateTo(LocalDate invoiceDateTo) {
		this.invoiceDateTo = invoiceDateTo;
		return this;
	}

	public void setInvoiceDateTo(LocalDate invoiceDateTo) {
		this.invoiceDateTo = invoiceDateTo;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public InvoiceParameters withInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
		return this;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public InvoiceParameters withInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
		return this;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public InvoiceParameters withInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
		return this;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Long getOcrNumber() {
		return ocrNumber;
	}

	public InvoiceParameters withOcrNumber(Long ocrNumber) {
		this.ocrNumber = ocrNumber;
		return this;
	}

	public void setOcrNumber(Long ocrNumber) {
		this.ocrNumber = ocrNumber;
	}

	public LocalDate getDueDateFrom() {
		return dueDateFrom;
	}

	public InvoiceParameters withDueDateFrom(LocalDate dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
		return this;
	}

	public void setDueDateFrom(LocalDate dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
	}

	public LocalDate getDueDateTo() {
		return dueDateTo;
	}

	public InvoiceParameters withDueDateTo(LocalDate dueDateTo) {
		this.dueDateTo = dueDateTo;
		return this;
	}

	public void setDueDateTo(LocalDate dueDateTo) {
		this.dueDateTo = dueDateTo;
	}

	public String getOrganizationGroup() {
		return organizationGroup;
	}

	public InvoiceParameters withOrganizationGroup(String organizationGroup) {
		this.organizationGroup = organizationGroup;
		return this;
	}

	public void setOrganizationGroup(String organizationGroup) {
		this.organizationGroup = organizationGroup;
	}

	public String getOrganizationNumber() {
		return organizationNumber;
	}

	public InvoiceParameters withOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
		return this;
	}

	public void setOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	public String getAdministration() {
		return administration;
	}

	public InvoiceParameters withAdministration(String administration) {
		this.administration = administration;
		return this;
	}

	public void setAdministration(String administration) {
		this.administration = administration;
	}

	public InvoiceParameters withSortBy(List<String> sortBy) {
		setSortBy(sortBy);
		return this;
	}

	public InvoiceParameters withSortDirection(Sort.Direction sortDirection) {
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
	public boolean equals(Object obj) {
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
