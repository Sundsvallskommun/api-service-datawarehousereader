package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityHourEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityKey;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@CircuitBreaker(name = "measurementElectricityHourRepository")
public interface MeasurementElectricityHourRepository 
	extends PagingAndSortingRepository<MeasurementElectricityHourEntity, MeasurementElectricityKey>, JpaSpecificationExecutor<MeasurementElectricityHourEntity> {
	
	@Query(value = "exec kundinfo.spMeasurementElectricityHour :customerOrgNumber, :facilityId, :dateTimeFrom, :dateTimeTo", nativeQuery = true)
	List<MeasurementElectricityHourEntity> findAllMatching(@Param("customerOrgNumber") String customerOrgNumber, @Param("facilityId") String facilityId,
		@Param("dateTimeFrom") LocalDateTime dateTimeFrom, @Param("dateTimeTo") LocalDateTime dateTimeTo);
}
