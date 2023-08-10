package se.sundsvall.datawarehousereader.service.logic;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static se.sundsvall.datawarehousereader.api.model.Category.DISTRICT_HEATING;
import static se.sundsvall.datawarehousereader.service.mapper.MeasurementMapper.decorateMeasurement;
import static se.sundsvall.datawarehousereader.service.mapper.MeasurementMapper.toMeasurementResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementMetaData;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementDistrictHeatingDayRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementDistrictHeatingHourRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementDistrictHeatingMonthRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.DefaultMeasurementAttributesInterface;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingDayEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingHourEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingMonthEntity;
import se.sundsvall.datawarehousereader.service.mapper.MeasurementMapper;

@Component
public class DistrictHeatingMeasurementProvider {

	@Autowired
	private MeasurementDistrictHeatingHourRepository districtHeatingHourRepository;

	@Autowired
	private MeasurementDistrictHeatingDayRepository districtHeatingDayRepository;

	@Autowired
	private MeasurementDistrictHeatingMonthRepository districtHeatingMonthRepository;

	private static final String AGGREGATION_NOT_IMPLEMENTED = "aggregation '%s' and category '%s'";
	private static final String READING_SEQUENCE_KEY = "readingSequence";

	public MeasurementResponse getMeasurements(String legalId, Aggregation aggregation, LocalDateTime fromDateTime, LocalDateTime toDateTime, MeasurementParameters searchParams) {
		final var matches = switch (aggregation) {
			case HOUR -> districtHeatingHourRepository.findAllMatching(legalId, searchParams.getFacilityId(), fromDateTime, toDateTime,
				PageRequest.of(searchParams.getPage() - 1, searchParams.getLimit(), searchParams.sort()));
			case DAY -> districtHeatingDayRepository.findAllMatching(legalId, searchParams.getFacilityId(), fromDateTime, toDateTime,
				PageRequest.of(searchParams.getPage() - 1, searchParams.getLimit(), searchParams.sort()));
			case MONTH -> districtHeatingMonthRepository.findAllMatching(legalId, searchParams.getFacilityId(), fromDateTime, toDateTime,
				PageRequest.of(searchParams.getPage() - 1, searchParams.getLimit(), searchParams.sort()));
			default -> throw Problem.valueOf(Status.NOT_IMPLEMENTED, String.format(AGGREGATION_NOT_IMPLEMENTED, aggregation, DISTRICT_HEATING));
		};

		// If page larger than last page is requested, an empty list is returned otherwise the current page
		final List<Measurement> measurements = matches.getTotalPages() < searchParams.getPage() ? Collections.emptyList() : toMeasurements(matches.getContent(), searchParams, aggregation);

		return toMeasurementResponse(searchParams, matches.getTotalPages(), matches.getTotalElements(), measurements);
	}

	private List<Measurement> toMeasurements(List<? extends DefaultMeasurementAttributesInterface> entities, MeasurementParameters searchParams, Aggregation aggregation) {
		return ofNullable(entities).orElse(emptyList()).stream()
			.filter(Objects::nonNull)
			.map(defaultMeasurement -> toMeasurement(defaultMeasurement, aggregation))
			.map(measurement -> decorateMeasurement(measurement, searchParams.getPartyId(), aggregation, DISTRICT_HEATING))
			.toList();
	}

	private Measurement toMeasurement(DefaultMeasurementAttributesInterface entity, Aggregation aggregation) {
		return MeasurementMapper.toMeasurement(entity)
			.withMetaData(toMetadata(entity, aggregation));
	}

	private List<MeasurementMetaData> toMetadata(DefaultMeasurementAttributesInterface entity, Aggregation aggregation) {

		return switch (aggregation) { case HOUR -> { final var hourEntity = (MeasurementDistrictHeatingHourEntity) entity; yield List.of(MeasurementMetaData.create().withKey(READING_SEQUENCE_KEY).withValue(toString(hourEntity.getReadingSequence()))); } case DAY -> { final var dayEntity = (MeasurementDistrictHeatingDayEntity) entity; yield List.of(MeasurementMetaData.create().withKey(READING_SEQUENCE_KEY).withValue(toString(dayEntity.getReadingSequence()))); } case MONTH -> { final var monthEntity = (MeasurementDistrictHeatingMonthEntity) entity; yield List.of(MeasurementMetaData.create().withKey(READING_SEQUENCE_KEY).withValue(toString(monthEntity.getReadingSequence()))); } default -> emptyList(); };
	}

	private String toString(Integer value) {
		return ofNullable(value)
			.map(String::valueOf)
			.orElse(null);
	}
}
