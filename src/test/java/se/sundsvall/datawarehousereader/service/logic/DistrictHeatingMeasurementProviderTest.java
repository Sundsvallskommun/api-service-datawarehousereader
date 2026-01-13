package se.sundsvall.datawarehousereader.service.logic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;
import static org.zalando.problem.Status.NOT_IMPLEMENTED;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.QUARTER;

import java.time.LocalDateTime;
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
import org.springframework.data.domain.Sort;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementDistrictHeatingDayRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementDistrictHeatingHourRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementDistrictHeatingMonthRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingDayEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingHourEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingMonthEntity;

@ExtendWith(MockitoExtension.class)
class DistrictHeatingMeasurementProviderTest {

	@Mock
	private MeasurementDistrictHeatingHourRepository districtHeatingHourRepositoryMock;

	@Mock
	private MeasurementDistrictHeatingDayRepository districtHeatingDayRepositoryMock;

	@Mock
	private MeasurementDistrictHeatingMonthRepository districtHeatingMonthRepositoryMock;

	@Mock
	private Page<MeasurementDistrictHeatingHourEntity> pageHourMock;

	@Mock
	private Page<MeasurementDistrictHeatingDayEntity> pageDayMock;

	@Mock
	private Page<MeasurementDistrictHeatingMonthEntity> pageMonthMock;

	@Mock
	private MeasurementDistrictHeatingHourEntity hourEntityMock;

	@Mock
	private MeasurementDistrictHeatingDayEntity dayEntityMock;

	@Mock
	private MeasurementDistrictHeatingMonthEntity monthEntityMock;

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
	private DistrictHeatingMeasurementProvider provider;

	@ParameterizedTest
	@EnumSource(value = Aggregation.class, names = {
		"QUARTER"
	}, mode = EnumSource.Mode.EXCLUDE)
	void testWithEmptyParameters(Aggregation aggregateOn) {
		final var searchParams = MeasurementParameters.create();

		switch (aggregateOn) {
			case HOUR -> {
				when(districtHeatingHourRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageHourMock);
				when(pageHourMock.getContent()).thenReturn(List.of(hourEntityMock));
				when(pageHourMock.getTotalPages()).thenReturn(1);
				when(pageHourMock.getTotalElements()).thenReturn(1L);
				when(pageHourMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageHourMock.getNumberOfElements()).thenReturn(1);
				when(pageHourMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageHourMock.getSort()).thenReturn(searchParams.sort());
			}
			case DAY -> {
				when(districtHeatingDayRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageDayMock);
				when(pageDayMock.getContent()).thenReturn(List.of(dayEntityMock));
				when(pageDayMock.getTotalPages()).thenReturn(1);
				when(pageDayMock.getTotalElements()).thenReturn(1L);
				when(pageDayMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageDayMock.getNumberOfElements()).thenReturn(1);
				when(pageDayMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageDayMock.getSort()).thenReturn(searchParams.sort());
			}
			case MONTH -> {
				when(districtHeatingMonthRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageMonthMock);
				when(pageMonthMock.getContent()).thenReturn(List.of(monthEntityMock));
				when(pageMonthMock.getTotalPages()).thenReturn(1);
				when(pageMonthMock.getTotalElements()).thenReturn(1L);
				when(pageMonthMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageMonthMock.getNumberOfElements()).thenReturn(1);
				when(pageMonthMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageMonthMock.getSort()).thenReturn(searchParams.sort());
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		final var response = provider.getMeasurements(null, aggregateOn, null, null, searchParams);

		switch (aggregateOn) {
			case HOUR -> {
				verify(districtHeatingHourRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(), fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(districtHeatingDayRepositoryMock);
				verifyNoInteractions(districtHeatingMonthRepositoryMock);
			}
			case DAY -> {
				verify(districtHeatingDayRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(), fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(districtHeatingHourRepositoryMock);
				verifyNoInteractions(districtHeatingMonthRepositoryMock);
			}
			case MONTH -> {
				verify(districtHeatingMonthRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(), fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(districtHeatingHourRepositoryMock);
				verifyNoInteractions(districtHeatingDayRepositoryMock);
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		assertThat(customerOrgNrCaptor.getValue()).isNull();
		assertThat(facilityIdCaptor.getValue()).isNull();
		assertThat(fromDateCaptor.getValue()).isNull();
		assertThat(toDateCaptor.getValue()).isNull();
		assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(100);
		assertThat(pageableCaptor.getValue().getSort()).isEqualTo(by(Sort.Direction.ASC, "measurementTimestamp"));
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
		final var readingSequence = Integer.valueOf(22);
		final var feedType = "feedType";
		searchParams.setPartyId(partyId);
		searchParams.setFacilityId(facilityId);

		switch (aggregateOn) {
			case HOUR -> {
				when(districtHeatingHourRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageHourMock);
				when(pageHourMock.getContent()).thenReturn(List.of(hourEntityMock));
				when(pageHourMock.getTotalPages()).thenReturn(1);
				when(pageHourMock.getTotalElements()).thenReturn(1L);
				when(pageHourMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageHourMock.getNumberOfElements()).thenReturn(1);
				when(pageHourMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageHourMock.getSort()).thenReturn(searchParams.sort());
				when(hourEntityMock.getReadingSequence()).thenReturn(readingSequence);
				when(hourEntityMock.getFeedType()).thenReturn(feedType);
				when(hourEntityMock.getFacilityId()).thenReturn(facilityId);
			}
			case DAY -> {
				when(districtHeatingDayRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageDayMock);
				when(pageDayMock.getContent()).thenReturn(List.of(dayEntityMock));
				when(pageDayMock.getTotalPages()).thenReturn(1);
				when(pageDayMock.getTotalElements()).thenReturn(1L);
				when(pageDayMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageDayMock.getNumberOfElements()).thenReturn(1);
				when(pageDayMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageDayMock.getSort()).thenReturn(searchParams.sort());
				when(dayEntityMock.getReadingSequence()).thenReturn(readingSequence);
				when(dayEntityMock.getFeedType()).thenReturn(feedType);
				when(dayEntityMock.getFacilityId()).thenReturn(facilityId);
			}
			case MONTH -> {
				when(districtHeatingMonthRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageMonthMock);
				when(pageMonthMock.getContent()).thenReturn(List.of(monthEntityMock));
				when(pageMonthMock.getTotalPages()).thenReturn(1);
				when(pageMonthMock.getTotalElements()).thenReturn(1L);
				when(pageMonthMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageMonthMock.getNumberOfElements()).thenReturn(1);
				when(pageMonthMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageMonthMock.getSort()).thenReturn(searchParams.sort());
				when(monthEntityMock.getReadingSequence()).thenReturn(readingSequence);
				when(monthEntityMock.getFeedType()).thenReturn(feedType);
				when(monthEntityMock.getFacilityId()).thenReturn(facilityId);
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		final var response = provider.getMeasurements(legalId, aggregateOn, fromDateTime, toDateTime, searchParams);

		switch (aggregateOn) {
			case HOUR -> {
				verify(districtHeatingHourRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(), fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(districtHeatingDayRepositoryMock);
				verifyNoInteractions(districtHeatingMonthRepositoryMock);
			}
			case DAY -> {
				verify(districtHeatingDayRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(), fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(districtHeatingHourRepositoryMock);
				verifyNoInteractions(districtHeatingMonthRepositoryMock);
			}
			case MONTH -> {
				verify(districtHeatingMonthRepositoryMock).findAllMatching(customerOrgNrCaptor.capture(), facilityIdCaptor.capture(), fromDateCaptor.capture(), toDateCaptor.capture(), pageableCaptor.capture());
				verifyNoInteractions(districtHeatingHourRepositoryMock);
				verifyNoInteractions(districtHeatingDayRepositoryMock);
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		assertThat(customerOrgNrCaptor.getValue()).isEqualTo(legalId);
		assertThat(facilityIdCaptor.getValue()).isEqualTo(facilityId);
		assertThat(fromDateCaptor.getValue()).isEqualTo(fromDateTime);
		assertThat(toDateCaptor.getValue()).isEqualTo(toDateTime);
		assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(100);
		assertThat(pageableCaptor.getValue().getSort()).isEqualTo(by(ASC, "measurementTimestamp"));
		assertThat(response.getMetaData().getCount()).isEqualTo(1);
		assertThat(response.getMetaData().getLimit()).isEqualTo(100);
		assertThat(response.getMetaData().getPage()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(1);
		assertThat(response.getMeasurements()).hasSize(1);
		assertThat(response.getMeasurements().getFirst().getMetaData()).hasSize(1);
		assertThat(response.getMeasurements().getFirst().getMetaData().getFirst().getValue()).isEqualTo(readingSequence.toString());
	}

	@ParameterizedTest
	@EnumSource(value = Aggregation.class, names = {
		"QUARTER"
	}, mode = EnumSource.Mode.EXCLUDE)
	void testForPageLargerThanResultsMaxPage(Aggregation aggregateOn) {
		final var searchParams = MeasurementParameters.create();
		searchParams.setPage(2);

		switch (aggregateOn) {
			case HOUR -> {
				when(districtHeatingHourRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageHourMock);
				when(pageHourMock.getTotalPages()).thenReturn(1);
				when(pageHourMock.getTotalElements()).thenReturn(1L);
				when(pageHourMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageHourMock.getNumberOfElements()).thenReturn(0);
				when(pageHourMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageHourMock.getSort()).thenReturn(searchParams.sort());
			}
			case DAY -> {
				when(districtHeatingDayRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageDayMock);
				when(pageDayMock.getTotalPages()).thenReturn(1);
				when(pageDayMock.getTotalElements()).thenReturn(1L);
				when(pageDayMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageDayMock.getNumberOfElements()).thenReturn(0);
				when(pageDayMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageDayMock.getSort()).thenReturn(searchParams.sort());
			}
			case MONTH -> {
				when(districtHeatingMonthRepositoryMock.findAllMatching(any(), any(), any(), any(), any())).thenReturn(pageMonthMock);
				when(pageMonthMock.getTotalPages()).thenReturn(1);
				when(pageMonthMock.getTotalElements()).thenReturn(1L);
				when(pageMonthMock.getNumber()).thenReturn(searchParams.getPage() - 1);
				when(pageMonthMock.getNumberOfElements()).thenReturn(0);
				when(pageMonthMock.getSize()).thenReturn(searchParams.getLimit());
				when(pageMonthMock.getSort()).thenReturn(searchParams.sort());
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		final var response = provider.getMeasurements(null, aggregateOn, null, null, searchParams);

		switch (aggregateOn) {
			case HOUR -> {
				verify(districtHeatingHourRepositoryMock).findAllMatching(any(), any(), any(), any(), pageableCaptor.capture());
				verifyNoInteractions(districtHeatingDayRepositoryMock);
				verifyNoInteractions(districtHeatingMonthRepositoryMock);
			}
			case DAY -> {
				verify(districtHeatingDayRepositoryMock).findAllMatching(any(), any(), any(), any(), pageableCaptor.capture());
				verifyNoInteractions(districtHeatingHourRepositoryMock);
				verifyNoInteractions(districtHeatingMonthRepositoryMock);
			}
			case MONTH -> {
				verify(districtHeatingMonthRepositoryMock).findAllMatching(any(), any(), any(), any(), pageableCaptor.capture());
				verifyNoInteractions(districtHeatingHourRepositoryMock);
				verifyNoInteractions(districtHeatingDayRepositoryMock);
			}
			default -> throw new IllegalArgumentException("Untested aggregateOn value: " + aggregateOn);
		}

		assertThat(response.getMetaData().getCount()).isZero();
		assertThat(response.getMetaData().getLimit()).isEqualTo(100);
		assertThat(response.getMetaData().getPage()).isEqualTo(2);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(1);
		assertThat(response.getMeasurements()).isEmpty();

	}

	@Test
	void testProblemIsThrownWhenNotSupportedAggregation() {
		final var searchParams = MeasurementParameters.create();

		ThrowableProblem e = assertThrows(ThrowableProblem.class, () -> provider.getMeasurements(null, QUARTER, null, null, searchParams));
		assertThat(e.getStatus()).isEqualTo(NOT_IMPLEMENTED);
		assertThat(e.getMessage()).isEqualTo("Not Implemented: aggregation 'QUARTER' and category 'DISTRICT_HEATING'");

		verifyNoInteractions(districtHeatingMonthRepositoryMock);
	}
}
