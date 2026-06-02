package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.time.LocalDate;

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

	public CustomerInvoiceQuery withPage(final Integer page) {
		this.page = page;
		return this;
	}

	public Integer getLimit() {
		return limit;
	}

	public CustomerInvoiceQuery withLimit(final Integer limit) {
		this.limit = limit;
		return this;
	}

	public String getCustomerIds() {
		return customerIds;
	}

	public CustomerInvoiceQuery withCustomerIds(final String customerIds) {
		this.customerIds = customerIds;
		return this;
	}

	public String getOrganizationIds() {
		return organizationIds;
	}

	public CustomerInvoiceQuery withOrganizationIds(final String organizationIds) {
		this.organizationIds = organizationIds;
		return this;
	}

	public String getFacilityIds() {
		return facilityIds;
	}

	public CustomerInvoiceQuery withFacilityIds(final String facilityIds) {
		this.facilityIds = facilityIds;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public CustomerInvoiceQuery withStatus(final String status) {
		this.status = status;
		return this;
	}

	public LocalDate getPeriodFrom() {
		return periodFrom;
	}

	public CustomerInvoiceQuery withPeriodFrom(final LocalDate periodFrom) {
		this.periodFrom = periodFrom;
		return this;
	}

	public LocalDate getPeriodTo() {
		return periodTo;
	}

	public CustomerInvoiceQuery withPeriodTo(final LocalDate periodTo) {
		this.periodTo = periodTo;
		return this;
	}

	public String getSortBy() {
		return sortBy;
	}

	public CustomerInvoiceQuery withSortBy(final String sortBy) {
		this.sortBy = sortBy;
		return this;
	}
}
