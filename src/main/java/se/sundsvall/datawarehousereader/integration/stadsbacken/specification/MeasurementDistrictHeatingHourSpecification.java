package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingHourEntity;

import java.time.LocalDateTime;

public interface MeasurementDistrictHeatingHourSpecification {

	SpecificationBuilder<MeasurementDistrictHeatingHourEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<MeasurementDistrictHeatingHourEntity> withCustomerOrgId(String customerOrgId) {
		return BUILDER.buildEqualFilter("customerOrgId", customerOrgId);
	}

	static Specification<MeasurementDistrictHeatingHourEntity> withfacilityId(String facilityId) {
		return BUILDER.buildEqualFilter("facilityId", facilityId);
	}

	static Specification<MeasurementDistrictHeatingHourEntity> withMeasurementTimestamp(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
		return BUILDER.buildDateFilter("measurementTimestamp", dateTimeFrom, dateTimeTo);
	}
}
