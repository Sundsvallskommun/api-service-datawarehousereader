package se.sundsvall.datawarehousereader.api.model.customer;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import se.sundsvall.datawarehousereader.api.model.CustomerType;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer details model")
public class CustomerDetails {

	@Schema(description = "Customer organization number, for internal use only", example = "5534567890", accessMode = READ_ONLY, hidden = true)
	private String customerOrgNumber;

	@Schema(description = "PartyId (e.g. a personId or an organizationId)", example = "81471222-5798-11e9-ae24-57fa13b361e1", accessMode = READ_ONLY)
	private String partyId;

	@Schema(description = "Customer number", example = "39195", accessMode = READ_ONLY)
	private String customerNumber;

	@Schema(description = "Customer name", example = "Sven Svensson", accessMode = READ_ONLY)
	private String customerName;

	@Schema(description = "Street", example = "Storgatan 44", accessMode = READ_ONLY)
	private String street;

	@Schema(description = "Postal code", example = "85230", accessMode = READ_ONLY)
	private String postalCode;

	@Schema(description = "City", example = "Sundsvall", accessMode = READ_ONLY)
	private String city;

	@Schema(description = "Care of address", example = "Agatha Malm", accessMode = READ_ONLY)
	private String careOf;

	@ArraySchema(schema = @Schema(description = "List of phoneNumbers", example = "076-1234567", accessMode = READ_ONLY))
	private List<String> phoneNumbers;

	@ArraySchema(schema = @Schema(description = "List of emailAddresses", example = "test@test.se", accessMode = READ_ONLY))
	private List<String> emails;

	@Schema(description = "Customer category ID", example = "1", accessMode = READ_ONLY)
	private int customerCategoryID;

	@Schema(description = "Customer category description", example = "Privat", accessMode = READ_ONLY)
	private String customerCategoryDescription;

	@Schema(description = "Indicates if customer details have changed since the search date", example = "PRIVATE", accessMode = READ_ONLY)
	private boolean customerChangedFlg;

	@Schema(description = "Indicates if placement and/or equipment details have changed since the search date", example = "true", accessMode = READ_ONLY)
	private boolean installedChangedFlg;

	@JsonIgnore
	private CustomerType customerType;

	public static CustomerDetails create() {
		return new CustomerDetails();
	}

	public CustomerDetails withCustomerNumber(final String customerNumber) {
		this.customerNumber = customerNumber;
		return this;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(final String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public CustomerDetails withCustomerName(final String customerName) {
		this.customerName = customerName;
		return this;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(final String customerName) {
		this.customerName = customerName;
	}

	public CustomerDetails withStreet(final String street) {
		this.street = street;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(final String street) {
		this.street = street;
	}

	public CustomerDetails withPostalCode(final String postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(final String postalCode) {
		this.postalCode = postalCode;
	}

	public CustomerDetails withCity(final String city) {
		this.city = city;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public CustomerDetails withCareOf(final String careOf) {
		this.careOf = careOf;
		return this;
	}

	public String getCareOf() {
		return careOf;
	}

	public void setCareOf(final String careOf) {
		this.careOf = careOf;
	}

	public CustomerDetails withPhoneNumbers(final List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
		return this;
	}

	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(final List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public CustomerDetails withEmailAddresses(final List<String> emails) {
		this.emails = emails;
		return this;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(final List<String> emails) {
		this.emails = emails;
	}

	public CustomerDetails withCustomerCategoryID(final int customerCategoryID) {
		this.customerCategoryID = customerCategoryID;
		return this;
	}

	public int getCustomerCategoryID() {
		return customerCategoryID;
	}

	public void setCustomerCategoryID(final int customerCategoryID) {
		this.customerCategoryID = customerCategoryID;
	}

	public CustomerDetails withCustomerCategoryDescription(final String customerCategoryDescription) {
		this.customerCategoryDescription = customerCategoryDescription;
		return this;
	}

	public String getCustomerCategoryDescription() {
		return customerCategoryDescription;
	}

	public void setCustomerCategoryDescription(final String customerCategoryDescription) {
		this.customerCategoryDescription = customerCategoryDescription;
	}

	public CustomerDetails withCustomerChangedFlg(final boolean customerChangedFlg) {
		this.customerChangedFlg = customerChangedFlg;
		return this;
	}

	public boolean isCustomerChangedFlg() {
		return customerChangedFlg;
	}

	public void setCustomerChangedFlg(final boolean customerChangedFlg) {
		this.customerChangedFlg = customerChangedFlg;
	}

	public CustomerDetails withInstalledChangedFlg(final boolean installedChangedFlg) {
		this.installedChangedFlg = installedChangedFlg;
		return this;
	}

	public boolean isInstalledChangedFlg() {
		return installedChangedFlg;
	}

	public void setInstalledChangedFlg(final boolean installedChangedFlg) {
		this.installedChangedFlg = installedChangedFlg;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public CustomerDetails withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public CustomerDetails withCustomerOrgNumber(String customerOrgNumber) {
		this.customerOrgNumber = customerOrgNumber;
		return this;
	}

	public String getCustomerOrgNumber() {
		return customerOrgNumber;
	}

	public void setCustomerOrgNumber(String customerOrgNumber) {
		this.customerOrgNumber = customerOrgNumber;
	}

	@Override
	public String toString() {
		return "CustomerDetails{" +
			"customerOrgNumber='" + customerOrgNumber + '\'' +
			", partyId='" + partyId + '\'' +
			", customerNumber='" + customerNumber + '\'' +
			", customerName='" + customerName + '\'' +
			", street='" + street + '\'' +
			", postalCode='" + postalCode + '\'' +
			", city='" + city + '\'' +
			", careOf='" + careOf + '\'' +
			", phoneNumbers=" + phoneNumbers +
			", emails=" + emails +
			", customerCategoryID=" + customerCategoryID +
			", customerCategoryDescription='" + customerCategoryDescription + '\'' +
			", customerChangedFlg=" + customerChangedFlg +
			", installedChangedFlg=" + installedChangedFlg +
			", customerType=" + customerType +
			'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CustomerDetails that = (CustomerDetails) o;
		return customerCategoryID == that.customerCategoryID && customerChangedFlg == that.customerChangedFlg && installedChangedFlg == that.installedChangedFlg && Objects.equals(customerOrgNumber, that.customerOrgNumber) && Objects.equals(partyId, that.partyId) && Objects.equals(customerNumber, that.customerNumber) && Objects.equals(customerName, that.customerName) && Objects.equals(street, that.street) && Objects.equals(postalCode, that.postalCode) && Objects.equals(city, that.city) && Objects.equals(careOf, that.careOf) && Objects.equals(phoneNumbers, that.phoneNumbers) && Objects.equals(emails, that.emails) && Objects.equals(customerCategoryDescription, that.customerCategoryDescription) && customerType == that.customerType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerOrgNumber, partyId, customerNumber, customerName, street, postalCode, city, careOf, phoneNumbers, emails, customerCategoryID, customerCategoryDescription, customerChangedFlg, installedChangedFlg, customerType);
	}

	public CustomerDetails withCustomerType(final CustomerType customerType) {
		this.customerType = customerType;
		return this;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(final CustomerType customerType) {
		this.customerType = customerType;
	}
}
