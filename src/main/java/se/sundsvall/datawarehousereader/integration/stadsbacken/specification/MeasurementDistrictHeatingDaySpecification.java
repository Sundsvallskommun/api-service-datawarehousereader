package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingDayEntity;

import java.time.LocalDateTime;

public interface MeasurementDistrictHeatingDaySpecification {

	SpecificationBuilder<MeasurementDistrictHeatingDayEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<MeasurementDistrictHeatingDayEntity> withCustomerOrgId(String customerOrgId) {
		return BUILDER.buildEqualFilter("customerOrgId", customerOrgId);
	}

	static Specification<MeasurementDistrictHeatingDayEntity> withfacilityId(String facilityId) {
		return BUILDER.buildEqualFilter("facilityId", facilityId);
	}

	static Specification<MeasurementDistrictHeatingDayEntity> withMeasurementTimestamp(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
		return BUILDER.buildDateFilter("measurementTimestamp", dateTimeFrom, dateTimeTo);
	}
}