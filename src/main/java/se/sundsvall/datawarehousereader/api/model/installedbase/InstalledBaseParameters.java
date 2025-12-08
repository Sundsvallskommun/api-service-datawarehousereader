package se.sundsvall.datawarehousereader.api.model.installedbase;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity;
import se.sundsvall.dept44.models.api.paging.AbstractParameterPagingAndSortingBase;
import se.sundsvall.dept44.models.api.paging.validation.ValidSortByProperty;

@Schema(description = "Installed base request parameters model")
@ValidSortByProperty(InstalledBaseItemEntity.class)
@ParameterObject
public class InstalledBaseParameters extends AbstractParameterPagingAndSortingBase {

	@Schema(description = "Company", examples = "Sundsvall Energi AB")
	private String company;

	@Schema(description = "Customer number", examples = "104327")
	private String customerNumber;

	@Schema(description = "Type", examples = "Fjärrvärme")
	private String type;

	@Schema(description = "Facility id", examples = "735999109270751042")
	private String facilityId;

	@Schema(description = "Care of address", examples = "Agatha Malm")
	private String careOf;

	@Schema(description = "Street", examples = "Storgatan 9")
	private String street;

	@Schema(description = "Postal code", examples = "85230")
	private String postCode;

	@Schema(description = "City", examples = "Sundsvall")
	private String city;

	@Schema(description = "Property designation", examples = "Södermalm 1:27")
	private String propertyDesignation;

	@Schema(description = "Earliest date when item was last modified", examples = "2022-12-31")
	private LocalDate lastModifiedDateFrom;

	@Schema(description = "Latest date when item was last modified", examples = "2022-12-31")
	private LocalDate lastModifiedDateTom;

	public static InstalledBaseParameters create() {
		return new InstalledBaseParameters();
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCareOf() {
		return careOf;
	}

	public void setCareOf(String careOf) {
		this.careOf = careOf;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPropertyDesignation() {
		return propertyDesignation;
	}

	public void setPropertyDesignation(String propertyDesignation) {
		this.propertyDesignation = propertyDesignation;
	}

	public LocalDate getLastModifiedDateFrom() {
		return lastModifiedDateFrom;
	}

	public void setLastModifiedDateFrom(LocalDate lastModifiedDateFrom) {
		this.lastModifiedDateFrom = lastModifiedDateFrom;
	}

	public LocalDate getLastModifiedDateTom() {
		return lastModifiedDateTom;
	}

	public void setLastModifiedDateTom(LocalDate lastModifiedDateTom) {
		this.lastModifiedDateTom = lastModifiedDateTom;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(careOf, city, company, customerNumber, facilityId, lastModifiedDateFrom, lastModifiedDateTom, postCode, propertyDesignation, street, type);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof InstalledBaseParameters)) {
			return false;
		}
		InstalledBaseParameters other = (InstalledBaseParameters) obj;
		return Objects.equals(careOf, other.careOf) && Objects.equals(city, other.city) && Objects.equals(company, other.company) && Objects.equals(customerNumber, other.customerNumber) && Objects.equals(facilityId, other.facilityId) && Objects
			.equals(lastModifiedDateFrom, other.lastModifiedDateFrom) && Objects.equals(lastModifiedDateTom, other.lastModifiedDateTom) && Objects.equals(postCode, other.postCode) && Objects.equals(propertyDesignation, other.propertyDesignation)
			&& Objects.equals(street, other.street) && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstalledBaseParameters [company=").append(company).append(", customerNumber=").append(customerNumber).append(", type=").append(type).append(", facilityId=").append(facilityId).append(", careOf=").append(careOf).append(
			", street=").append(street).append(", postCode=").append(postCode).append(", city=").append(city).append(", propertyDesignation=").append(propertyDesignation).append(", lastModifiedDateFrom=").append(lastModifiedDateFrom).append(
				", lastModifiedDateTom=").append(lastModifiedDateTom).append(", sortBy=").append(sortBy).append(", sortDirection=").append(sortDirection).append(", page=").append(page).append(", limit=").append(limit).append("]");
		return builder.toString();
	}
}
