package se.sundsvall.datawarehousereader.service.logic;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;
import static org.zalando.problem.Status.BAD_REQUEST;
import static org.zalando.problem.Status.NOT_IMPLEMENTED;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.HOUR;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.QUARTER;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementElectricityDayRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementElectricityHourRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementElectricityMonthRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityDayEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityHourEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityMonthEntity;

@ExtendWith(MockitoExtension.class)
class ElectricityMeasurementProviderTest {

	private static final LocalDateTime START_DATE_TIME = LocalDate.now().with(firstDayOfYear()).atStartOfDay();
	private static final LocalDateTime END_DATE_TIME = LocalDate.now().with(lastDayOfYear()).atStartOfDay();

	@Mock
	private MeasurementElectricityHourRepository electricityHourRepositoryMock;

	@Mock
	private MeasurementElectricityDayRepository electricityDayRepositoryMock;

	@Mock
	private MeasurementElectricityMonthRepository electricityMonthRepositoryMock;

	@Mock
	private Page<MeasurementElectricityDayEntity> pageDayMock;

	@Mock
	private Page<MeasurementElectricityMonthEntity> pageMonthMock;

	@Mock
	private MeasurementElectricityHourEntity entityHourMock;

	@Mock
	private MeasurementElectricityDayEntity entityDayMock;

	@Mock
	private MeasurementElectricityMonthEntity entityMonthMock;

	@Captor
	private ArgumentCaptor<Pageable> pageableCaptor;

	@Captor
	private ArgumentCaptor<String> customerOrgNrCaptor;

	@Captor
	private ArgumentCaptor<String> facilityIdCaptor;

	@Captor
	private ArgumentCaptor<LocalDateTime> fromDateCaptor;

	@Captor
	private ArgumentCaptor<LocalDateTime> toDateCaptor;

	@InjectMocks
	private ElectricityMeasurementProvider provider;

	@ParameterizedTest
	@EnumSource(value = Aggregation.class, names = {
		"QUARTER"
	}, mode = EnumSource.Mode.EXCLUDE)
	void testWithEmptyParameters(Aggregation aggregateOn) {
		final var searchParams = MeasurementParameters.create();

		switch (aggregateOn) {
			case HOUR -> when(electricityHourRepositoryMock.findAllMatching(any(), any(), any(), any())).thenReturn(new ArrayList<>(List.of(entityHourMock)));
			case DAY -> {
				when(electricityDayRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageDayMock);
				when(pageDayMock.getContent()).thenReturn(List.of(entityDayMock));
				when(pageDayMock.getTotalElements()).thenReturn(1L);
				when(pageDayMock.getTotalPages()).thenReturn(1);
				when(pageDayMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageDayMock.getNumberOfElements()).thenReturn(1);
				when(pageDayMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageDayMock.getSort()).thenReturn(searchParams.sort());
			}
			case MONTH -> {
				when(electricityMonthRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageMonthMock);
				when(pageMonthMock.getContent()).thenReturn(List.of(entityMonthMock));
				when(pageMonthMock.getTotalPages()).thenReturn(1);
				when(pageMonthMock.getTotalElements()).thenReturn(1L);
				when(pageMonthMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageMonthMock.getNumberOfElements()).thenReturn(1);
				when(pageMonthMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageMonthMock.getSort()).thenReturn(searchParams.sort());
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		final var response = provider.getMeasurements(null, aggregateOn, START_DATE_TIME, END_DATE_TIME, searchParams);

		switch (aggregateOn) {
			case HOUR -> {
				verify(electricityHourRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(),
					fromDateCaptor.capture(), toDateCaptor.capture());
				verifyNoInteractions(electricityDayRepositoryMock);
				verifyNoInteractions(electricityMonthRepositoryMock);
			}
			case DAY -> {
				verify(electricityDayRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(),
					fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(electricityHourRepositoryMock);
				verifyNoInteractions(electricityMonthRepositoryMock);
			}
			case MONTH -> {
				verify(electricityMonthRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(),
					fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(electricityHourRepositoryMock);
				verifyNoInteractions(electricityDayRepositoryMock);
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		assertThat(customerOrgNrCaptor.getValue()).isNull();
		assertThat(facilityIdCaptor.getValue()).isNull();
		assertThat(fromDateCaptor.getValue()).isEqualTo(START_DATE_TIME);
		assertThat(toDateCaptor.getValue()).isEqualTo(END_DATE_TIME);
		if (HOUR != aggregateOn) {
			assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
			assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(100);
			assertThat(pageableCaptor.getValue().getSort()).isEqualTo(by(ASC, "measurementTimestamp"));
		}
		assertThat(response.getMetaData().getCount()).isEqualTo(1);
		assertThat(response.getMetaData().getLimit()).isEqualTo(100);
		assertThat(response.getMetaData().getPage()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(1);
		assertThat(response.getMeasurements()).hasSize(1);
	}

	@ParameterizedTest
	@EnumSource(value = Aggregation.class, names = {
		"QUARTER"
	}, mode = EnumSource.Mode.EXCLUDE)
	void testWithAllParametersSet(Aggregation aggregateOn) {
		final var searchParams = MeasurementParameters.create();
		final var legalId = "legalId";
		final var partyId = "partyId";
		final var facilityId = "facilityId";
		final var fromDateTime = LocalDateTime.now().minusMonths(1);
		final var toDateTime = LocalDateTime.now();
		searchParams.setPartyId(partyId);
		searchParams.setFacilityId(facilityId);
		searchParams.setLimit(1);

		switch (aggregateOn) {
			case HOUR -> when(electricityHourRepositoryMock.findAllMatching(any(), any(), any(), any())).thenReturn(new ArrayList<>(List.of(entityHourMock, entityHourMock)));
			case DAY -> {
				when(electricityDayRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageDayMock);
				when(pageDayMock.getContent()).thenReturn(List.of(entityDayMock));
				when(pageDayMock.getTotalElements()).thenReturn(2L);
				when(pageDayMock.getTotalPages()).thenReturn(2);
				when(pageDayMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageDayMock.getNumberOfElements()).thenReturn(1);
				when(pageDayMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageDayMock.getSort()).thenReturn(searchParams.sort());
			}
			case MONTH -> {
				when(electricityMonthRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageMonthMock);
				when(pageMonthMock.getContent()).thenReturn(List.of(entityMonthMock));
				when(pageMonthMock.getTotalPages()).thenReturn(2);
				when(pageMonthMock.getTotalElements()).thenReturn(2L);
				when(pageMonthMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageMonthMock.getNumberOfElements()).thenReturn(1);
				when(pageMonthMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageMonthMock.getSort()).thenReturn(searchParams.sort());
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		final var response = provider.getMeasurements(legalId, aggregateOn, fromDateTime, toDateTime, searchParams);

		switch (aggregateOn) {
			case HOUR -> {
				verify(electricityHourRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(),
					fromDateCaptor.capture(), toDateCaptor.capture());
				verifyNoInteractions(electricityDayRepositoryMock);
				verifyNoInteractions(electricityMonthRepositoryMock);
			}
			case DAY -> {
				verify(electricityDayRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(),
					fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(electricityHourRepositoryMock);
				verifyNoInteractions(electricityMonthRepositoryMock);
			}
			case MONTH -> {
				verify(electricityMonthRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(),
					fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(electricityHourRepositoryMock);
				verifyNoInteractions(electricityDayRepositoryMock);
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		assertThat(customerOrgNrCaptor.getValue()).isEqualTo(legalId);
		assertThat(facilityIdCaptor.getValue()).isEqualTo(facilityId);
		assertThat(fromDateCaptor.getValue()).isEqualTo(fromDateTime);
		assertThat(toDateCaptor.getValue()).isEqualTo(toDateTime);
		if (HOUR != aggregateOn) {
			assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
			assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(1);
			assertThat(pageableCaptor.getValue().getSort()).isEqualTo(by(ASC, "measurementTimestamp"));
		}
		assertThat(response.getMetaData().getCount()).isEqualTo(1);
		assertThat(response.getMetaData().getLimit()).isEqualTo(1);
		assertThat(response.getMetaData().getPage()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(2);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(2);
		assertThat(response.getMeasurements()).hasSize(1);
	}

	@ParameterizedTest
	@EnumSource(value = Aggregation.class, names = {
		"QUARTER"
	}, mode = EnumSource.Mode.EXCLUDE)
	void testForPageLargerThanResultsMaxPage(Aggregation aggregateOn) {
		final var searchParams = MeasurementParameters.create();
		searchParams.setPage(101);
		searchParams.setLimit(1);

		switch (aggregateOn) {
			case HOUR -> {
				when(electricityHourRepositoryMock.findAllMatching(any(), any(), any(), any())).thenReturn(new ArrayList<>(List.of(entityHourMock, entityHourMock)));
			}
			case DAY -> {
				when(electricityDayRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageDayMock);
				when(pageDayMock.getTotalElements()).thenReturn(2L);
				when(pageDayMock.getTotalPages()).thenReturn(2);
				when(pageDayMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageDayMock.getNumberOfElements()).thenReturn(0);
				when(pageDayMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageDayMock.getSort()).thenReturn(searchParams.sort());
			}
			case MONTH -> {
				when(electricityMonthRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageMonthMock);
				when(pageMonthMock.getTotalPages()).thenReturn(2);
				when(pageMonthMock.getTotalElements()).thenReturn(2L);
				when(pageMonthMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageMonthMock.getNumberOfElements()).thenReturn(0);
				when(pageMonthMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageMonthMock.getSort()).thenReturn(searchParams.sort());
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		final var response = provider.getMeasurements(null, aggregateOn, START_DATE_TIME, END_DATE_TIME, searchParams);

		switch (aggregateOn) {
			case HOUR -> {
				verify(electricityHourRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(),
					fromDateCaptor.capture(), toDateCaptor.capture());
				verifyNoInteractions(electricityDayRepositoryMock);
				verifyNoInteractions(electricityMonthRepositoryMock);
			}
			case DAY -> {
				verify(electricityDayRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(),
					fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(electricityHourRepositoryMock);
				verifyNoInteractions(electricityMonthRepositoryMock);
			}
			case MONTH -> {
				verify(electricityMonthRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(),
					fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(electricityHourRepositoryMock);
				verifyNoInteractions(electricityDayRepositoryMock);
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		assertThat(response.getMetaData().getCount()).isZero();
		assertThat(response.getMetaData().getLimit()).isEqualTo(1);
		assertThat(response.getMetaData().getPage()).isEqualTo(101);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(2);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(2);
		assertThat(response.getMeasurements()).isEmpty();
	}

	@Test
	void testProblemIsThrownWhenNotSupportedAggregation() {
		final var searchParams = MeasurementParameters.create();

		final ThrowableProblem e = assertThrows(ThrowableProblem.class, () -> provider.getMeasurements(null, QUARTER, null, null, searchParams));
		assertThat(e.getStatus()).isEqualTo(NOT_IMPLEMENTED);
		assertThat(e.getMessage()).isEqualTo("Not Implemented: aggregation 'QUARTER' and category 'ELECTRICITY'");

		verifyNoInteractions(electricityHourRepositoryMock, electricityDayRepositoryMock, electricityMonthRepositoryMock);
	}

	@ParameterizedTest
	@MethodSource("dateRangeArgumentProvider")
	void testDateRangeExceedsMax(LocalDateTime start, LocalDateTime end) {
		final var searchParams = MeasurementParameters.create();

		final ThrowableProblem e = assertThrows(ThrowableProblem.class, () -> provider.getMeasurements(null, HOUR, start, end, searchParams));
		assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(e.getMessage()).isEqualTo("Bad Request: Date range exceeds maximum range. Range can max be one year when asking for hourly electricity measurements.");

		verifyNoInteractions(electricityHourRepositoryMock, electricityDayRepositoryMock, electricityMonthRepositoryMock);
	}

	private static Stream<Arguments> dateRangeArgumentProvider() {
		return Stream.of(
			Arguments.of(null, null),
			Arguments.of(null, END_DATE_TIME),
			Arguments.of(START_DATE_TIME, null),
			Arguments.of(START_DATE_TIME, END_DATE_TIME.plusDays(1).plusNanos(1)),
			Arguments.of(START_DATE_TIME.minusNanos(1), END_DATE_TIME.plusDays(1)));
	}
}
