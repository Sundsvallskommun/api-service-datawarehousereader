package se.sundsvall.datawarehousereader.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementRepository;
import se.sundsvall.datawarehousereader.service.logic.PartyProvider;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceTest {

	@Mock
	private PartyProvider partyProviderMock;

	@Mock
	private MeasurementRepository measurementRepositoryMock;

	@InjectMocks
	private MeasurementService service;

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(partyProviderMock, measurementRepositoryMock);
	}

	@Test
	void getElectricityMeasurementsWithAllParameters() {
		final var municipalityId = "2281";
		final var partyId = "81471222-5798-11e9-ae24-57fa13b361e1";
		final var facilityId = "735999109151401011";
		final var legalId = "5591627751";
		final var aggregation = Aggregation.HOUR;
		final var fromDateTime = OffsetDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
		final var toDateTime = OffsetDateTime.of(2023, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);

		final var parameters = new MeasurementParameters();
		parameters.setPartyId(partyId);
		parameters.setFacilityId(facilityId);
		parameters.setFromDateTime(fromDateTime);
		parameters.setToDateTime(toDateTime);

		final var expectedMeasurement = Measurement.create()
			.withUuid("uuid-123")
			.withCustomerOrgId(legalId)
			.withFacilityId(facilityId)
			.withFeedType("Production")
			.withUnit("kWh")
			.withUsage(100)
			.withInterpolation(0)
			.withDateAndTime(LocalDateTime.of(2023, 6, 15, 12, 0, 0));

		when(partyProviderMock.translateToLegalId(municipalityId, partyId)).thenReturn(legalId);
		when(measurementRepositoryMock.getElectricityMeasurements(
			legalId,
			facilityId,
			aggregation,
			fromDateTime.toLocalDateTime(),
			toDateTime.toLocalDateTime())).thenReturn(List.of(expectedMeasurement));

		final var result = service.getMeasurements(municipalityId, Category.ELECTRICITY, aggregation, parameters);

		assertThat(result).hasSize(1);
		assertThat(result.getFirst()).isEqualTo(expectedMeasurement);

		verify(partyProviderMock).translateToLegalId(municipalityId, partyId);
		verify(measurementRepositoryMock).getElectricityMeasurements(
			legalId,
			facilityId,
			aggregation,
			fromDateTime.toLocalDateTime(),
			toDateTime.toLocalDateTime());
	}

	@Test
	void getElectricityMeasurementsWithNullPartyId() {
		final var municipalityId = "2281";
		final var facilityId = "735999109151401011";
		final var aggregation = Aggregation.DAY;
		final var fromDateTime = OffsetDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
		final var toDateTime = OffsetDateTime.of(2023, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);

		final var parameters = new MeasurementParameters();
		parameters.setPartyId(null);
		parameters.setFacilityId(facilityId);
		parameters.setFromDateTime(fromDateTime);
		parameters.setToDateTime(toDateTime);

		when(measurementRepositoryMock.getElectricityMeasurements(
			null,
			facilityId,
			aggregation,
			fromDateTime.toLocalDateTime(),
			toDateTime.toLocalDateTime())).thenReturn(List.of());

		final var result = service.getMeasurements(municipalityId, Category.ELECTRICITY, aggregation, parameters);

		assertThat(result).isEmpty();

		verify(partyProviderMock, never()).translateToLegalId(any(), any());
		verify(measurementRepositoryMock).getElectricityMeasurements(
			null,
			facilityId,
			aggregation,
			fromDateTime.toLocalDateTime(),
			toDateTime.toLocalDateTime());
	}

	@Test
	void getElectricityMeasurementsWithNullDateTimeParameters() {
		final var municipalityId = "2281";
		final var partyId = "81471222-5798-11e9-ae24-57fa13b361e1";
		final var facilityId = "735999109151401011";
		final var legalId = "5591627751";
		final var aggregation = Aggregation.MONTH;

		final var parameters = new MeasurementParameters();
		parameters.setPartyId(partyId);
		parameters.setFacilityId(facilityId);
		parameters.setFromDateTime(null);
		parameters.setToDateTime(null);

		when(partyProviderMock.translateToLegalId(municipalityId, partyId)).thenReturn(legalId);
		when(measurementRepositoryMock.getElectricityMeasurements(
			legalId,
			facilityId,
			aggregation,
			null,
			null)).thenReturn(List.of());

		final var result = service.getMeasurements(municipalityId, Category.ELECTRICITY, aggregation, parameters);

		assertThat(result).isEmpty();

		verify(partyProviderMock).translateToLegalId(municipalityId, partyId);
		verify(measurementRepositoryMock).getElectricityMeasurements(
			legalId,
			facilityId,
			aggregation,
			null,
			null);
	}

	@Test
	void getElectricityMeasurementsReturnsEmptyList() {
		final var municipalityId = "2281";
		final var partyId = "81471222-5798-11e9-ae24-57fa13b361e1";
		final var facilityId = "735999109151401011";
		final var legalId = "5591627751";
		final var aggregation = Aggregation.HOUR;
		final var fromDateTime = OffsetDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
		final var toDateTime = OffsetDateTime.of(2023, 1, 2, 0, 0, 0, 0, ZoneOffset.UTC);

		final var parameters = new MeasurementParameters();
		parameters.setPartyId(partyId);
		parameters.setFacilityId(facilityId);
		parameters.setFromDateTime(fromDateTime);
		parameters.setToDateTime(toDateTime);

		when(partyProviderMock.translateToLegalId(municipalityId, partyId)).thenReturn(legalId);
		when(measurementRepositoryMock.getElectricityMeasurements(
			legalId,
			facilityId,
			aggregation,
			fromDateTime.toLocalDateTime(),
			toDateTime.toLocalDateTime())).thenReturn(List.of());

		final var result = service.getMeasurements(municipalityId, Category.ELECTRICITY, aggregation, parameters);

		assertThat(result).isEmpty();

		verify(partyProviderMock).translateToLegalId(municipalityId, partyId);
		verify(measurementRepositoryMock).getElectricityMeasurements(
			legalId,
			facilityId,
			aggregation,
			fromDateTime.toLocalDateTime(),
			toDateTime.toLocalDateTime());
	}

	@Test
	void getDistrictHeatingMeasurementsReturnsEmptyList() {
		final var municipalityId = "2281";
		final var partyId = "81471222-5798-11e9-ae24-57fa13b361e1";
		final var legalId = "5591627751";
		final var facilityId = "facilityId";
		final var aggregation = Aggregation.MONTH;
		final var fromDateTime = OffsetDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
		final var toDateTime = OffsetDateTime.of(2023, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);

		final var parameters = new MeasurementParameters();
		parameters.setPartyId(partyId);
		parameters.setFacilityId(facilityId);
		parameters.setFromDateTime(fromDateTime);
		parameters.setToDateTime(toDateTime);

		when(partyProviderMock.translateToLegalId(municipalityId, partyId)).thenReturn(legalId);
		when(measurementRepositoryMock.getDistrictHeatingMeasurements(legalId, facilityId, aggregation, fromDateTime.toLocalDateTime(), toDateTime.toLocalDateTime())).thenReturn(List.of());

		final var result = service.getMeasurements(municipalityId, Category.DISTRICT_HEATING, aggregation, parameters);

		assertThat(result).isEmpty();

		verify(partyProviderMock).translateToLegalId(municipalityId, partyId);
		verify(measurementRepositoryMock).getDistrictHeatingMeasurements(legalId, facilityId, aggregation, fromDateTime.toLocalDateTime(), toDateTime.toLocalDateTime());
	}

	@ParameterizedTest
	@EnumSource(value = Category.class, names = {
		"COMMUNICATION", "DISTRICT_COOLING", "ELECTRICITY_TRADE", "WASTE_MANAGEMENT", "WATER"
	})
	void getNotImplementedCategoryThrowsProblem(Category category) {
		final var municipalityId = "2281";
		final var aggregation = Aggregation.HOUR;

		final var parameters = new MeasurementParameters();
		parameters.setPartyId("81471222-5798-11e9-ae24-57fa13b361e1");
		parameters.setFacilityId("facilityId");
		parameters.setFromDateTime(OffsetDateTime.now());
		parameters.setToDateTime(OffsetDateTime.now());

		when(partyProviderMock.translateToLegalId(any(), any())).thenReturn("legalId");

		assertThatThrownBy(() -> service.getMeasurements(municipalityId, category, aggregation, parameters))
			.isInstanceOf(ThrowableProblem.class)
			.hasFieldOrPropertyWithValue("status", Status.NOT_IMPLEMENTED)
			.hasMessageContaining(category.name());

		verify(partyProviderMock).translateToLegalId(municipalityId, parameters.getPartyId());
	}

	@ParameterizedTest
	@EnumSource(Aggregation.class)
	void getElectricityMeasurementsWithDifferentAggregations(Aggregation aggregation) {
		final var municipalityId = "2281";
		final var partyId = "81471222-5798-11e9-ae24-57fa13b361e1";
		final var facilityId = "735999109151401011";
		final var legalId = "5591627751";
		final var fromDateTime = OffsetDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
		final var toDateTime = OffsetDateTime.of(2023, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);

		final var parameters = new MeasurementParameters();
		parameters.setPartyId(partyId);
		parameters.setFacilityId(facilityId);
		parameters.setFromDateTime(fromDateTime);
		parameters.setToDateTime(toDateTime);

		when(partyProviderMock.translateToLegalId(municipalityId, partyId)).thenReturn(legalId);
		when(measurementRepositoryMock.getElectricityMeasurements(
			legalId,
			facilityId,
			aggregation,
			fromDateTime.toLocalDateTime(),
			toDateTime.toLocalDateTime())).thenReturn(List.of());

		final var result = service.getMeasurements(municipalityId, Category.ELECTRICITY, aggregation, parameters);

		assertThat(result).isEmpty();

		verify(partyProviderMock).translateToLegalId(municipalityId, partyId);
		verify(measurementRepositoryMock).getElectricityMeasurements(
			legalId,
			facilityId,
			aggregation,
			fromDateTime.toLocalDateTime(),
			toDateTime.toLocalDateTime());
	}
}
