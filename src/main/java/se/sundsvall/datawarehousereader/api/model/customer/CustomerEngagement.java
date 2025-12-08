package se.sundsvall.datawarehousereader.api.model.customer;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import se.sundsvall.datawarehousereader.api.model.CustomerType;

@Schema(description = "Customer engagement model")
public class CustomerEngagement {

	@Schema(description = "PartyId (e.g. a personId or an organizationId)", examples = "81471222-5798-11e9-ae24-57fa13b361e1", accessMode = READ_ONLY)
	private String partyId;

	@Schema(description = "Customer organization number, for internal use only", examples = "5534567890", accessMode = READ_ONLY, hidden = true)
	private String customerOrgNumber;

	@Schema(implementation = CustomerType.class, accessMode = READ_ONLY)
	private CustomerType customerType;

	@Schema(description = "Customer number", examples = "10007", accessMode = READ_ONLY)
	private String customerNumber;

	@Schema(description = "Organization number for counterpart of engagement", examples = "5565027223", accessMode = READ_ONLY)
	private String organizationNumber;

	@Schema(description = "Organization name for counterpart of engagement", examples = "Sundsvall Elnät", accessMode = READ_ONLY)
	private String organizationName;

	@Schema(description = "Indicates customer status, if not active then the moveInDate holds information on when the customer will be activated", examples = "true", accessMode = READ_ONLY)
	private boolean active;

	@Schema(description = "The prospective customer's move-in date", accessMode = READ_ONLY)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDate moveInDate;

	public static CustomerEngagement create() {
		return new CustomerEngagement();
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public CustomerEngagement withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getCustomerOrgNumber() {
		return customerOrgNumber;
	}

	public void setCustomerOrgNumber(String customerOrgNumber) {
		this.customerOrgNumber = customerOrgNumber;
	}

	public CustomerEngagement withCustomerOrgNumber(String customerOrgNumber) {
		this.customerOrgNumber = customerOrgNumber;
		return this;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public CustomerEngagement withCustomerType(CustomerType customerType) {
		this.customerType = customerType;
		return this;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public CustomerEngagement withCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

	public String getOrganizationNumber() {
		return organizationNumber;
	}

	public void setOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	public CustomerEngagement withOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
		return this;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public CustomerEngagement withOrganizationName(String organizationName) {
		this.organizationName = organizationName;
		return this;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public CustomerEngagement withActive(boolean active) {
		this.active = active;
		return this;
	}

	public LocalDate getMoveInDate() {
		return moveInDate;
	}

	public void setMoveInDate(LocalDate moveInDate) {
		this.moveInDate = moveInDate;
	}

	public CustomerEngagement withMoveInDate(LocalDate moveInDate) {
		this.moveInDate = moveInDate;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(active, customerNumber, customerOrgNumber, customerType, moveInDate, organizationName, organizationNumber, partyId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof final CustomerEngagement other)) {
			return false;
		}
		return active == other.active && Objects.equals(customerNumber, other.customerNumber) && Objects.equals(customerOrgNumber, other.customerOrgNumber) && customerType == other.customerType && Objects.equals(moveInDate, other.moveInDate)
			&& Objects.equals(organizationName, other.organizationName) && Objects.equals(organizationNumber, other.organizationNumber) && Objects.equals(partyId, other.partyId);
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		builder.append("CustomerEngagement [partyId=").append(partyId).append(", customerOrgNumber=").append(customerOrgNumber).append(", customerType=").append(customerType).append(", customerNumber=").append(customerNumber).append(
			", organizationNumber=").append(organizationNumber).append(", organizationName=").append(organizationName).append(", active=").append(active).append(", moveInDate=").append(moveInDate).append("]");
		return builder.toString();
	}
}
