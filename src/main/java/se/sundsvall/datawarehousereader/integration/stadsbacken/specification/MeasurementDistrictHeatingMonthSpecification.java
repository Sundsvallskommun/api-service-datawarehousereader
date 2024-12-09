package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingMonthEntity_.CUSTOMER_ORG_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingMonthEntity_.FACILITY_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingMonthEntity_.MEASUREMENT_TIMESTAMP;

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingMonthEntity;

public interface MeasurementDistrictHeatingMonthSpecification {

	SpecificationBuilder<MeasurementDistrictHeatingMonthEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<MeasurementDistrictHeatingMonthEntity> withCustomerOrgId(String customerOrgId) {
		return BUILDER.buildEqualFilter(CUSTOMER_ORG_ID, customerOrgId);
	}

	static Specification<MeasurementDistrictHeatingMonthEntity> withfacilityId(String facilityId) {
		return BUILDER.buildEqualFilter(FACILITY_ID, facilityId);
	}

	static Specification<MeasurementDistrictHeatingMonthEntity> withMeasurementTimestamp(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
		return BUILDER.buildDateFilter(MEASUREMENT_TIMESTAMP, dateTimeFrom, dateTimeTo);
	}
}
