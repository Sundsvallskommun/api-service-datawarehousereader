package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityKey;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityMonthEntity;

import java.time.LocalDateTime;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementElectricityMonthSpecification.withCustomerOrgId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementElectricityMonthSpecification.withMeasurementTimestamp;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementElectricityMonthSpecification.withfacilityId;

@Transactional
@CircuitBreaker(name = "measurementElectricityMonthRepository")
public interface MeasurementElectricityMonthRepository 
	extends PagingAndSortingRepository<MeasurementElectricityMonthEntity, MeasurementElectricityKey>, JpaSpecificationExecutor<MeasurementElectricityMonthEntity> {
	
	default Page<MeasurementElectricityMonthEntity> findAllMatching(String customerOrgNumber, String facilityId, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, Pageable pageable) {
		return this.findAll(
			withCustomerOrgId(customerOrgNumber).
				and(withfacilityId(facilityId)).
				and(withMeasurementTimestamp(dateTimeFrom, dateTimeTo))
			, pageable);
	}
}
