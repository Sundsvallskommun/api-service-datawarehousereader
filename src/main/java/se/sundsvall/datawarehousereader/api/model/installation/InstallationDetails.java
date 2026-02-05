package se.sundsvall.datawarehousereader.api.model.installation;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Schema(description = "Installation details model")
public class InstallationDetails {

	@Schema(description = "Company", examples = "Sundsvall Energi AB", accessMode = READ_ONLY)
	private String company;

	@Schema(description = "Type", examples = "Fjärrvärme", accessMode = READ_ONLY)
	private String type;

	@Schema(description = "Facility id", examples = "735999109270751042", accessMode = READ_ONLY)
	private String facilityId;

	@Schema(description = "Placement id", examples = "5263", accessMode = READ_ONLY)
	private int placementId;

	@Schema(description = "Care of address", examples = "Agatha Malm", accessMode = READ_ONLY)
	private String careOf;

	@Schema(description = "Street", examples = "Storgatan 9", accessMode = READ_ONLY)
	private String street;

	@Schema(description = "Post code", examples = "85230", accessMode = READ_ONLY)
	private String postCode;

	@Schema(description = "City", examples = "Sundsvall", accessMode = READ_ONLY)
	private String city;

	@Schema(description = "Property designation", examples = "Södermalm 1:27", accessMode = READ_ONLY)
	private String propertyDesignation;

	@Schema(description = "From date", examples = "2019-01-01", accessMode = READ_ONLY)
	private LocalDate dateFrom;

	@Schema(description = "To date", examples = "2022-12-31", accessMode = READ_ONLY)
	private LocalDate dateTo;

	@Schema(description = "Date when object was last modified (or null if never modified)", examples = "2022-12-31", accessMode = READ_ONLY)
	private LocalDate dateLastModified;

	@ArraySchema(schema = @Schema(implementation = InstallationMetaData.class, accessMode = READ_ONLY), maxItems = 1000)
	private List<InstallationMetaData> metaData;

	public static InstallationDetails create() {
		return new InstallationDetails();
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(final String company) {
		this.company = company;
	}

	public InstallationDetails withCompany(final String company) {
		this.company = company;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public InstallationDetails withType(final String type) {
		this.type = type;
		return this;
	}

	public String getCareOf() {
		return careOf;
	}

	public void setCareOf(final String careOf) {
		this.careOf = careOf;
	}

	public InstallationDetails withCareOf(final String careOf) {
		this.careOf = careOf;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(final String street) {
		this.street = street;
	}

	public InstallationDetails withStreet(final String street) {
		this.street = street;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(final String facilityId) {
		this.facilityId = facilityId;
	}

	public InstallationDetails withFacilityId(final String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public int getPlacementId() {
		return placementId;
	}

	public void setPlacementId(int placementId) {
		this.placementId = placementId;
	}

	public InstallationDetails withPlacementId(final int placementId) {
		this.placementId = placementId;
		return this;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(final String postCode) {
		this.postCode = postCode;
	}

	public InstallationDetails withPostCode(final String postCode) {
		this.postCode = postCode;
		return this;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public InstallationDetails withCity(final String city) {
		this.city = city;
		return this;
	}

	public String getPropertyDesignation() {
		return propertyDesignation;
	}

	public void setPropertyDesignation(final String propertyDesignation) {
		this.propertyDesignation = propertyDesignation;
	}

	public InstallationDetails withPropertyDesignation(final String propertyDesignation) {
		this.propertyDesignation = propertyDesignation;
		return this;
	}

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(final LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public InstallationDetails withDateFrom(final LocalDate dateFrom) {
		this.dateFrom = dateFrom;
		return this;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setDateTo(final LocalDate dateTo) {
		this.dateTo = dateTo;
	}

	public InstallationDetails withDateTo(final LocalDate dateTo) {
		this.dateTo = dateTo;
		return this;
	}

	public LocalDate getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(final LocalDate dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public InstallationDetails withDateLastModified(final LocalDate dateLastModified) {
		this.dateLastModified = dateLastModified;
		return this;
	}

	public List<InstallationMetaData> getMetaData() {
		return metaData;
	}

	public void setMetaData(final List<InstallationMetaData> metaData) {
		this.metaData = metaData;
	}

	public InstallationDetails withMetaData(final List<InstallationMetaData> metaData) {
		this.metaData = metaData;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		InstallationDetails that = (InstallationDetails) o;
		return Objects.equals(company, that.company) && Objects.equals(type, that.type) && Objects.equals(facilityId, that.facilityId) && Objects.equals(placementId, that.placementId) && Objects.equals(careOf, that.careOf) && Objects.equals(street,
			that.street) && Objects.equals(postCode, that.postCode) && Objects.equals(city, that.city) && Objects.equals(propertyDesignation, that.propertyDesignation) && Objects.equals(dateFrom, that.dateFrom) && Objects.equals(dateTo, that.dateTo)
			&& Objects.equals(dateLastModified, that.dateLastModified) && Objects.equals(metaData, that.metaData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(company, type, facilityId, placementId, careOf, street, postCode, city, propertyDesignation, dateFrom, dateTo, dateLastModified, metaData);
	}

	@Override
	public String toString() {
		return "InstallationDetails{" +
			"company='" + company + '\'' +
			", type='" + type + '\'' +
			", facilityId='" + facilityId + '\'' +
			", placementId=" + placementId +
			", careOf='" + careOf + '\'' +
			", street='" + street + '\'' +
			", postCode='" + postCode + '\'' +
			", city='" + city + '\'' +
			", propertyDesignation='" + propertyDesignation + '\'' +
			", dateFrom=" + dateFrom +
			", dateTo=" + dateTo +
			", dateLastModified=" + dateLastModified +
			", metaData=" + metaData +
			'}';
	}
}
