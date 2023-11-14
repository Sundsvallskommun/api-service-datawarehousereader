package se.sundsvall.datawarehousereader.api.model.customer;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import se.sundsvall.datawarehousereader.api.validation.ValidCustomerDetailsParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailsEntity;
import se.sundsvall.dept44.common.validators.annotation.ValidOrganizationNumber;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.dept44.models.api.paging.AbstractParameterPagingAndSortingBase;
import se.sundsvall.dept44.models.api.paging.validation.ValidSortByProperty;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer details request parameters model")
@ValidSortByProperty(CustomerDetailsEntity.class)
@ValidCustomerDetailsParameters
public class CustomerDetailsParameters extends AbstractParameterPagingAndSortingBase {

	@ArraySchema(schema = @Schema(description = "PartyId (e.g. a personId or an organizationId)", example = "81471222-5798-11e9-ae24-57fa13b361e1"))
	private List<@ValidUuid String> partyId;

	@ValidOrganizationNumber
	@Schema(description = "Organization id for customer engagements", example = "5565027225", requiredMode = REQUIRED)
	private String customerEngagementOrgId;

	@Schema(description = "Date and time for when to search for changes from. Format is yyyy-MM-dd'T'HH:mm:ss.SSSXXX. If omitted changes within the last year will be returned.", example = "2000-10-31T01:30:00.000-05:00")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private OffsetDateTime fromDateTime;

	@Schema(description = "Date and time for when to search for change to. Format is yyyy-MM-dd'T'HH:mm:ss.SSSXXX,", example = "2000-10-31T01:30:00.000-05:00")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private OffsetDateTime toDateTime;

	public static CustomerDetailsParameters create() {
		return new CustomerDetailsParameters();
	}

	public CustomerDetailsParameters withPartyId(List<String> partyId) {
		this.partyId = partyId;
		return this;
	}

	public List<String> getPartyId() {
		return partyId;
	}

	public void setPartyId(List<String> partyId) {
		this.partyId = partyId;
	}

	public CustomerDetailsParameters withCustomerEngagementOrgId(final String customerEngagementOrgId) {
		this.customerEngagementOrgId = customerEngagementOrgId;
		return this;
	}

	public String getCustomerEngagementOrgId() {
		return customerEngagementOrgId;
	}

	public void setCustomerEngagementOrgId(final String customerEngagementOrgId) {
		this.customerEngagementOrgId = customerEngagementOrgId;
	}

	public CustomerDetailsParameters withFromDateTime(OffsetDateTime fromDateTime) {
		this.fromDateTime = fromDateTime;
		return this;
	}

	public OffsetDateTime getFromDateTime() {
		return fromDateTime;
	}

	public void setFromDateTime(final OffsetDateTime fromDateTime) {
		this.fromDateTime = fromDateTime;
	}

	public CustomerDetailsParameters withToDateTime(OffsetDateTime toDateTime) {
		this.toDateTime = toDateTime;
		return this;
	}

	public OffsetDateTime getToDateTime() {
		return toDateTime;
	}

	public void setToDateTime(final OffsetDateTime toDateTime) {
		this.toDateTime = toDateTime;
	}

	@Override
	public String toString() {
		return "CustomerDetailsParameters{" +
			"partyId=" + partyId +
			", customerEngagementOrgId=" + customerEngagementOrgId +
			", fromDateTime=" + fromDateTime +
			", toDateTime=" + toDateTime +
			", page=" + page +
			", limit=" + limit +
			", sortBy=" + sortBy +
			", sortDirection=" + sortDirection +
			'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (getClass() != o.getClass())) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		final CustomerDetailsParameters that = (CustomerDetailsParameters) o;
		return Objects.equals(partyId, that.partyId) &&
			Objects.equals(customerEngagementOrgId, that.customerEngagementOrgId) &&
			Objects.equals(fromDateTime, that.fromDateTime) &&
			Objects.equals(toDateTime, that.toDateTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), partyId, customerEngagementOrgId, fromDateTime, toDateTime);
	}
}
