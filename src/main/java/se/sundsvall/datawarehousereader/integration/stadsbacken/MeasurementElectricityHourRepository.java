package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityHourEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityKey;

@Transactional
@CircuitBreaker(name = "measurementElectricityHourRepository")
public interface MeasurementElectricityHourRepository 
	extends PagingAndSortingRepository<MeasurementElectricityHourEntity, MeasurementElectricityKey>, JpaSpecificationExecutor<MeasurementElectricityHourEntity> {
	
	@Query(value = "exec kundinfo.spMeasurementElectricityHour :customerorgid, :anlaggningsID, :datum_start, :datum_stop ;", nativeQuery = true)
	List<MeasurementElectricityHourEntity> findAllMatching(@Param("customerorgid") String customerOrgNumber, @Param("anlaggningsID") String facilityId,
		@Param("datum_start") LocalDateTime dateTimeFrom, @Param("datum_stop") LocalDateTime dateTimeTo);
}
