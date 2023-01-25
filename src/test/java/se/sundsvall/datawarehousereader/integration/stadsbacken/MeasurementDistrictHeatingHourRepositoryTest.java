package se.sundsvall.datawarehousereader.integration.stadsbacken;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingHourEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;

/**
 * MeasurementDistrictHeatingHour repository tests.
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@SpringBootTest
@ActiveProfiles("junit")
class MeasurementDistrictHeatingHourRepositoryTest {

	@Autowired
	private MeasurementDistrictHeatingHourRepository repository;

	@Test
	void testResponseForMeasurementOfOneHour() {
		final var customerOrgNbr = "5591561234";
		final var facilityId = "9115803075";
		final var dateTimeFrom = LocalDateTime.of(2022, 1, 1, 22, 0, 0)	;
		final var dateTimeTo = LocalDateTime.of(2022, 1, 1, 22,0,0);

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getContent())
			.hasSize(1)
			.extracting(
				MeasurementDistrictHeatingHourEntity::getCustomerOrgId,
				MeasurementDistrictHeatingHourEntity::getFacilityId,
				MeasurementDistrictHeatingHourEntity::getFeedType,
				MeasurementDistrictHeatingHourEntity::getInterpolation,
				MeasurementDistrictHeatingHourEntity::getMeasurementTimestamp,
				MeasurementDistrictHeatingHourEntity::getReadingSequence,
				MeasurementDistrictHeatingHourEntity::getUnit,
				MeasurementDistrictHeatingHourEntity::getUsage,
				MeasurementDistrictHeatingHourEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "Aktiv", 0, dateTimeFrom, 147129828, "kWh", toBigDecimal(7913.23), "A0B52C5B-93AC-480B-821D-E238C8F4D952"));
	}

	@Test
	void testResponseSortedAscendingOnMeasurementTimestamp() {
		final var customerOrgNbr = "5591561234";
		final var facilityId = "9115803075";
		final var dateTimeFrom = LocalDateTime.of(2022, 1, 2, 3, 0, 0);
		final var dateTimeTo = LocalDateTime.of(2022, 1, 2, 6, 0, 0);

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100).withSort(by(ASC, "measurementTimestamp")));

		assertThat(page.getContent())
			.hasSize(4)
			.extracting(
				MeasurementDistrictHeatingHourEntity::getCustomerOrgId,
				MeasurementDistrictHeatingHourEntity::getFacilityId,
				MeasurementDistrictHeatingHourEntity::getFeedType,
				MeasurementDistrictHeatingHourEntity::getInterpolation,
				MeasurementDistrictHeatingHourEntity::getMeasurementTimestamp,
				MeasurementDistrictHeatingHourEntity::getReadingSequence,
				MeasurementDistrictHeatingHourEntity::getUnit,
				MeasurementDistrictHeatingHourEntity::getUsage,
				MeasurementDistrictHeatingHourEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "Aktiv", 0, LocalDateTime.of(2022, 1, 2,3,0), 161995441, "kWh", toBigDecimal(7913.97), "A0B52C5B-93AC-480B-821D-E238C8F4D952"),
				tuple(customerOrgNbr, facilityId, "Aktiv", 0, LocalDateTime.of(2022, 1, 2,4,0), 161995441, "kWh", toBigDecimal(7914.12), "A0B52C5B-93AC-480B-821D-E238C8F4D952"),
				tuple(customerOrgNbr, facilityId, "Aktiv", 0, LocalDateTime.of(2022, 1, 2,5,0), 161995441, "kWh", toBigDecimal(7914.25), "A0B52C5B-93AC-480B-821D-E238C8F4D952"),
				tuple(customerOrgNbr, facilityId, "Aktiv", 0, LocalDateTime.of(2022, 1, 2,6,0), 161995441, "kWh", toBigDecimal(7914.39), "A0B52C5B-93AC-480B-821D-E238C8F4D952"));
	}

	@Test
	void testResponseSortedDescendingOnMeasurementTimestamp() {
		final var customerOrgNbr = "5591561234";
		final var facilityId = "9115803075";
		final var dateTimeFrom = LocalDateTime.of(2022, 1, 2, 3,0);
		final var dateTimeTo = LocalDateTime.of(2022, 1, 2, 6, 0);

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100).withSort(by(DESC, "measurementTimestamp")));
		assertThat(page.getContent())
			.hasSize(4)
			.extracting(
				MeasurementDistrictHeatingHourEntity::getCustomerOrgId,
				MeasurementDistrictHeatingHourEntity::getFacilityId,
				MeasurementDistrictHeatingHourEntity::getFeedType,
				MeasurementDistrictHeatingHourEntity::getInterpolation,
				MeasurementDistrictHeatingHourEntity::getMeasurementTimestamp,
				MeasurementDistrictHeatingHourEntity::getReadingSequence,
				MeasurementDistrictHeatingHourEntity::getUnit,
				MeasurementDistrictHeatingHourEntity::getUsage,
				MeasurementDistrictHeatingHourEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "Aktiv", 0, LocalDateTime.of(2022, 1, 2,6,0), 161995441, "kWh", toBigDecimal(7914.39), "A0B52C5B-93AC-480B-821D-E238C8F4D952"),
				tuple(customerOrgNbr, facilityId, "Aktiv", 0, LocalDateTime.of(2022, 1, 2,5,0), 161995441, "kWh", toBigDecimal(7914.25), "A0B52C5B-93AC-480B-821D-E238C8F4D952"),
				tuple(customerOrgNbr, facilityId, "Aktiv", 0, LocalDateTime.of(2022, 1, 2,4,0), 161995441, "kWh", toBigDecimal(7914.12), "A0B52C5B-93AC-480B-821D-E238C8F4D952"),
				tuple(customerOrgNbr, facilityId, "Aktiv", 0, LocalDateTime.of(2022, 1, 2,3,0), 161995441, "kWh", toBigDecimal(7913.97), "A0B52C5B-93AC-480B-821D-E238C8F4D952"));

	}

	@Test
	void testPagingOfResponse() {
		final var customerOrgNbr = "5591561234";
		final var facilityId = "9115803075";
		final var dateTimeFrom = LocalDateTime.of(2022, 1, 1, 7, 0);
		final var dateTimeTo = LocalDateTime.of(2022, 1, 1, 10, 0);

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(2, 1).withSort(by(ASC, "measurementTimestamp")));

		assertThat(page.getNumber()).isEqualTo(2);
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getTotalPages()).isEqualTo(4);
		assertThat(page.getTotalElements()).isEqualTo(4);
		assertThat(page.getContent())
			.hasSize(1)
			.extracting(
				MeasurementDistrictHeatingHourEntity::getCustomerOrgId,
				MeasurementDistrictHeatingHourEntity::getFacilityId,
				MeasurementDistrictHeatingHourEntity::getFeedType,
				MeasurementDistrictHeatingHourEntity::getInterpolation,
				MeasurementDistrictHeatingHourEntity::getMeasurementTimestamp,
				MeasurementDistrictHeatingHourEntity::getReadingSequence,
				MeasurementDistrictHeatingHourEntity::getUnit,
				MeasurementDistrictHeatingHourEntity::getUsage,
				MeasurementDistrictHeatingHourEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "Aktiv", 0, LocalDateTime.of(2022, 1, 1, 9,0), 147129828, "kWh", toBigDecimal(7911.17), "A0B52C5B-93AC-480B-821D-E238C8F4D952"));
	}

	@Test
	void testResponseWithHitsBeforeDateTime() {
		final var dateTimeTo = LocalDateTime.of(2022, 1, 1, 1, 0, 0);

		final var page = repository.findAllMatching(null, null, null, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isEqualTo(11);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isEqualTo(11);
		assertThat(page.getTotalPages()).isEqualTo(1);
	}

	@Test
	void testResponseWithNoHitsBeforeDateTime() {
		final var dateTimeTo = LocalDateTime.of(2022, 1, 1, 0, 0, 0);

		final var page = repository.findAllMatching(null, null, null, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isZero();
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isZero();
		assertThat(page.getTotalPages()).isZero();
	}

	@Test
	void testResponseWithHitsAfterDateTime() {
		final var dateTimeFrom = LocalDateTime.of(2022, 1, 2, 23, 59, 59);

		final var page = repository.findAllMatching(null, null, dateTimeFrom, null, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isEqualTo(10);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isEqualTo(10);
		assertThat(page.getTotalPages()).isEqualTo(1);
	}

	@Test
	void testResponseWithNoHitsAfterDateTime() {
		final var dateTimeFrom = LocalDateTime.of(2022, 1, 3, 1, 0, 1);

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
		assertThat(page.getTotalElements()).isEqualTo(420);
		assertThat(page.getTotalPages()).isEqualTo(5); // A total of 420 entries divided in 100 equals 5 pages
	}

	private static BigDecimal toBigDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(10);
	}

}
