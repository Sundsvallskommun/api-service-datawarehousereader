package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityMonthEntity_.CUSTOMER_ORG_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityMonthEntity_.FACILITY_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityMonthEntity_.MEASUREMENT_TIMESTAMP;

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityMonthEntity;

public interface MeasurementElectricityMonthSpecification {

	SpecificationBuilder<MeasurementElectricityMonthEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<MeasurementElectricityMonthEntity> withCustomerOrgId(String customerOrgId) {
		return BUILDER.buildEqualFilter(CUSTOMER_ORG_ID, customerOrgId);
	}

	static Specification<MeasurementElectricityMonthEntity> withfacilityId(String facilityId) {
		return BUILDER.buildEqualFilter(FACILITY_ID, facilityId);
	}

	static Specification<MeasurementElectricityMonthEntity> withMeasurementTimestamp(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
		return BUILDER.buildDateFilter(MEASUREMENT_TIMESTAMP, dateTimeFrom, dateTimeTo);
	}
}
