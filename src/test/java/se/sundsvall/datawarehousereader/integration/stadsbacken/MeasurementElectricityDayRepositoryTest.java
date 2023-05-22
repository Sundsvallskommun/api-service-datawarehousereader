package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityDayEntity;

/**
 * MeasurementElectricityDay repository tests.
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
class MeasurementElectricityDayRepositoryTest {

	@Autowired
	private MeasurementElectricityDayRepository repository;

	@Test
	void testResponseMeasurementsForOneDay() {
		final var customerOrgNbr = "197003201234";
		final var facilityId = "735999109212613018";
		final var dateTimeFrom = LocalDate.of(2022, 7, 1).atStartOfDay();
		final var dateTimeTo = LocalDate.of(2022, 7, 1).atStartOfDay();

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getContent())
			.hasSize(5)
			.extracting(
				MeasurementElectricityDayEntity::getCustomerOrgId,
				MeasurementElectricityDayEntity::getFacilityId,
				MeasurementElectricityDayEntity::getFeedType,
				MeasurementElectricityDayEntity::getInterpolation,
				MeasurementElectricityDayEntity::getMeasurementTimestamp,
				MeasurementElectricityDayEntity::getUnit,
				MeasurementElectricityDayEntity::getUsage,
				MeasurementElectricityDayEntity::getUuid)
			.containsExactlyInAnyOrder(
				tuple(customerOrgNbr, facilityId, "Energy", 0, dateTimeFrom, "kWh", toBigDecimal(37.58), "6ABF14C3-BD77-467C-8560-1B191FFD993E"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, dateTimeFrom, "kWh", toBigDecimal(40.37), "6ABF14C3-BD77-467C-8560-1B191FFD993E"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, dateTimeFrom, "kWh", toBigDecimal(44.33), "6ABF14C3-BD77-467C-8560-1B191FFD993E"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, dateTimeFrom, "kWh", toBigDecimal(45.21), "6ABF14C3-BD77-467C-8560-1B191FFD993E"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, dateTimeFrom, "kWh", toBigDecimal(121.05), "6ABF14C3-BD77-467C-8560-1B191FFD993E"));
	}

	@Test
	void testResponseSortedAscendingOnMeasurementTimestamp() {
		final var customerOrgNbr = "197401061234";
		final var facilityId = "735999109148803019";
		final var dateTimeFrom = LocalDate.of(2020, 9, 2).atStartOfDay();
		final var dateTimeTo = LocalDate.of(2020, 9, 16).atStartOfDay();

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100).withSort(by(ASC, "measurementTimestamp")));

		assertThat(page.getContent())
			.hasSize(9)
			.extracting(
				MeasurementElectricityDayEntity::getCustomerOrgId,
				MeasurementElectricityDayEntity::getFacilityId,
				MeasurementElectricityDayEntity::getFeedType,
				MeasurementElectricityDayEntity::getInterpolation,
				MeasurementElectricityDayEntity::getMeasurementTimestamp,
				MeasurementElectricityDayEntity::getUnit,
				MeasurementElectricityDayEntity::getUsage,
				MeasurementElectricityDayEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 2).atStartOfDay(), "kWh", toBigDecimal(21.96), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 3).atStartOfDay(), "kWh", toBigDecimal(13.15), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 3).atStartOfDay(), "kWh", toBigDecimal(50.42), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 23, LocalDate.of(2020, 9, 4).atStartOfDay(), "kWh", toBigDecimal(41.676), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 4).atStartOfDay(), "kWh", toBigDecimal(42.66), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 5).atStartOfDay(), "kWh", toBigDecimal(25.21), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 5).atStartOfDay(), "kWh", toBigDecimal(36.36), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 6).atStartOfDay(), "kWh", toBigDecimal(18.81), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 9).atStartOfDay(), "kWh", toBigDecimal(20.63), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"));
	}

	@Test
	void testResponseSortedDescendingOnMeasurementTimestamp() {
		final var customerOrgNbr = "197401061234";
		final var facilityId = "735999109148803019";
		final var dateTimeFrom = LocalDate.of(2020, 9, 2).atStartOfDay();
		final var dateTimeTo = LocalDate.of(2020, 9, 16).atStartOfDay();

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100).withSort(by(DESC, "measurementTimestamp")));

		assertThat(page.getContent())
			.hasSize(9)
			.extracting(
				MeasurementElectricityDayEntity::getCustomerOrgId,
				MeasurementElectricityDayEntity::getFacilityId,
				MeasurementElectricityDayEntity::getFeedType,
				MeasurementElectricityDayEntity::getInterpolation,
				MeasurementElectricityDayEntity::getMeasurementTimestamp,
				MeasurementElectricityDayEntity::getUnit,
				MeasurementElectricityDayEntity::getUsage,
				MeasurementElectricityDayEntity::getUuid)
			.containsExactly(tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 9).atStartOfDay(), "kWh", toBigDecimal(20.63), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 6).atStartOfDay(), "kWh", toBigDecimal(18.81), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 5).atStartOfDay(), "kWh", toBigDecimal(25.21), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 5).atStartOfDay(), "kWh", toBigDecimal(36.36), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 4).atStartOfDay(), "kWh", toBigDecimal(42.66), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 23, LocalDate.of(2020, 9, 4).atStartOfDay(), "kWh", toBigDecimal(41.676), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 3).atStartOfDay(), "kWh", toBigDecimal(50.42), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 3).atStartOfDay(), "kWh", toBigDecimal(13.15), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2020, 9, 2).atStartOfDay(), "kWh", toBigDecimal(21.96), "3B3F9EF0-FDF6-4DBB-AB6D-AFA41BF76685"));

	}

	@Test
	void testPagingOfResponse() {
		final var customerOrgNbr = "197003201111";
		final var facilityId = "735999109212613018";
		final var dateTimeFrom = LocalDate.of(2021, 1, 28).atStartOfDay();
		final var dateTimeTo = LocalDate.of(2021, 2, 3).atStartOfDay();

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(1, 2).withSort(by(ASC, "measurementTimestamp")));

		assertThat(page.getNumber()).isEqualTo(1);
		assertThat(page.getNumberOfElements()).isEqualTo(2);
		assertThat(page.getTotalPages()).isEqualTo(3);
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getContent())
			.hasSize(2)
			.extracting(
				MeasurementElectricityDayEntity::getCustomerOrgId,
				MeasurementElectricityDayEntity::getFacilityId,
				MeasurementElectricityDayEntity::getFeedType,
				MeasurementElectricityDayEntity::getInterpolation,
				MeasurementElectricityDayEntity::getMeasurementTimestamp,
				MeasurementElectricityDayEntity::getUnit,
				MeasurementElectricityDayEntity::getUsage,
				MeasurementElectricityDayEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2021, 1, 31).atStartOfDay(), "kWh", toBigDecimal(27.46), "6ABF14C3-BD77-467C-8560-1B191FFD993E"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDate.of(2021, 2, 1).atStartOfDay(), "kWh", toBigDecimal(67.34), "6ABF14C3-BD77-467C-8560-1B191FFD993E"));
	}

	@Test
	void testResponseWithHitsBeforeDate() {
		final var dateTimeTo = LocalDate.of(2017, 1, 1).atStartOfDay();

		final var page = repository.findAllMatching(null, null, null, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isEqualTo(14);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isEqualTo(14);
		assertThat(page.getTotalPages()).isEqualTo(1);
	}

	@Test
	void testResponseWithNoHitsBeforeDate() {
		final var dateTimeTo = LocalDate.of(2014, 1, 4).atStartOfDay();

		final var page = repository.findAllMatching(null, null, null, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isZero();
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isZero();
		assertThat(page.getTotalPages()).isZero();
	}

	@Test
	void testResponseWithHitsAfterDate() {
		final var dateTimeFrom = LocalDate.of(2022, 9, 19).atStartOfDay();

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
		assertThat(page.getTotalElements()).isEqualTo(204);
		assertThat(page.getTotalPages()).isEqualTo(3); // A total of 204 entries divided in slices of 100 equals 3 pages
	}

	private static BigDecimal toBigDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(10);
	}
}
