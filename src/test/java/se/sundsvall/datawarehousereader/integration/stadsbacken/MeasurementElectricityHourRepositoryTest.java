package se.sundsvall.datawarehousereader.integration.stadsbacken;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityHourEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;

/**
 * MeasurementElectricityHour repository tests.
 * 
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@SpringBootTest
@ActiveProfiles("junit")
class MeasurementElectricityHourRepositoryTest {

	@Autowired
	private MeasurementElectricityHourRepository repository;

	@Test
	void testResponseMeasurementsForOneHour() {
		final var customerOrgNbr = "195211161234";
		final var facilityId = "735999109320425015";
		final var dateTimeFrom = LocalDateTime.of(2022, 4, 27,  21, 0);
		final var dateTimeTo = LocalDateTime.of(2022, 4, 27,  21, 0);

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getContent())
			.hasSize(1)
			.extracting(
				MeasurementElectricityHourEntity::getCustomerOrgId,
				MeasurementElectricityHourEntity::getFacilityId,
				MeasurementElectricityHourEntity::getFeedType,
				MeasurementElectricityHourEntity::getInterpolation,
				MeasurementElectricityHourEntity::getMeasurementTimestamp,
				MeasurementElectricityHourEntity::getUnit,
				MeasurementElectricityHourEntity::getUsage,
				MeasurementElectricityHourEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "Energy", 0, dateTimeFrom, "kWh", toBigDecimal(0.05), "62743983-9C08-4CB4-A7F6-1DAAE3889733"));
	}

	@Test
	void testResponseSortedAscendingOnMeasurementTimestamp() {
		final var customerOrgNbr = "195211161234";
		final var facilityId = "735999109320425015";
		final var dateTimeFrom = LocalDateTime.of(2022, 4, 27, 16, 0);
		final var dateTimeTo = LocalDateTime.of(2022, 4, 27, 19, 0);

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100).withSort(by(ASC, "measurementTimestamp")));

		assertThat(page.getContent())
			.hasSize(4)
			.extracting(
				MeasurementElectricityHourEntity::getCustomerOrgId,
				MeasurementElectricityHourEntity::getFacilityId,
				MeasurementElectricityHourEntity::getFeedType,
				MeasurementElectricityHourEntity::getInterpolation,
				MeasurementElectricityHourEntity::getMeasurementTimestamp,
				MeasurementElectricityHourEntity::getUnit,
				MeasurementElectricityHourEntity::getUsage,
				MeasurementElectricityHourEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 27,16,0), "kWh", toBigDecimal(0.04), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 27,17,0), "kWh", toBigDecimal(0.04), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 27,18,0), "kWh", toBigDecimal(0.03), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 27,19,0), "kWh", toBigDecimal(0.05), "62743983-9C08-4CB4-A7F6-1DAAE3889733"));
	}

	@Test
	void testResponseSortedDescendingOnMeasurementTimestamp() {
		final var customerOrgNbr = "195211161234";
		final var facilityId = "735999109320425015";
		final var dateTimeFrom = LocalDateTime.of(2022, 4, 27, 16, 0);
		final var dateTimeTo = LocalDateTime.of(2022, 4, 27, 19, 0);

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100).withSort(by(DESC, "measurementTimestamp")));

		assertThat(page.getContent())
			.hasSize(4)
			.extracting(
				MeasurementElectricityHourEntity::getCustomerOrgId,
				MeasurementElectricityHourEntity::getFacilityId,
				MeasurementElectricityHourEntity::getFeedType,
				MeasurementElectricityHourEntity::getInterpolation,
				MeasurementElectricityHourEntity::getMeasurementTimestamp,
				MeasurementElectricityHourEntity::getUnit,
				MeasurementElectricityHourEntity::getUsage,
				MeasurementElectricityHourEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 27,19,0), "kWh", toBigDecimal(0.05), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 27,18,0), "kWh", toBigDecimal(0.03), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 27,17,0), "kWh", toBigDecimal(0.04), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 27,16,0), "kWh", toBigDecimal(0.04), "62743983-9C08-4CB4-A7F6-1DAAE3889733"));

	}

	@Test
	void testPagingOfResponse() {
		final var customerOrgNbr = "195211161234";
		final var facilityId = "735999109320425015";
		final var dateTimeFrom = LocalDateTime.of(2022, 4, 27, 19,0);
		final var dateTimeTo = LocalDateTime.of(2022, 4, 28, 8,0);

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(1, 2).withSort(by(ASC, "measurementTimestamp")));

		assertThat(page.getNumber()).isEqualTo(1);
		assertThat(page.getNumberOfElements()).isEqualTo(2);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.getTotalElements()).isEqualTo(4);
		assertThat(page.getContent())
			.hasSize(2)

			.extracting(
				MeasurementElectricityHourEntity::getCustomerOrgId,
				MeasurementElectricityHourEntity::getFacilityId,
				MeasurementElectricityHourEntity::getFeedType,
				MeasurementElectricityHourEntity::getInterpolation,
				MeasurementElectricityHourEntity::getMeasurementTimestamp,
				MeasurementElectricityHourEntity::getUnit,
				MeasurementElectricityHourEntity::getUsage,
				MeasurementElectricityHourEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 27, 21, 0), "kWh", toBigDecimal(0.05), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 28, 0, 0), "kWh", toBigDecimal(0.04), "62743983-9C08-4CB4-A7F6-1DAAE3889733"));
	}

	@Test
	void testResponseWithHitsBeforeDate() {
		final var dateTimeTo = LocalDateTime.of(2022, 4, 9, 8, 0);

		final var page = repository.findAllMatching(null, null, null, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isEqualTo(3);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isEqualTo(3);
		assertThat(page.getTotalPages()).isEqualTo(1);
	}

	@Test
	void testResponseWithNoHitsBeforeDate() {
		final var dateTimeTo = LocalDateTime.of(2020, 9, 3, 0, 0);

		final var page = repository.findAllMatching(null, null, null, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isZero();
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isZero();
		assertThat(page.getTotalPages()).isZero();
	}

	@Test
	void testResponseWithHitsAfterDate() {
		final var dateTimeFrom = LocalDateTime.of(2022, 4, 27, 23, 59);

		final var page = repository.findAllMatching(null, null, dateTimeFrom, null, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getTotalPages()).isEqualTo(1);
	}

	@Test
	void testResponseWithNoHitsAfterDate() {
		final var dateTimeFrom = LocalDate.of(2022, 9, 21).atStartOfDay();

		final var page = repository.findAllMatching(null, null, dateTimeFrom, null, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isZero();
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isZero();
		assertThat(page.getTotalPages()).isZero();
	}

	@Test
	void testResponseWithNoFiltering() {
		final var page = repository.findAllMatching(null, null, null, null, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isEqualTo(100);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isEqualTo(180);
		assertThat(page.getTotalPages()).isEqualTo(2); // A total of 180 entries divided in slices of 100 equals 2 pages
	}

	private static BigDecimal toBigDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(10);
	}
}
