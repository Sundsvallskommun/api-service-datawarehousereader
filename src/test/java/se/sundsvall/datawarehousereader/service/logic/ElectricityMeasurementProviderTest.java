package se.sundsvall.datawarehousereader.service.logic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.zalando.problem.Status.NOT_IMPLEMENTED;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.HOUR;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.YEAR;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	@EnumSource(value = Aggregation.class, names = {"YEAR"}, mode = EnumSource.Mode.EXCLUDE)
	void testWithEmptyParameters(Aggregation aggregateOn) {
		final var searchParams = MeasurementParameters.create();

		switch (aggregateOn) {
			case HOUR -> {
				when(electricityHourRepositoryMock.findAllMatching(any(), any(), any(), any())).thenReturn(new ArrayList<>(List.of(entityHourMock)));
			}
			case DAY -> {
				when(electricityDayRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageDayMock);
				when(pageDayMock.getContent()).thenReturn(List.of(entityDayMock));
				when(pageDayMock.getTotalElements()).thenReturn(1L);
				when(pageDayMock.getTotalPages()).thenReturn(1);
			}
			case MONTH -> {
				when(electricityMonthRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageMonthMock);
				when(pageMonthMock.getContent()).thenReturn(List.of(entityMonthMock));
				when(pageMonthMock.getTotalPages()).thenReturn(1);
				when(pageMonthMock.getTotalElements()).thenReturn(1L);
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		final var response = provider.getMeasurements(null, aggregateOn, null, null, searchParams);

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
		assertThat(fromDateCaptor.getValue()).isNull();
		assertThat(toDateCaptor.getValue()).isNull();
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
	@EnumSource(value = Aggregation.class, names = {"YEAR"}, mode = EnumSource.Mode.EXCLUDE)
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
			case HOUR -> {
				when(electricityHourRepositoryMock.findAllMatching(any(), any(), any(), any())).thenReturn(new ArrayList<>(List.of(entityHourMock, entityHourMock)));
			}
			case DAY -> {
				when(electricityDayRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageDayMock);
				when(pageDayMock.getContent()).thenReturn(List.of(entityDayMock));
				when(pageDayMock.getTotalElements()).thenReturn(2L);
				when(pageDayMock.getTotalPages()).thenReturn(2);
			}
			case MONTH -> {
				when(electricityMonthRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageMonthMock);
				when(pageMonthMock.getContent()).thenReturn(List.of(entityMonthMock));
				when(pageMonthMock.getTotalPages()).thenReturn(2);
				when(pageMonthMock.getTotalElements()).thenReturn(2L);
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
	@EnumSource(value = Aggregation.class, names = {"YEAR"}, mode = EnumSource.Mode.EXCLUDE)
	void testForPageLargerThanResultsMaxPage(Aggregation aggregateOn) {

		switch (aggregateOn) {
			case HOUR -> {
				when(electricityHourRepositoryMock.findAllMatching(any(), any(), any(), any())).thenReturn(new ArrayList<>(List.of(entityHourMock, entityHourMock)));
			}
			case DAY -> {
				when(electricityDayRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageDayMock);
				when(pageDayMock.getTotalElements()).thenReturn(2L);
				when(pageDayMock.getTotalPages()).thenReturn(2);
			}
			case MONTH -> {
				when(electricityMonthRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageMonthMock);
				when(pageMonthMock.getTotalPages()).thenReturn(2);
				when(pageMonthMock.getTotalElements()).thenReturn(2L);
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		final var searchParams = MeasurementParameters.create();
		searchParams.setPage(101);
		searchParams.setLimit(1);
		final var response = provider.getMeasurements(null, aggregateOn, null, null, searchParams);

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

		ThrowableProblem e = assertThrows(ThrowableProblem.class, () -> provider.getMeasurements(null, YEAR, null, null, searchParams));
		assertThat(e.getStatus()).isEqualTo(NOT_IMPLEMENTED);
		assertThat(e.getMessage()).isEqualTo("Not Implemented: aggregation 'YEAR' and category 'ELECTRICITY'");

		verifyNoInteractions(electricityMonthRepositoryMock);
	}
}
