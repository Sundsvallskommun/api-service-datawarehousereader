package se.sundsvall.datawarehousereader.integration.stadsbacken;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingDayEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;

/**
 * MeasurementDistrictHeatingDay repository tests.
 * 
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
class MeasurementDistrictHeatingDayRepositoryTest {

	@Autowired
	private MeasurementDistrictHeatingDayRepository repository;

	@Test
	void testResponseForMeasurementOfOneDay() {
		final var customerOrgNbr = "5566661234";
		final var facilityId = "9261219043";
		final var dateTimeFrom = LocalDate.of(2022, 3, 23).atStartOfDay();
		final var dateTimeTo = LocalDate.of(2022, 3, 23).atStartOfDay();

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getContent())
			.hasSize(1)
			.extracting(
				MeasurementDistrictHeatingDayEntity::getCustomerOrgId,
				MeasurementDistrictHeatingDayEntity::getFacilityId,
				MeasurementDistrictHeatingDayEntity::getFeedType,
				MeasurementDistrictHeatingDayEntity::getInterpolation,
				MeasurementDistrictHeatingDayEntity::getMeasurementTimestamp,
				MeasurementDistrictHeatingDayEntity::getReadingSequence,
				MeasurementDistrictHeatingDayEntity::getUnit,
				MeasurementDistrictHeatingDayEntity::getUsage,
				MeasurementDistrictHeatingDayEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "energy", 0, dateTimeFrom, 158221639, "kWh", toBigDecimal(1393), null));
	}

	@Test
	void testResponseSortedAscendingOnMeasurementTimestamp() {
		final var customerOrgNbr = "5566661234";
		final var facilityId = "9261219043";
		final var dateTimeFrom = LocalDate.of(2022, 3, 20).atStartOfDay();
		final var dateTimeTo = LocalDate.of(2022, 3, 23).atStartOfDay();

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100).withSort(by(ASC, "measurementTimestamp")));

		assertThat(page.getContent())
			.hasSize(4)
			.extracting(
				MeasurementDistrictHeatingDayEntity::getCustomerOrgId,
				MeasurementDistrictHeatingDayEntity::getFacilityId,
				MeasurementDistrictHeatingDayEntity::getFeedType,
				MeasurementDistrictHeatingDayEntity::getInterpolation,
				MeasurementDistrictHeatingDayEntity::getMeasurementTimestamp,
				MeasurementDistrictHeatingDayEntity::getReadingSequence,
				MeasurementDistrictHeatingDayEntity::getUnit,
				MeasurementDistrictHeatingDayEntity::getUsage,
				MeasurementDistrictHeatingDayEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "energy", 0, LocalDate.of(2022, 3, 20).atStartOfDay(), 147614734, "kWh", toBigDecimal(1647), null),
				tuple(customerOrgNbr, facilityId, "energy", 0, LocalDate.of(2022, 3, 21).atStartOfDay(), 154331012, "kWh", toBigDecimal(1802), null),
				tuple(customerOrgNbr, facilityId, "energy", 0, LocalDate.of(2022, 3, 22).atStartOfDay(), 147094392, "kWh", toBigDecimal(1869), null),
				tuple(customerOrgNbr, facilityId, "energy", 0, LocalDate.of(2022, 3, 23).atStartOfDay(), 158221639, "kWh", toBigDecimal(1393), null));
	}

	@Test
	void testResponseSortedDescendingOnMeasurementTimestamp() {
		final var customerOrgNbr = "5566661234";
		final var facilityId = "9261219043";
		final var dateTimeFrom = LocalDate.of(2022, 3, 20).atStartOfDay();
		final var dateTimeTo = LocalDate.of(2022, 3, 23).atStartOfDay();

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(0, 100).withSort(by(DESC, "measurementTimestamp")));
		assertThat(page.getContent())
			.hasSize(4)
			.extracting(
				MeasurementDistrictHeatingDayEntity::getCustomerOrgId,
				MeasurementDistrictHeatingDayEntity::getFacilityId,
				MeasurementDistrictHeatingDayEntity::getFeedType,
				MeasurementDistrictHeatingDayEntity::getInterpolation,
				MeasurementDistrictHeatingDayEntity::getMeasurementTimestamp,
				MeasurementDistrictHeatingDayEntity::getReadingSequence,
				MeasurementDistrictHeatingDayEntity::getUnit,
				MeasurementDistrictHeatingDayEntity::getUsage,
				MeasurementDistrictHeatingDayEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "energy", 0, LocalDate.of(2022, 3, 23).atStartOfDay(), 158221639, "kWh", toBigDecimal(1393), null),
				tuple(customerOrgNbr, facilityId, "energy", 0, LocalDate.of(2022, 3, 22).atStartOfDay(), 147094392, "kWh", toBigDecimal(1869), null),
				tuple(customerOrgNbr, facilityId, "energy", 0, LocalDate.of(2022, 3, 21).atStartOfDay(), 154331012, "kWh", toBigDecimal(1802), null),
				tuple(customerOrgNbr, facilityId, "energy", 0, LocalDate.of(2022, 3, 20).atStartOfDay(), 147614734, "kWh", toBigDecimal(1647), null));

	}

	@Test
	void testPagingOfResponse() {
		final var customerOrgNbr = "5563231234";
		final var facilityId = "9112204012";
		final var dateTimeFrom = LocalDate.of(2019, 4, 1).atStartOfDay();
		final var dateTimeTo = LocalDate.of(2019, 4, 4).atStartOfDay();

		final var page = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo, PageRequest.of(2, 1).withSort(by(ASC, "measurementTimestamp")));

		assertThat(page.getNumber()).isEqualTo(2);
		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getTotalPages()).isEqualTo(4);
		assertThat(page.getTotalElements()).isEqualTo(4);
		assertThat(page.getContent())
			.hasSize(1)
			.extracting(
				MeasurementDistrictHeatingDayEntity::getCustomerOrgId,
				MeasurementDistrictHeatingDayEntity::getFacilityId,
				MeasurementDistrictHeatingDayEntity::getFeedType,
				MeasurementDistrictHeatingDayEntity::getInterpolation,
				MeasurementDistrictHeatingDayEntity::getMeasurementTimestamp,
				MeasurementDistrictHeatingDayEntity::getReadingSequence,
				MeasurementDistrictHeatingDayEntity::getUnit,
				MeasurementDistrictHeatingDayEntity::getUsage,
				MeasurementDistrictHeatingDayEntity::getUuid)
			.containsExactly(
				tuple(customerOrgNbr, facilityId, "energy", 0, LocalDate.of(2019, 4, 3).atStartOfDay(), 82281543, "kWh", toBigDecimal(0), null));
	}

	@Test
	void testResponseWithHitsBeforeDate() {
		final var dateTimeTo = LocalDate.of(2017, 8, 1).atStartOfDay();

		final var page = repository.findAllMatching(null, null, null, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isEqualTo(1);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isEqualTo(1);
		assertThat(page.getTotalPages()).isEqualTo(1);
	}

	@Test
	void testResponseWithNoHitsBeforeDate() {
		final var dateTimeTo = LocalDate.of(2013, 8, 31).atStartOfDay();

		final var page = repository.findAllMatching(null, null, null, dateTimeTo, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isZero();
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isZero();
		assertThat(page.getTotalPages()).isZero();
	}

	@Test
	void testResponseWithHitsAfterDate() {
		final var dateTimeFrom = LocalDate.of(2022, 3, 28).atStartOfDay();

		final var page = repository.findAllMatching(null, null, dateTimeFrom, null, PageRequest.of(0, 100));

		assertThat(page.getNumberOfElements()).isEqualTo(3);
		assertThat(page.getNumber()).isZero();
		assertThat(page.getTotalElements()).isEqualTo(3);
		assertThat(page.getTotalPages()).isEqualTo(1);
	}

	@Test
	void testResponseWithNoHitsAfterDate() {
		final var dateTimeFrom = LocalDate.of(2022, 3, 31).atStartOfDay();

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
		assertThat(page.getTotalElements()).isEqualTo(209);
		assertThat(page.getTotalPages()).isEqualTo(3); // A total of 209 entries divided in 100 equals 3 pages
	}

	private static BigDecimal toBigDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(10);
	}

}
