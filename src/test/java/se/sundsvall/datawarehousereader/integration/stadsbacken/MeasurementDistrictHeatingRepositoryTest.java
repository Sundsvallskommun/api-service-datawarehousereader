package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * MeasurementDistrictHeating repository tests.
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
@Transactional
class MeasurementDistrictHeatingRepositoryTest {

	@Autowired
	private MeasurementDistrictHeatingRepository repository;

	@Test
	void testFindAllMatchingWithAllParameters() {
		final var customerOrgId = "5591561234";
		final var facilityId = "9115801178";
		final var dateTimeFrom = LocalDateTime.of(2022, 1, 1, 1, 0);
		final var dateTimeTo = LocalDateTime.of(2022, 1, 1, 5, 0);
		final var aggregationLevel = "HOUR";

		final var result = repository.findAllMatching(
			customerOrgId,
			facilityId,
			dateTimeFrom,
			dateTimeTo,
			aggregationLevel);

		assertThat(result).hasSize(15);
	}

	@Test
	void testFindAllMatchingWithNullParameters() {
		final var result = repository.findAllMatching(
			null,
			null,
			null,
			null,
			null);

		assertThat(result).isNotNull().isNotEmpty();
	}

	@Test
	void testFindAllMatchingWithDateRange() {
		final var dateTimeFrom = LocalDateTime.of(2022, 1, 1, 0, 0);
		final var dateTimeTo = LocalDateTime.of(2022, 1, 1, 23, 59);

		final var result = repository.findAllMatching(
			null,
			null,
			dateTimeFrom,
			dateTimeTo,
			null);

		assertThat(result).isNotNull().isNotEmpty();
	}

	@Test
	void testFindAllMatchingWithCustomerAndFacility() {
		final var customerOrgId = "5591561234";
		final var facilityId = "9115801178";

		final var result = repository.findAllMatching(
			customerOrgId,
			facilityId,
			null,
			null,
			null);

		assertThat(result).isNotNull().hasSizeGreaterThan(0);
	}

	@Test
	void testFindAllMatchingWithNoResults() {
		final var customerOrgId = "nonexistent";
		final var facilityId = "nonexistent";

		final var result = repository.findAllMatching(
			customerOrgId,
			facilityId,
			null,
			null,
			null);

		assertThat(result).isEmpty();
	}

	@Test
	void testFindAllMatchingWithDayAggregationLevel() {
		final var customerOrgId = "5591561234";
		final var facilityId = "9115801178";
		final var dateTimeFrom = LocalDateTime.of(2022, 1, 1, 1, 0);
		final var dateTimeTo = LocalDateTime.of(2022, 1, 1, 5, 0);
		final var aggregationLevel = "DAY";

		final var result = repository.findAllMatching(
			customerOrgId,
			facilityId,
			dateTimeFrom,
			dateTimeTo,
			aggregationLevel);

		// With DAY aggregation, hourly data is aggregated to daily level
		// Should return 3 records (one per feedType: Aktiv, flow, flow_temperature) for 2022-01-01
		assertThat(result).hasSize(3).allMatch(m -> m.getMeasurementTimestamp().toLocalDate().equals(LocalDateTime.of(2022, 1, 1, 0, 0).toLocalDate()));
	}
}
