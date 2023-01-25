package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityHourEntity;

import java.time.LocalDateTime;

public interface MeasurementElectricityHourSpecification {

	SpecificationBuilder<MeasurementElectricityHourEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<MeasurementElectricityHourEntity> withCustomerOrgId(String customerOrgId) {
		return BUILDER.buildEqualFilter("customerOrgId", customerOrgId);
	}

	static Specification<MeasurementElectricityHourEntity> withfacilityId(String facilityId) {
		return BUILDER.buildEqualFilter("facilityId", facilityId);
	}

	static Specification<MeasurementElectricityHourEntity> withMeasurementTimestamp(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
		return BUILDER.buildDateFilter("measurementTimestamp", dateTimeFrom, dateTimeTo);
	}
}
