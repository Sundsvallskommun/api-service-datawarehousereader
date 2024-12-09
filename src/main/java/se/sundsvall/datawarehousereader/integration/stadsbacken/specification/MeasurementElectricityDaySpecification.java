package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityDayEntity_.CUSTOMER_ORG_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityDayEntity_.FACILITY_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityDayEntity_.MEASUREMENT_TIMESTAMP;

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityDayEntity;

public interface MeasurementElectricityDaySpecification {

	SpecificationBuilder<MeasurementElectricityDayEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<MeasurementElectricityDayEntity> withCustomerOrgId(String customerOrgId) {
		return BUILDER.buildEqualFilter(CUSTOMER_ORG_ID, customerOrgId);
	}

	static Specification<MeasurementElectricityDayEntity> withfacilityId(String facilityId) {
		return BUILDER.buildEqualFilter(FACILITY_ID, facilityId);
	}

	static Specification<MeasurementElectricityDayEntity> withMeasurementTimestamp(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
		return BUILDER.buildDateFilter(MEASUREMENT_TIMESTAMP, dateTimeFrom, dateTimeTo);
	}
}
