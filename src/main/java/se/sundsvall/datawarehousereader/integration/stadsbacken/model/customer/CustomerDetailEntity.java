package se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(schema = "kundinfo", name = "vCustomerDetail")
public class CustomerDetailEntity {

	@Id
	@Column(name = "Customerid", nullable = false, insertable = false, updatable = false)
	private Integer customerId;

	@Column(name = "uuid", columnDefinition = "uniqueidentifier", insertable = false, updatable = false)
	private String partyId;

	@Column(name = "CustomerOrgId", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String customerOrgId;

	@Column(name = "organizationid", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String organizationId;

	@Column(name = "organizationname", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String organizationName;

	@Column(name = "CustomerCategoryID", insertable = false, updatable = false)
	private Integer customerCategoryID;

	@Column(name = "CustomerCategoryDescription", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String customerCategoryDescription;

	@Column(name = "Name", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String name;

	@Column(name = "c/o", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String co;

	@Column(name = "Address", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String address;

	@Column(name = "Zipcode", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String zipcode;

	@Column(name = "City", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String city;

	@Column(name = "Phone1", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String phone1;

	@Column(name = "Phone2", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String phone2;

	@Column(name = "Phone3", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String phone3;

	@Column(name = "Email1", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String email1;

	@Column(name = "Email2", columnDefinition = "nvarchar(255)", insertable = false, updatable = false)
	private String email2;

	@Column(name = "CustomerChangedFlg", insertable = false, updatable = false)
	private boolean customerChangedFlg;

	@Column(name = "Installedchangedflg", insertable = false, updatable = false)
	private boolean installedChangedFlg;

	@Column(name = "Active", columnDefinition = "varchar(1)", nullable = false, insertable = false, updatable = false)
	private boolean active;

	@Column(name = "MoveInDate", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDateTime moveInDate;

	public static CustomerDetailEntity create() {
		return new CustomerDetailEntity();
	}

	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public void setCustomerOrgId(final String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public CustomerDetailEntity withCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
		return this;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public CustomerDetailEntity withOrganizationId(String organizationId) {
		this.organizationId = organizationId;
		return this;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public CustomerDetailEntity withOrganizationName(String organizationName) {
		this.organizationName = organizationName;
		return this;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(final Integer customerId) {
		this.customerId = customerId;
	}

	public CustomerDetailEntity withCustomerId(Integer customerId) {
		this.customerId = customerId;
		return this;
	}

	public Integer getCustomerCategoryID() {
		return customerCategoryID;
	}

	public void setCustomerCategoryID(final Integer customerCategoryID) {
		this.customerCategoryID = customerCategoryID;
	}

	public CustomerDetailEntity withCustomerCategoryID(Integer customerCategoryID) {
		this.customerCategoryID = customerCategoryID;
		return this;
	}

	public String getCustomerCategoryDescription() {
		return customerCategoryDescription;
	}

	public void setCustomerCategoryDescription(final String customerCategoryDescription) {
		this.customerCategoryDescription = customerCategoryDescription;
	}

	public CustomerDetailEntity withCustomerCategoryDescription(String customerCategoryDescription) {
		this.customerCategoryDescription = customerCategoryDescription;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public CustomerDetailEntity withName(String name) {
		this.name = name;
		return this;
	}

	public String getCo() {
		return co;
	}

	public void setCo(final String co) {
		this.co = co;
	}

	public CustomerDetailEntity withCo(String co) {
		this.co = co;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public CustomerDetailEntity withAddress(String address) {
		this.address = address;
		return this;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(final String zipcode) {
		this.zipcode = zipcode;
	}

	public CustomerDetailEntity withZipcode(String zipcode) {
		this.zipcode = zipcode;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public CustomerDetailEntity withCity(String city) {
		this.city = city;
		return this;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(final String phone1) {
		this.phone1 = phone1;
	}

	public CustomerDetailEntity withPhone1(String phone1) {
		this.phone1 = phone1;
		return this;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(final String phone2) {
		this.phone2 = phone2;
	}

	public CustomerDetailEntity withPhone2(String phone2) {
		this.phone2 = phone2;
		return this;
	}

	public String getPhone3() {
		return phone3;
	}

	public void setPhone3(final String phone3) {
		this.phone3 = phone3;
	}

	public CustomerDetailEntity withPhone3(String phone3) {
		this.phone3 = phone3;
		return this;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(final String email1) {
		this.email1 = email1;
	}

	public CustomerDetailEntity withEmail1(String email1) {
		this.email1 = email1;
		return this;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(final String email2) {
		this.email2 = email2;
	}

	public CustomerDetailEntity withEmail2(String email2) {
		this.email2 = email2;
		return this;
	}

	public boolean isCustomerChangedFlg() {
		return customerChangedFlg;
	}

	public void setCustomerChangedFlg(final boolean customerChangedFlg) {
		this.customerChangedFlg = customerChangedFlg;
	}

	public CustomerDetailEntity withCustomerChangedFlg(boolean customerChangedFlg) {
		this.customerChangedFlg = customerChangedFlg;
		return this;
	}

	public boolean isInstalledChangedFlg() {
		return installedChangedFlg;
	}

	public void setInstalledChangedFlg(final boolean installedChangedFlg) {
		this.installedChangedFlg = installedChangedFlg;
	}

	public CustomerDetailEntity withInstalledChangedFlg(boolean installedChangedFlg) {
		this.installedChangedFlg = installedChangedFlg;
		return this;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public CustomerDetailEntity withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public CustomerDetailEntity withActive(boolean active) {
		this.active = active;
		return this;
	}

	public LocalDateTime getMoveInDate() {
		return moveInDate;
	}

	public void setMoveInDate(LocalDateTime moveInDate) {
		this.moveInDate = moveInDate;
	}

	public CustomerDetailEntity withMoveInDate(LocalDateTime moveInDate) {
		this.moveInDate = moveInDate;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(active, address, city, co, customerCategoryDescription, customerCategoryID, customerChangedFlg, customerId, customerOrgId, email1, email2, installedChangedFlg, moveInDate, name, organizationId, organizationName, partyId,
			phone1, phone2, phone3, zipcode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof final CustomerDetailEntity other)) {
			return false;
		}
		return active == other.active && Objects.equals(address, other.address) && Objects.equals(city, other.city) && Objects.equals(co, other.co) && Objects.equals(customerCategoryDescription, other.customerCategoryDescription) && Objects.equals(
			customerCategoryID, other.customerCategoryID) && customerChangedFlg == other.customerChangedFlg && Objects.equals(customerId, other.customerId) && Objects.equals(customerOrgId, other.customerOrgId) && Objects.equals(email1, other.email1)
			&& Objects.equals(email2, other.email2) && installedChangedFlg == other.installedChangedFlg && Objects.equals(moveInDate, other.moveInDate) && Objects.equals(name, other.name) && Objects.equals(organizationId, other.organizationId)
			&& Objects.equals(organizationName, other.organizationName) && Objects.equals(partyId, other.partyId) && Objects.equals(phone1, other.phone1) && Objects.equals(phone2, other.phone2) && Objects.equals(phone3, other.phone3) && Objects
				.equals(zipcode, other.zipcode);
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		builder.append("CustomerDetailEntity [customerId=").append(customerId).append(", partyId=").append(partyId).append(", customerOrgId=").append(customerOrgId).append(", organizationId=").append(organizationId).append(", organizationName=")
			.append(organizationName).append(", customerCategoryID=").append(customerCategoryID).append(", customerCategoryDescription=").append(customerCategoryDescription).append(", name=").append(name).append(", co=").append(co).append(
				", address=").append(address).append(", zipcode=").append(zipcode).append(", city=").append(city).append(", phone1=").append(phone1).append(", phone2=").append(phone2).append(", phone3=").append(phone3).append(", email1=").append(
					email1).append(", email2=").append(email2).append(", customerChangedFlg=").append(customerChangedFlg).append(", installedChangedFlg=").append(installedChangedFlg).append(", active=").append(active).append(", moveInDate=").append(
						moveInDate).append("]");
		return builder.toString();
	}
}
