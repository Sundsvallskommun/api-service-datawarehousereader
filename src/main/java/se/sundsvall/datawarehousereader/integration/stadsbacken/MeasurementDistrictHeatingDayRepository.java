package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingDayEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingKey;

import java.time.LocalDateTime;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementDistrictHeatingDaySpecification.withCustomerOrgId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementDistrictHeatingDaySpecification.withMeasurementTimestamp;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementDistrictHeatingDaySpecification.withfacilityId;

@Transactional(readOnly = true)
@CircuitBreaker(name = "measurementDistrictHeatingDayRepository")
public interface MeasurementDistrictHeatingDayRepository
	extends PagingAndSortingRepository<MeasurementDistrictHeatingDayEntity, MeasurementDistrictHeatingKey>, JpaSpecificationExecutor<MeasurementDistrictHeatingDayEntity> {
	
	default Page<MeasurementDistrictHeatingDayEntity> findAllMatching(String customerOrgNumber, String facilityId, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, Pageable pageable) {
		return this.findAll(
			withCustomerOrgId(customerOrgNumber).
				and(withfacilityId(facilityId)).
				and(withMeasurementTimestamp(dateTimeFrom, dateTimeTo))
			, pageable);
	}
}
