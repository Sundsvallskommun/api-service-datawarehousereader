package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Parameter object for {@link InvoiceJdbcRepository#getInvoices(CustomerInvoiceQuery)}.
 *
 * <p>
 * List valued filters (customer numbers, organization ids and facility ids) are carried as comma separated strings,
 * matching what the underlying SQL function and the STRING_SPLIT based filtering expect.
 */
public class CustomerInvoiceQuery {

	private Integer page;
	private Integer limit;
	private String customerIds;
	private String organizationIds;
	private String facilityIds;
	private String status;
	private LocalDate periodFrom;
	private LocalDate periodTo;
	private String sortBy;

	public static CustomerInvoiceQuery create() {
		return new CustomerInvoiceQuery();
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(final Integer page) {
		this.page = page;
	}

	public CustomerInvoiceQuery withPage(final Integer page) {
		this.setPage(page);
		return this;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(final Integer limit) {
		this.limit = limit;
	}

	public CustomerInvoiceQuery withLimit(final Integer limit) {
		this.setLimit(limit);
		return this;
	}

	public String getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(final String customerIds) {
		this.customerIds = customerIds;
	}

	public CustomerInvoiceQuery withCustomerIds(final String customerIds) {
		this.setCustomerIds(customerIds);
		return this;
	}

	public String getOrganizationIds() {
		return organizationIds;
	}

	public void setOrganizationIds(final String organizationIds) {
		this.organizationIds = organizationIds;
	}

	public CustomerInvoiceQuery withOrganizationIds(final String organizationIds) {
		this.setOrganizationIds(organizationIds);
		return this;
	}

	public String getFacilityIds() {
		return facilityIds;
	}

	public void setFacilityIds(final String facilityIds) {
		this.facilityIds = facilityIds;
	}

	public CustomerInvoiceQuery withFacilityIds(final String facilityIds) {
		this.setFacilityIds(facilityIds);
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public CustomerInvoiceQuery withStatus(final String status) {
		this.setStatus(status);
		return this;
	}

	public LocalDate getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(final LocalDate periodFrom) {
		this.periodFrom = periodFrom;
	}

	public CustomerInvoiceQuery withPeriodFrom(final LocalDate periodFrom) {
		this.setPeriodFrom(periodFrom);
		return this;
	}

	public LocalDate getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(final LocalDate periodTo) {
		this.periodTo = periodTo;
	}

	public CustomerInvoiceQuery withPeriodTo(final LocalDate periodTo) {
		this.setPeriodTo(periodTo);
		return this;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(final String sortBy) {
		this.sortBy = sortBy;
	}

	public CustomerInvoiceQuery withSortBy(final String sortBy) {
		this.setSortBy(sortBy);
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(page, limit, customerIds, organizationIds, facilityIds, status, periodFrom, periodTo, sortBy);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof final CustomerInvoiceQuery other)) {
			return false;
		}
		return Objects.equals(page, other.page)
			&& Objects.equals(limit, other.limit)
			&& Objects.equals(customerIds, other.customerIds)
			&& Objects.equals(organizationIds, other.organizationIds)
			&& Objects.equals(facilityIds, other.facilityIds)
			&& Objects.equals(status, other.status)
			&& Objects.equals(periodFrom, other.periodFrom)
			&& Objects.equals(periodTo, other.periodTo)
			&& Objects.equals(sortBy, other.sortBy);
	}

	@Override
	public String toString() {
		return "CustomerInvoiceQuery [page=" + page
			+ ", limit=" + limit
			+ ", customerIds=" + customerIds
			+ ", organizationIds=" + organizationIds
			+ ", facilityIds=" + facilityIds
			+ ", status=" + status
			+ ", periodFrom=" + periodFrom
			+ ", periodTo=" + periodTo
			+ ", sortBy=" + sortBy + "]";
	}
}
