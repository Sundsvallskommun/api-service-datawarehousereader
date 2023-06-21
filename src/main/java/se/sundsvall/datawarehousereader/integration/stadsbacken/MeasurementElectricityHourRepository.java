package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityHourEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityKey;

@Transactional(readOnly = true)
@CircuitBreaker(name = "measurementElectricityHourRepository")
public interface MeasurementElectricityHourRepository extends PagingAndSortingRepository<MeasurementElectricityHourEntity, MeasurementElectricityKey>, JpaSpecificationExecutor<MeasurementElectricityHourEntity> {

	@Query(value = "exec kundinfo.spMeasurementElectricityHour :customerOrgNumber, :facilityId, :dateTimeFrom, :dateTimeTo", nativeQuery = true)
	List<MeasurementElectricityHourEntity> findAllMatching(@Param("customerOrgNumber") String customerOrgNumber, @Param("facilityId") String facilityId,
		@Param("dateTimeFrom") LocalDateTime dateTimeFrom, @Param("dateTimeTo") LocalDateTime dateTimeTo);
}
