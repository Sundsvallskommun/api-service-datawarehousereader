package se.sundsvall.datawarehousereader.service.logic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementDistrictHeatingRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingEntity;

@ExtendWith(MockitoExtension.class)
class DistrictHeatingMeasurementProviderTest {

	@Mock
	private MeasurementDistrictHeatingRepository districtHeatingRepositoryMock;

	@Mock
	private MeasurementDistrictHeatingEntity entityMock;

	@Captor
	private ArgumentCaptor<String> customerOrgIdCaptor;

	@Captor
	private ArgumentCaptor<String> facilityIdCaptor;

	@Captor
	private ArgumentCaptor<LocalDateTime> fromDateCaptor;

	@Captor
	private ArgumentCaptor<LocalDateTime> toDateCaptor;

	@Captor
	private ArgumentCaptor<String> aggregationLevelCaptor;

	@InjectMocks
	private DistrictHeatingMeasurementProvider provider;

	@ParameterizedTest
	@EnumSource(value = Aggregation.class)
	void testGetMeasurementsWithValidAggregations(final Aggregation aggregation) {
		// Arrange
		final var searchParams = MeasurementParameters.create();
		searchParams.setFacilityId("facilityId");
		searchParams.setPage(1);
		searchParams.setLimit(10);
		searchParams.setSortBy(List.of("measurementTimestamp"));
		searchParams.setSortDirection(Sort.Direction.ASC);

		final var legalId = "legalId";
		final var fromDateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
		final var toDateTime = LocalDateTime.of(2023, 1, 31, 23, 59);

		when(entityMock.getInterpolation()).thenReturn(0);
		when(entityMock.getUnit()).thenReturn("kWh");
		when(entityMock.getFeedType()).thenReturn("consumption");
		when(entityMock.getFacilityId()).thenReturn("facilityId");
		when(entityMock.getUuid()).thenReturn("test-uuid");
		when(entityMock.getUsage()).thenReturn(
			java.math.BigDecimal.valueOf(100.0));
		when(entityMock.getMeasurementTimestamp()).thenReturn(fromDateTime);
		when(
			districtHeatingRepositoryMock.findAllMatching(
				any(),
				any(),
				any(),
				any(),
				any())).thenReturn(List.of(entityMock));

		// Act
		final var result = provider.getMeasurements(
			legalId,
			aggregation,
			fromDateTime,
			toDateTime,
			searchParams);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getMeasurements()).hasSize(1);
		assertThat(result.getMetaData()).isNotNull();

		verify(districtHeatingRepositoryMock).findAllMatching(
			customerOrgIdCaptor.capture(),
			facilityIdCaptor.capture(),
			fromDateCaptor.capture(),
			toDateCaptor.capture(),
			aggregationLevelCaptor.capture());

		assertThat(customerOrgIdCaptor.getValue()).isEqualTo(legalId);
		assertThat(facilityIdCaptor.getValue()).isEqualTo("facilityId");
		assertThat(fromDateCaptor.getValue()).isEqualTo(fromDateTime);
		assertThat(toDateCaptor.getValue()).isEqualTo(toDateTime);

		final var expectedAggregationLevel = switch (aggregation) {
			case QUARTER -> "QUARTER";
			case HOUR -> "HOUR";
			case DAY -> "DAY";
			case MONTH -> "MONTH";
		};
		assertThat(aggregationLevelCaptor.getValue()).isEqualTo(
			expectedAggregationLevel);
	}

	@Test
	void testGetMeasurementsWithEmptyResult() {
		// Arrange
		final var searchParams = MeasurementParameters.create();
		when(
			districtHeatingRepositoryMock.findAllMatching(
				any(),
				any(),
				any(),
				any(),
				any())).thenReturn(Collections.emptyList());

		// Act
		final var result = provider.getMeasurements(
			null,
			Aggregation.HOUR,
			null,
			null,
			searchParams);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getMeasurements()).isEmpty();
	}

	@Test
	void testGetMeasurementsWithPagination() {
		// Arrange
		final var searchParams = MeasurementParameters.create();
		searchParams.setPage(2);
		searchParams.setLimit(2);

		// Create 5 mock entities to test pagination
		final var entities = List.of(
			createMockEntity(1),
			createMockEntity(2),
			createMockEntity(3),
			createMockEntity(4),
			createMockEntity(5));

		when(
			districtHeatingRepositoryMock.findAllMatching(
				any(),
				any(),
				any(),
				any(),
				any())).thenReturn(entities);

		// Act
		final var result = provider.getMeasurements(
			null,
			Aggregation.HOUR,
			null,
			null,
			searchParams);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getMeasurements()).hasSize(2); // Page 2 with limit 2 should have 2 items (indices 2-3)
		assertThat(result.getMetaData().getTotalRecords()).isEqualTo(5);
		assertThat(result.getMetaData().getTotalPages()).isEqualTo(3);
		assertThat(result.getMetaData().getPage()).isEqualTo(2); // Page 2 as requested
	}

	@Test
	void testGetMeasurementsWithPageLargerThanAvailable() {
		// Arrange
		final var searchParams = MeasurementParameters.create();
		searchParams.setPage(10);
		searchParams.setLimit(10);

		final var entities = List.of(createMockEntity(1));

		when(
			districtHeatingRepositoryMock.findAllMatching(
				any(),
				any(),
				any(),
				any(),
				any())).thenReturn(entities);

		// Act
		final var result = provider.getMeasurements(
			null,
			Aggregation.HOUR,
			null,
			null,
			searchParams);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getMeasurements()).isEmpty(); // Should be empty when page exceeds available pages
	}

	@Test
	void testWithSort() {
		// Arrange
		final var searchParams = MeasurementParameters.create();
		searchParams.setSortBy(List.of("measurementTimestamp"));
		searchParams.setSortDirection(Sort.Direction.ASC);

		when(
			districtHeatingRepositoryMock.findAllMatching(
				any(),
				any(),
				any(),
				any(),
				any())).thenReturn(Collections.emptyList());

		// Act
		provider.getMeasurements(
			null,
			Aggregation.HOUR,
			null,
			null,
			searchParams);

		// Assert
		verify(districtHeatingRepositoryMock).findAllMatching(
			any(),
			any(),
			any(),
			any(),
			any());
	}

	@Test
	void testWithAllParameters() {
		// Arrange
		final var legalId = "legalId";
		final var facilityId = "facilityId";
		final var fromDateTime = LocalDateTime.of(2023, 1, 1, 0, 0);
		final var toDateTime = LocalDateTime.of(2023, 1, 31, 23, 59);
		final var searchParams = MeasurementParameters.create();
		searchParams.setFacilityId(facilityId);
		searchParams.setPage(1);
		searchParams.setLimit(10);
		searchParams.setSortBy(List.of("measurementTimestamp"));
		searchParams.setSortDirection(Sort.Direction.DESC);

		when(
			districtHeatingRepositoryMock.findAllMatching(
				any(),
				any(),
				any(),
				any(),
				any())).thenReturn(Collections.emptyList());

		// Act
		provider.getMeasurements(
			legalId,
			Aggregation.DAY,
			fromDateTime,
			toDateTime,
			searchParams);

		// Assert
		verify(districtHeatingRepositoryMock).findAllMatching(
			legalId,
			facilityId,
			fromDateTime,
			toDateTime,
			"DAY");
	}

	private MeasurementDistrictHeatingEntity createMockEntity(final int id) {
		return MeasurementDistrictHeatingEntity.create()
			.withInterpolation(0)
			.withUnit("kWh")
			.withFeedType("consumption")
			.withFacilityId("facilityId")
			.withUuid("test-uuid-" + id)
			.withUsage(java.math.BigDecimal.valueOf(100.0 + id))
			.withMeasurementTimestamp(LocalDateTime.of(2023, 1, 1, id, 0));
	}
}
