package se.sundsvall.datawarehousereader.api.model.invoice;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import se.sundsvall.dept44.models.api.paging.AbstractParameterPagingBase;

@Schema(description = "Customer invoice request parameters model")
@ParameterObject
public class CustomerInvoiceParameters extends AbstractParameterPagingBase {

	@ArraySchema(schema = @Schema(description = "Organization id of invoice issuer", examples = "5565027223"))
	private List<String> organizationIds;

	@Schema(description = "Earliest invoice period start. Format is YYYY-MM-DD.", examples = "2025-01-01")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate periodFrom;

	@Schema(description = "Latest invoice period end. Format is YYYY-MM-DD.", examples = "2025-12-31")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate periodTo;

	@Schema(description = "Column to sort by.", examples = {
		"periodFrom", "periodTo", "InvoiceDate", "DueDate", "InvoiceNumber", "TotalAmount"
	})
	private String sortBy;

	public static CustomerInvoiceParameters create() {
		return new CustomerInvoiceParameters();
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

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(final String sortBy) {
		this.sortBy = sortBy;
	}

	public CustomerInvoiceParameters withSortBy(final String sortBy) {
		this.sortBy = sortBy;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(organizationIds, periodFrom, periodTo, sortBy);
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
		return Objects.equals(organizationIds, other.organizationIds)
			&& Objects.equals(periodFrom, other.periodFrom)
			&& Objects.equals(periodTo, other.periodTo)
			&& Objects.equals(sortBy, other.sortBy);
	}

	@Override
	public String toString() {
		return "CustomerInvoiceParameters [organizationIds=" + organizationIds
			+ ", periodFrom=" + periodFrom
			+ ", periodTo=" + periodTo
			+ ", sortBy=" + sortBy
			+ ", page=" + page
			+ ", limit=" + limit + "]";
	}
}
