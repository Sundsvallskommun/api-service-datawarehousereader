package se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "kundinfo", name = "vCustomerDetail")
public class CustomerDetailsEntity {

	@Id
	@Column(name = "Customerid", nullable = false, insertable = false, updatable = false)
	private Integer customerId;

	@Column(name = "uuid", columnDefinition = "uniqueidentifier", insertable = false, updatable = false)
	private String uuid;

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

	public static CustomerDetailsEntity create() {
		return new CustomerDetailsEntity();
	}

	public CustomerDetailsEntity withCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
		return this;
	}

	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public void setCustomerOrgId(final String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public CustomerDetailsEntity withOrganizationId(String organizationId) {
		this.organizationId = organizationId;
		return this;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public CustomerDetailsEntity withOrganizationName(String organizationName) {
		this.organizationName = organizationName;
		return this;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public CustomerDetailsEntity withCustomerId(Integer customerId) {
		this.customerId = customerId;
		return this;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(final Integer customerId) {
		this.customerId = customerId;
	}

	public CustomerDetailsEntity withCustomerCategoryID(Integer customerCategoryID) {
		this.customerCategoryID = customerCategoryID;
		return this;
	}

	public Integer getCustomerCategoryID() {
		return customerCategoryID;
	}

	public void setCustomerCategoryID(final Integer customerCategoryID) {
		this.customerCategoryID = customerCategoryID;
	}

	public CustomerDetailsEntity withCustomerCategoryDescription(String customerCategoryDescription) {
		this.customerCategoryDescription = customerCategoryDescription;
		return this;
	}

	public String getCustomerCategoryDescription() {
		return customerCategoryDescription;
	}

	public void setCustomerCategoryDescription(final String customerCategoryDescription) {
		this.customerCategoryDescription = customerCategoryDescription;
	}

	public CustomerDetailsEntity withName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public CustomerDetailsEntity withCo(String co) {
		this.co = co;
		return this;
	}

	public String getCo() {
		return co;
	}

	public void setCo(final String co) {
		this.co = co;
	}

	public CustomerDetailsEntity withAddress(String address) {
		this.address = address;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public CustomerDetailsEntity withZipcode(String zipcode) {
		this.zipcode = zipcode;
		return this;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(final String zipcode) {
		this.zipcode = zipcode;
	}

	public CustomerDetailsEntity withCity(String city) {
		this.city = city;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public CustomerDetailsEntity withPhone1(String phone1) {
		this.phone1 = phone1;
		return this;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(final String phone1) {
		this.phone1 = phone1;
	}

	public CustomerDetailsEntity withPhone2(String phone2) {
		this.phone2 = phone2;
		return this;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(final String phone2) {
		this.phone2 = phone2;
	}

	public CustomerDetailsEntity withPhone3(String phone3) {
		this.phone3 = phone3;
		return this;
	}

	public String getPhone3() {
		return phone3;
	}

	public void setPhone3(final String phone3) {
		this.phone3 = phone3;
	}

	public CustomerDetailsEntity withEmail1(String email1) {
		this.email1 = email1;
		return this;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(final String email1) {
		this.email1 = email1;
	}

	public CustomerDetailsEntity withEmail2(String email2) {
		this.email2 = email2;
		return this;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(final String email2) {
		this.email2 = email2;
	}

	public CustomerDetailsEntity withCustomerChangedFlg(boolean customerChangedFlg) {
		this.customerChangedFlg = customerChangedFlg;
		return this;
	}

	public boolean isCustomerChangedFlg() {
		return customerChangedFlg;
	}

	public void setCustomerChangedFlg(final boolean customerChangedFlg) {
		this.customerChangedFlg = customerChangedFlg;
	}

	public CustomerDetailsEntity withInstalledChangedFlg(boolean installedChangedFlg) {
		this.installedChangedFlg = installedChangedFlg;
		return this;
	}

	public boolean isInstalledChangedFlg() {
		return installedChangedFlg;
	}

	public void setInstalledChangedFlg(final boolean installedChangedFlg) {
		this.installedChangedFlg = installedChangedFlg;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public CustomerDetailsEntity withUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	@Override
	public String toString() {
		return "CustomerDetailsEntity{" +
				"customerId=" + customerId +
				", uuid='" + uuid + '\'' +
				", customerOrgId='" + customerOrgId + '\'' +
				", organizationId='" + organizationId + '\'' +
				", organizationName='" + organizationName + '\'' +
				", customerCategoryID=" + customerCategoryID +
				", customerCategoryDescription='" + customerCategoryDescription + '\'' +
				", name='" + name + '\'' +
				", co='" + co + '\'' +
				", address='" + address + '\'' +
				", zipcode='" + zipcode + '\'' +
				", city='" + city + '\'' +
				", phone1='" + phone1 + '\'' +
				", phone2='" + phone2 + '\'' +
				", phone3='" + phone3 + '\'' +
				", email1='" + email1 + '\'' +
				", email2='" + email2 + '\'' +
				", customerChangedFlg=" + customerChangedFlg +
				", installedChangedFlg=" + installedChangedFlg +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CustomerDetailsEntity that = (CustomerDetailsEntity) o;
		return customerChangedFlg == that.customerChangedFlg && installedChangedFlg == that.installedChangedFlg && Objects.equals(customerId, that.customerId) && Objects.equals(uuid, that.uuid) && Objects.equals(customerOrgId, that.customerOrgId) && Objects.equals(organizationId, that.organizationId) && Objects.equals(organizationName, that.organizationName) && Objects.equals(customerCategoryID, that.customerCategoryID) && Objects.equals(customerCategoryDescription, that.customerCategoryDescription) && Objects.equals(name, that.name) && Objects.equals(co, that.co) && Objects.equals(address, that.address) && Objects.equals(zipcode, that.zipcode) && Objects.equals(city, that.city) && Objects.equals(phone1, that.phone1) && Objects.equals(phone2, that.phone2) && Objects.equals(phone3, that.phone3) && Objects.equals(email1, that.email1) && Objects.equals(email2, that.email2);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerId, uuid, customerOrgId, organizationId, organizationName, customerCategoryID, customerCategoryDescription, name, co, address, zipcode, city, phone1, phone2, phone3, email1, email2, customerChangedFlg, installedChangedFlg);
	}
}
