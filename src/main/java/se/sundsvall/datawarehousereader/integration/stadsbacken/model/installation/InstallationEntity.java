package se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.type.NumericBooleanConverter;

import static org.hibernate.annotations.FetchMode.SUBSELECT;

@Entity
@Table(schema = "kundinfo", name = "vInstallations")
public class InstallationEntity {

	@Id
	@Column(name = "BillLocationID", nullable = false, insertable = false, updatable = false)
	private Integer id;

	@Column(name = "InternalId", nullable = false, insertable = false, updatable = false)
	private Integer internalId;

	@Convert(converter = NumericBooleanConverter.class)
	@Column(name = "CustomerFlg", nullable = false, insertable = false, updatable = false)
	private Boolean customerFlag;

	@Column(name = "Company", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String company;

	@Column(name = "Type", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String type;

	@Column(name = "Careof", insertable = false, updatable = false)
	private String careOf;

	@Column(name = "Street", insertable = false, updatable = false)
	private String street;

	@Column(name = "FacilityId", insertable = false, updatable = false)
	private String facilityId;

	@Column(name = "Postcode", insertable = false, updatable = false)
	private String postCode;

	@Column(name = "City", insertable = false, updatable = false)
	private String city;

	@Column(name = "HouseName", insertable = false, updatable = false)
	private String houseName;

	@Column(name = "DateFrom", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDate dateFrom;

	@Column(name = "DateTo", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDate dateTo;

	@Column(name = "InstallationsLastChangedDate", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDate lastChangedDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(SUBSELECT)
	@BatchSize(size = 1000)
	@CollectionTable(schema = "kundinfo",
		name = "vInstalledBaseMetadata",
		joinColumns = @JoinColumn(
			name = "internalId",
			referencedColumnName = "InternalId"))
	private List<InstallationMetaDataEmbeddable> metaData;

	public static InstallationEntity create() {
		return new InstallationEntity();
	}

	public List<InstallationMetaDataEmbeddable> getMetaData() {
		return metaData;
	}

	public void setMetaData(final List<InstallationMetaDataEmbeddable> metaData) {
		this.metaData = metaData;
	}

	public InstallationEntity withMetaData(final List<InstallationMetaDataEmbeddable> metaData) {
		this.metaData = metaData;
		return this;
	}

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public InstallationEntity withId(final Integer id) {
		this.id = id;
		return this;
	}

	public Integer getInternalId() {
		return internalId;
	}

	public void setInternalId(final Integer internalId) {
		this.internalId = internalId;
	}

	public InstallationEntity withInternalId(final Integer id) {
		this.internalId = id;
		return this;
	}

	public Boolean getCustomerFlag() {
		return customerFlag;
	}

	public void setCustomerFlag(final Boolean customerFlag) {
		this.customerFlag = customerFlag;
	}

	public InstallationEntity withCustomerFlag(final Boolean flag) {
		this.customerFlag = flag;
		return this;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(final String company) {
		this.company = company;
	}

	public InstallationEntity withCompany(final String company) {
		this.company = company;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public InstallationEntity withType(final String type) {
		this.type = type;
		return this;
	}

	public String getCareOf() {
		return careOf;
	}

	public void setCareOf(final String careOf) {
		this.careOf = careOf;
	}

	public InstallationEntity withCareOf(final String careOf) {
		this.careOf = careOf;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(final String street) {
		this.street = street;
	}

	public InstallationEntity withStreet(final String street) {
		this.street = street;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(final String facilityId) {
		this.facilityId = facilityId;
	}

	public InstallationEntity withFacilityId(final String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(final String postCode) {
		this.postCode = postCode;
	}

	public InstallationEntity withPostCode(final String postCode) {
		this.postCode = postCode;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public InstallationEntity withCity(final String city) {
		this.city = city;
		return this;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(final String houseName) {
		this.houseName = houseName;
	}

	public InstallationEntity withHouseName(final String houseName) {
		this.houseName = houseName;
		return this;
	}

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(final LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public InstallationEntity withDateFrom(final LocalDate dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setDateTo(final LocalDate dateTo) {
		this.dateTo = dateTo;
	}

	public InstallationEntity withDateTo(final LocalDate dateTo) {
		this.dateTo = dateTo;
		return this;
	}

	public LocalDate getLastChangedDate() {
		return lastChangedDate;
	}

	public void setLastChangedDate(final LocalDate lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}

	public InstallationEntity withLastChangedDate(final LocalDate lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		InstallationEntity that = (InstallationEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(internalId, that.internalId) && Objects.equals(customerFlag, that.customerFlag) && Objects.equals(company, that.company) && Objects.equals(type, that.type) && Objects.equals(careOf, that.careOf)
			&& Objects.equals(street, that.street) && Objects.equals(facilityId, that.facilityId) && Objects.equals(postCode, that.postCode) && Objects.equals(city, that.city) && Objects.equals(houseName, that.houseName) && Objects.equals(dateFrom,
				that.dateFrom) && Objects.equals(dateTo, that.dateTo) && Objects.equals(lastChangedDate, that.lastChangedDate) && Objects.equals(metaData, that.metaData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, internalId, customerFlag, company, type, careOf, street, facilityId, postCode, city, houseName, dateFrom, dateTo, lastChangedDate, metaData);
	}

	@Override
	public String toString() {
		return "InstallationEntity{" +
			"id=" + id +
			", internalId=" + internalId +
			", customerFlag=" + customerFlag +
			", company='" + company + '\'' +
			", type='" + type + '\'' +
			", careOf='" + careOf + '\'' +
			", street='" + street + '\'' +
			", facilityId='" + facilityId + '\'' +
			", postCode='" + postCode + '\'' +
			", city='" + city + '\'' +
			", houseName='" + houseName + '\'' +
			", dateFrom=" + dateFrom +
			", dateTo=" + dateTo +
			", lastChangedDate=" + lastChangedDate +
			", metaData=" + metaData +
			'}';
	}
}
