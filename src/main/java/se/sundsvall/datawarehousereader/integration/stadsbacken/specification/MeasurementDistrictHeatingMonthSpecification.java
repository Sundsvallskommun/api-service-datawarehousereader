package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingMonthEntity;

import java.time.LocalDateTime;

public interface MeasurementDistrictHeatingMonthSpecification {

	SpecificationBuilder<MeasurementDistrictHeatingMonthEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<MeasurementDistrictHeatingMonthEntity> withCustomerOrgId(String customerOrgId) {
		return BUILDER.buildEqualFilter("customerOrgId", customerOrgId);
	}

	static Specification<MeasurementDistrictHeatingMonthEntity> withfacilityId(String facilityId) {
		return BUILDER.buildEqualFilter("facilityId", facilityId);
	}

	static Specification<MeasurementDistrictHeatingMonthEntity> withMeasurementTimestamp(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
		return BUILDER.buildDateFilter("measurementTimestamp", dateTimeFrom, dateTimeTo);
	}
}
