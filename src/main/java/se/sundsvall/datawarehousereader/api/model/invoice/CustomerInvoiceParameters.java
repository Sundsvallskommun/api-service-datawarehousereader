package se.sundsvall.datawarehousereader.api.model.invoice;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import se.sundsvall.dept44.models.api.paging.AbstractParameterPagingAndSortingBase;

@Schema(description = "Customer invoice request parameters model")
@ParameterObject
public class CustomerInvoiceParameters extends AbstractParameterPagingAndSortingBase {

	@ArraySchema(schema = @Schema(description = "Customer numbers", examples = "123456"), minItems = 1)
	@NotEmpty
	private List<String> customerNumbers;

	@ArraySchema(schema = @Schema(description = "Organization id of invoice issuer", examples = "5565027223"))
	private List<String> organizationIds;

	@ArraySchema(schema = @Schema(description = "Facility ids to filter by. A row matches if any of its facility ids contains a requested value", examples = "123456789012345670"))
	private List<String> facilityIds;

	@Schema(description = "Invoice status", examples = "Betalad")
	private String status;

	@Schema(description = "Earliest invoice period start. Format is YYYY-MM-DD.", examples = "2025-01-01")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate periodFrom;

	@Schema(description = "Latest invoice period end. Format is YYYY-MM-DD.", examples = "2025-12-31")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate periodTo;

	public static CustomerInvoiceParameters create() {
		return new CustomerInvoiceParameters();
	}

	public List<String> getCustomerNumbers() {
		return customerNumbers;
	}

	public void setCustomerNumbers(final List<String> customerNumbers) {
		this.customerNumbers = customerNumbers;
	}

	public CustomerInvoiceParameters withCustomerNumbers(final List<String> customerNumbers) {
		this.customerNumbers = customerNumbers;
		return this;
	}

	public List<String> getOrganizationIds() {
		return organizationIds;
	}

	public void setOrganizationIds(final List<String> organizationIds) {
		this.organizationIds = organizationIds;
	}

	public CustomerInvoiceParameters withOrganizationIds(final List<String> organizationIds) {
		this.organizationIds = organizationIds;
		return this;
	}

	public List<String> getFacilityIds() {
		return facilityIds;
	}

	public void setFacilityIds(final List<String> facilityIds) {
		this.facilityIds = facilityIds;
	}

	public CustomerInvoiceParameters withFacilityIds(final List<String> facilityIds) {
		this.facilityIds = facilityIds;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public CustomerInvoiceParameters withStatus(final String status) {
		this.status = status;
		return this;
	}

	public LocalDate getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(final LocalDate periodFrom) {
		this.periodFrom = periodFrom;
	}

	public CustomerInvoiceParameters withPeriodFrom(final LocalDate periodFrom) {
		this.periodFrom = periodFrom;
		return this;
	}

	public LocalDate getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(final LocalDate periodTo) {
		this.periodTo = periodTo;
	}

	public CustomerInvoiceParameters withPeriodTo(final LocalDate periodTo) {
		this.periodTo = periodTo;
		return this;
	}

	@Override
	@ArraySchema(schema = @Schema(description = "Column to sort by", examples = {
		"periodFrom", "periodTo", "InvoiceDate", "DueDate", "InvoiceNumber", "TotalAmount"
	}))
	public List<String> getSortBy() {
		return super.getSortBy();
	}

	public CustomerInvoiceParameters withSortBy(final List<String> sortBy) {
		setSortBy(sortBy);
		return this;
	}

	public CustomerInvoiceParameters withSortDirection(final Sort.Direction sortDirection) {
		setSortDirection(sortDirection);
		return this;
	}

	public CustomerInvoiceParameters withPage(final int page) {
		setPage(page);
		return this;
	}

	public CustomerInvoiceParameters withLimit(final int limit) {
		setLimit(limit);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(customerNumbers, organizationIds, facilityIds, status, periodFrom, periodTo, sortBy, sortDirection, page, limit);
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
		if (!(obj instanceof CustomerInvoiceParameters other)) {
			return false;
		}
		return Objects.equals(customerNumbers, other.customerNumbers)
			&& Objects.equals(organizationIds, other.organizationIds)
			&& Objects.equals(facilityIds, other.facilityIds)
			&& Objects.equals(status, other.status)
			&& Objects.equals(periodFrom, other.periodFrom)
			&& Objects.equals(periodTo, other.periodTo)
			&& Objects.equals(sortBy, other.sortBy)
			&& Objects.equals(sortDirection, other.sortDirection)
			&& Objects.equals(page, other.page)
			&& Objects.equals(limit, other.limit);
	}

	@Override
	public String toString() {
		return "CustomerInvoiceParameters [customerNumbers=" + customerNumbers
			+ ", organizationIds=" + organizationIds
			+ ", facilityIds=" + facilityIds
			+ ", status=" + status
			+ ", periodFrom=" + periodFrom
			+ ", periodTo=" + periodTo
			+ ", sortBy=" + sortBy
			+ ", sortDirection=" + sortDirection
			+ ", page=" + page
			+ ", limit=" + limit + "]";
	}
}
