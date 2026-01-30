package se.sundsvall.datawarehousereader.service.logic;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static se.sundsvall.datawarehousereader.api.model.Category.DISTRICT_HEATING;
import static se.sundsvall.datawarehousereader.service.mapper.MeasurementMapper.decorateMeasurement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementDistrictHeatingRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingEntity;
import se.sundsvall.datawarehousereader.service.mapper.MeasurementMapper;

@Component
public class DistrictHeatingMeasurementProvider {

	private final MeasurementDistrictHeatingRepository districtHeatingRepository;

	DistrictHeatingMeasurementProvider(
		final MeasurementDistrictHeatingRepository districtHeatingRepository) {
		this.districtHeatingRepository = districtHeatingRepository;
	}

	public MeasurementResponse getMeasurements(
		final String legalId,
		final Aggregation aggregation,
		final LocalDateTime fromDateTime,
		final LocalDateTime toDateTime,
		final MeasurementParameters searchParams) {

		final List<MeasurementDistrictHeatingEntity> results = districtHeatingRepository.findAllMatching(
			legalId,
			searchParams.getFacilityId(),
			fromDateTime,
			toDateTime,
			aggregation.toString());

		final List<Measurement> measurements = ofNullable(results)
			.orElse(emptyList())
			.stream()
			.filter(Objects::nonNull)
			.map(MeasurementMapper::toMeasurement)
			.map(measurement -> decorateMeasurement(
				measurement,
				searchParams.getPartyId(),
				aggregation,
				DISTRICT_HEATING))
			.toList();

		return MeasurementResponse.create()
			.withMeasurements(measurements);
	}
}
