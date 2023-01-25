package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityDayEntity;

import java.time.LocalDateTime;

public interface MeasurementElectricityDaySpecification {

	SpecificationBuilder<MeasurementElectricityDayEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<MeasurementElectricityDayEntity> withCustomerOrgId(String customerOrgId) {
		return BUILDER.buildEqualFilter("customerOrgId", customerOrgId);
	}

	static Specification<MeasurementElectricityDayEntity> withfacilityId(String facilityId) {
		return BUILDER.buildEqualFilter("facilityId", facilityId);
	}

	static Specification<MeasurementElectricityDayEntity> withMeasurementTimestamp(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
		return BUILDER.buildDateFilter("measurementTimestamp", dateTimeFrom, dateTimeTo);
	}
}
