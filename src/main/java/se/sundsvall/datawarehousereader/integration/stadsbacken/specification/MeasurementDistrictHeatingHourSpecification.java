package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingHourEntity_.CUSTOMER_ORG_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingHourEntity_.FACILITY_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingHourEntity_.MEASUREMENT_TIMESTAMP;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingHourEntity;

public interface MeasurementDistrictHeatingHourSpecification {

	SpecificationBuilder<MeasurementDistrictHeatingHourEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<MeasurementDistrictHeatingHourEntity> withCustomerOrgId(String customerOrgId) {
		return BUILDER.buildEqualFilter(CUSTOMER_ORG_ID, customerOrgId);
	}

	static Specification<MeasurementDistrictHeatingHourEntity> withfacilityId(String facilityId) {
		return BUILDER.buildEqualFilter(FACILITY_ID, facilityId);
	}

	static Specification<MeasurementDistrictHeatingHourEntity> withMeasurementTimestamp(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
		return BUILDER.buildDateFilter(MEASUREMENT_TIMESTAMP, dateTimeFrom, dateTimeTo);
	}
}
