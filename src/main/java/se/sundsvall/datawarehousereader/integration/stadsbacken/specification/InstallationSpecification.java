package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationEntity_.CUSTOMER_FLAG;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationEntity_.FACILITY_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationEntity_.LAST_CHANGED_DATE;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationEntity_.TYPE;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationEntity;

public interface InstallationSpecification {

	SpecificationBuilder<InstallationEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<InstallationEntity> withCustomerFlag(final Boolean flag) {
		return BUILDER.buildEqualFilter(CUSTOMER_FLAG, flag);
	}

	static Specification<InstallationEntity> withLastModifiedDateBetween(final LocalDate from, final LocalDate to) {
		return BUILDER.buildDateFilter(LAST_CHANGED_DATE, from, to);
	}

	static Specification<InstallationEntity> withType(final String type) {
		return BUILDER.buildEqualFilter(TYPE, type);
	}

	static Specification<InstallationEntity> withFacilityId(final String facilityId) {
		return BUILDER.buildEqualFilter(FACILITY_ID, facilityId);
	}
}
