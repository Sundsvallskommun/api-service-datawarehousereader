package se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase;

import static org.hibernate.annotations.FetchMode.SUBSELECT;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
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

@Entity
@Table(schema = "kundinfo", name = "vInstalledBase")
public class InstalledBaseItemEntity {

	@Id
	@Column(name = "BillLocationID", nullable = false, insertable = false, updatable = false)
	private Integer id;

	@Column(name = "InternalId", nullable = false, insertable = false, updatable = false)
	private Integer internalId;

	@Column(name = "customerid", nullable = false, insertable = false, updatable = false)
	private Integer customerId;

	@Column(name = "Company", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String company;

	@Column(name = "Type", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String type;

	@Column(name = "CareOf", insertable = false, updatable = false)
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

	@Column(name = "InstalledBaseLastChangedDate", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDate lastChangedDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(SUBSELECT)
	@BatchSize(size = 1000)
	@CollectionTable(schema = "kundinfo",
		name = "vInstalledBaseMetadata",
		joinColumns = @JoinColumn(
			name = "internalId",
			referencedColumnName = "InternalId"))
	private List<InstalledBaseItemMetaDataEmbeddable> metaData;

	public static InstalledBaseItemEntity create() {
		return new InstalledBaseItemEntity();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public InstalledBaseItemEntity withId(Integer id) {
		this.id = id;
		return this;
	}

	public Integer getInternalId() {
		return internalId;
	}

	public void setInternalId(Integer internalId) {
		this.internalId = internalId;
	}

	public InstalledBaseItemEntity withInternalId(Integer internalId) {
		this.internalId = internalId;
		return this;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public InstalledBaseItemEntity withCustomerId(Integer customerId) {
		this.customerId = customerId;
		return this;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public InstalledBaseItemEntity withCompany(String company) {
		this.company = company;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public InstalledBaseItemEntity withType(String type) {
		this.type = type;
		return this;
	}

	public String getCareOf() {
		return careOf;
	}

	public void setCareOf(String careOf) {
		this.careOf = careOf;
	}

	public InstalledBaseItemEntity withCareOf(String careOf) {
		this.careOf = careOf;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public InstalledBaseItemEntity withStreet(String street) {
		this.street = street;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public InstalledBaseItemEntity withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public InstalledBaseItemEntity withPostCode(String postCode) {
		this.postCode = postCode;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public InstalledBaseItemEntity withCity(String city) {
		this.city = city;
		return this;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public InstalledBaseItemEntity withHouseName(String houseName) {
		this.houseName = houseName;
		return this;
	}

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public InstalledBaseItemEntity withDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}

	public InstalledBaseItemEntity withDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
		return this;
	}

	public LocalDate getLastChangedDate() {
		return lastChangedDate;
	}

	public void setLastChangedDate(LocalDate lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}

	public InstalledBaseItemEntity withLastChangedDate(LocalDate lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
		return this;
	}

	public List<InstalledBaseItemMetaDataEmbeddable> getMetaData() {
		return metaData;
	}

	public void setMetaData(List<InstalledBaseItemMetaDataEmbeddable> metaData) {
		this.metaData = metaData;
	}

	public InstalledBaseItemEntity withMetaData(List<InstalledBaseItemMetaDataEmbeddable> metaData) {
		this.metaData = metaData;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(careOf, city, company, customerId, dateFrom, dateTo, facilityId, houseName, id, internalId, metaData, lastChangedDate, postCode, street, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof InstalledBaseItemEntity)) {
			return false;
		}
		InstalledBaseItemEntity other = (InstalledBaseItemEntity) obj;
		return Objects.equals(careOf, other.careOf) && Objects.equals(city, other.city) && Objects.equals(company, other.company) && Objects.equals(customerId, other.customerId) && Objects.equals(dateFrom, other.dateFrom) && Objects.equals(dateTo,
			other.dateTo) && Objects.equals(facilityId, other.facilityId) && Objects.equals(houseName, other.houseName) && Objects.equals(id, other.id) && Objects.equals(internalId, other.internalId) && Objects.equals(metaData, other.metaData)
			&& Objects.equals(lastChangedDate, other.lastChangedDate) && Objects.equals(postCode, other.postCode) && Objects.equals(street, other.street) && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstalledBaseItemEntity [id=").append(id).append(", internalId=").append(internalId).append(", customerId=").append(customerId).append(", company=").append(company).append(", type=").append(type).append(", careOf=").append(
			careOf).append(", street=").append(street).append(", facilityId=").append(facilityId).append(", postCode=").append(postCode).append(", city=").append(city).append(", houseName=").append(houseName).append(", dateFrom=").append(dateFrom)
			.append(", dateTo=").append(dateTo).append(", lastChangedDate=").append(lastChangedDate).append(", metaData=").append(metaData).append("]");
		return builder.toString();
	}
}
