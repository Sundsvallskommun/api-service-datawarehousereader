package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityHourEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityKey;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementElectricityHourSpecification.withCustomerOrgId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementElectricityHourSpecification.withMeasurementTimestamp;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.MeasurementElectricityHourSpecification.withfacilityId;

@Transactional
@CircuitBreaker(name = "measurementElectricityHourRepository")
public interface MeasurementElectricityHourRepository 
	extends PagingAndSortingRepository<MeasurementElectricityHourEntity, MeasurementElectricityKey>, JpaSpecificationExecutor<MeasurementElectricityHourEntity> {
	
	default Page<MeasurementElectricityHourEntity> findAllMatching(String customerOrgNumber, String facilityId, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, Pageable pageable) {
		return this.findAll(
			withCustomerOrgId(customerOrgNumber).
				and(withfacilityId(facilityId)).
				and(withMeasurementTimestamp(dateTimeFrom, dateTimeTo))
			, pageable);
	}
}
