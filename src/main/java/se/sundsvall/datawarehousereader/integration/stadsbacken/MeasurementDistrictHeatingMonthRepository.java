package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingKey;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingMonthEntity;

import java.time.LocalDateTime;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementDistrictHeatingMonthSpecification.withCustomerOrgId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementDistrictHeatingMonthSpecification.withMeasurementTimestamp;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementDistrictHeatingMonthSpecification.withfacilityId;

@Transactional(readOnly = true)
@CircuitBreaker(name = "measurementDistrictHeatingMonthRepository")
public interface MeasurementDistrictHeatingMonthRepository 
	extends PagingAndSortingRepository<MeasurementDistrictHeatingMonthEntity, MeasurementDistrictHeatingKey>, JpaSpecificationExecutor<MeasurementDistrictHeatingMonthEntity> {
	
	default Page<MeasurementDistrictHeatingMonthEntity> findAllMatching(String customerOrgNumber, String facilityId, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, Pageable pageable) {
		return this.findAll(
			withCustomerOrgId(customerOrgNumber).
				and(withfacilityId(facilityId)).
				and(withMeasurementTimestamp(dateTimeFrom, dateTimeTo))
			, pageable);
	}
}
