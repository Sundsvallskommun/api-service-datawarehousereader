package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityHourEntity;

/**
 * MeasurementElectricityHour repository tests.
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
@Transactional
class MeasurementElectricityHourRepositoryTest {

	@Autowired
	private MeasurementElectricityHourRepository repository;

	@Test
	void testResponseMeasurementsForOneHour() {
		final var customerOrgNbr = "195211161234";
		final var facilityId = "735999109320425015";
		final var dateTimeFrom = LocalDateTime.of(2022, 4, 27, 21, 0);
		final var dateTimeTo = LocalDateTime.of(2022, 4, 27, 21, 0);

		final var list = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo);

		assertThat(list)
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
	void testResponseForOneDay() {
		final var customerOrgNbr = "195211161234";
		final var facilityId = "735999109320425015";
		final var dateTimeFrom = LocalDateTime.of(2022, 4, 10, 0, 0);
		final var dateTimeTo = LocalDateTime.of(2022, 4, 10, 23, 59);

		final var list = repository.findAllMatching(customerOrgNbr, facilityId, dateTimeFrom, dateTimeTo);

		assertThat(list)
			.hasSize(24)
			.extracting(
				MeasurementElectricityHourEntity::getCustomerOrgId,
				MeasurementElectricityHourEntity::getFacilityId,
				MeasurementElectricityHourEntity::getFeedType,
				MeasurementElectricityHourEntity::getInterpolation,
				MeasurementElectricityHourEntity::getMeasurementTimestamp,
				MeasurementElectricityHourEntity::getUnit,
				MeasurementElectricityHourEntity::getUsage,
				MeasurementElectricityHourEntity::getUuid)
			.containsExactlyInAnyOrder(
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 0, 0), "kWh", toBigDecimal(0.03), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 1, 0), "kWh", toBigDecimal(0.05), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 2, 0), "kWh", toBigDecimal(0.03), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 3, 0), "kWh", toBigDecimal(0.05), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 4, 0), "kWh", toBigDecimal(0.04), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 5, 0), "kWh", toBigDecimal(0.04), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 6, 0), "kWh", toBigDecimal(0.04), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 7, 0), "kWh", toBigDecimal(0.06), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 8, 0), "kWh", toBigDecimal(0.02), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 9, 0), "kWh", toBigDecimal(0.04), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 10, 0), "kWh", toBigDecimal(0.03), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 11, 0), "kWh", toBigDecimal(0.01), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 12, 0), "kWh", toBigDecimal(0.01), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 13, 0), "kWh", toBigDecimal(0.02), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 14, 0), "kWh", toBigDecimal(0.00), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 15, 0), "kWh", toBigDecimal(0.01), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 16, 0), "kWh", toBigDecimal(0.01), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 17, 0), "kWh", toBigDecimal(0.02), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 18, 0), "kWh", toBigDecimal(0.01), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 19, 0), "kWh", toBigDecimal(0.03), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 20, 0), "kWh", toBigDecimal(0.01), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 21, 0), "kWh", toBigDecimal(0.03), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 22, 0), "kWh", toBigDecimal(0.06), "62743983-9C08-4CB4-A7F6-1DAAE3889733"),
				tuple(customerOrgNbr, facilityId, "Energy", 0, LocalDateTime.of(2022, 4, 10, 23, 0), "kWh", toBigDecimal(0.00), "62743983-9C08-4CB4-A7F6-1DAAE3889733"));
	}

	@Test
	void testResponseWithHitsBeforeDate() {
		final var dateTimeTo = LocalDateTime.of(2022, 4, 9, 8, 0);

		final var list = repository.findAllMatching(null, null, null, dateTimeTo);

		assertThat(list).isNotNull().hasSize(3);
	}

	@Test
	void testResponseWithNoHitsBeforeDate() {
		final var dateTimeTo = LocalDateTime.of(2020, 9, 3, 0, 0);

		final var list = repository.findAllMatching(null, null, null, dateTimeTo);

		assertThat(list).isEmpty();
	}

	@Test
	void testResponseWithHitsAfterDate() {
		final var dateTimeFrom = LocalDateTime.of(2022, 4, 27, 23, 59);

		final var list = repository.findAllMatching(null, null, dateTimeFrom, null);

		assertThat(list).isNotNull().hasSize(1);
	}

	@Test
	void testResponseWithNoHitsAfterDate() {
		final var dateTimeFrom = LocalDate.of(2022, 9, 21).atStartOfDay();

		final var list = repository.findAllMatching(null, null, dateTimeFrom, null);

		assertThat(list).isEmpty();
	}

	@Test
	void testResponseWithNoFiltering() {
		final var list = repository.findAllMatching(null, null, null, null);

		assertThat(list).isNotNull().hasSize(180);
	}

	private static BigDecimal toBigDecimal(double value) {
		return BigDecimal.valueOf(value).setScale(10);
	}
}
