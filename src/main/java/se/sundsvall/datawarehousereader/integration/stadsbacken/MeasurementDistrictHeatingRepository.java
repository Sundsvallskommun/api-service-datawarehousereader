package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingKey;

@Transactional(readOnly = true)
@CircuitBreaker(name = "measurementDistrictHeatingRepository")
public interface MeasurementDistrictHeatingRepository extends JpaRepository<MeasurementDistrictHeatingEntity, MeasurementDistrictHeatingKey> {
	@Query(
		value = "exec kundinfo.spMeasurementDistrictHeating :customerOrgId, :facilityId, :dateTimeFrom, :dateTimeTo, :aggregationLevel",
		nativeQuery = true)
	List<MeasurementDistrictHeatingEntity> findAllMatching(
		@Param("customerOrgId") String customerOrgId,
		@Param("facilityId") String facilityId,
		@Param("dateTimeFrom") LocalDateTime dateTimeFrom,
		@Param("dateTimeTo") LocalDateTime dateTimeTo,
		@Param("aggregationLevel") String aggregationLevel);
}
