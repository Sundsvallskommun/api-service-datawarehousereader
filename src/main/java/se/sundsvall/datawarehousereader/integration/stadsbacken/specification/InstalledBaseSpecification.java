package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity_.CARE_OF;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity_.CITY;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity_.COMPANY;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity_.CUSTOMER_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity_.FACILITY_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity_.HOUSE_NAME;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity_.PLACEMENT_MODIFIED;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity_.POST_CODE;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity_.STREET;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity_.TYPE;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity;

public interface InstalledBaseSpecification {

	SpecificationBuilder<InstalledBaseItemEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<InstalledBaseItemEntity> withCareOf(String careOf) {
		return BUILDER.buildEqualFilter(CARE_OF, careOf);
	}

	static Specification<InstalledBaseItemEntity> withCity(String city) {
		return BUILDER.buildEqualFilter(CITY, city);
	}

	static Specification<InstalledBaseItemEntity> withCompany(String company) {
		return BUILDER.buildEqualFilter(COMPANY, company);
	}

	static Specification<InstalledBaseItemEntity> withCustomerId(Integer customerId) {
		return BUILDER.buildEqualFilter(CUSTOMER_ID, customerId);
	}

	static Specification<InstalledBaseItemEntity> withFacilityId(String facilityId) {
		return BUILDER.buildEqualFilter(FACILITY_ID, facilityId);
	}
	
	static Specification<InstalledBaseItemEntity> withPostCode(String postCode) {
		return BUILDER.buildEqualFilter(POST_CODE, postCode);
	}

	static Specification<InstalledBaseItemEntity> withHouseName(String houseName) {
		return BUILDER.buildEqualFilter(HOUSE_NAME, houseName);
	}

	static Specification<InstalledBaseItemEntity> withStreet(String street) {
		return BUILDER.buildEqualFilter(STREET, street);
	}

	static Specification<InstalledBaseItemEntity> withType(String type) {
		return BUILDER.buildEqualFilter(TYPE, type);
	}

	static Specification<InstalledBaseItemEntity> withLastModifiedDateBetween(LocalDate dateFrom, LocalDate dateTom) {
		return BUILDER.buildDateFilter(PLACEMENT_MODIFIED, dateFrom, dateTom);
	}
}
