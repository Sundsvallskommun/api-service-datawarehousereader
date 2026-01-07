package se.sundsvall.datawarehousereader.service.logic;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static se.sundsvall.datawarehousereader.api.model.Category.DISTRICT_HEATING;
import static se.sundsvall.datawarehousereader.service.mapper.MeasurementMapper.decorateMeasurement;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementDistrictHeatingRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.DefaultMeasurementAttributesInterface;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementDistrictHeatingEntity;
import se.sundsvall.datawarehousereader.service.mapper.MeasurementMapper;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

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
		final String aggregationLevel = switch (aggregation) {
			case QUARTER -> "QUARTER";
			case HOUR -> "HOUR";
			case DAY -> "DAY";
			case MONTH -> "MONTH";
		};

		// Get all results from the stored procedure
		final List<MeasurementDistrictHeatingEntity> allResults = districtHeatingRepository.findAllMatching(
			legalId,
			searchParams.getFacilityId(),
			fromDateTime,
			toDateTime,
			aggregationLevel);

		// Apply manual pagination
		final int totalElements = allResults.size();
		final int totalPages = (int) Math.ceil(
			(double) totalElements / searchParams.getLimit());
		final int startIndex = (searchParams.getPage() - 1) * searchParams.getLimit();
		final int endIndex = Math.min(
			startIndex + searchParams.getLimit(),
			totalElements);

		// If page larger than last page is requested, return an empty list
		final List<? extends DefaultMeasurementAttributesInterface> pageContent = searchParams.getPage() > totalPages
			? Collections.emptyList()
			: allResults.subList(startIndex, endIndex);

		final List<Measurement> measurements = toMeasurements(
			pageContent,
			searchParams,
			aggregation);

		// Create manual page info
		final var pageInfo = new org.springframework.data.domain.PageImpl<>(
			pageContent,
			PageRequest.of(
				searchParams.getPage() - 1,
				searchParams.getLimit(),
				searchParams.sort()),
			totalElements);

		return MeasurementResponse.create()
			.withMeasurements(measurements)
			.withMetaData(
				PagingAndSortingMetaData.create().withPageData(pageInfo));
	}

	private List<Measurement> toMeasurements(
		final List<? extends DefaultMeasurementAttributesInterface> entities,
		final MeasurementParameters searchParams,
		final Aggregation aggregation) {
		return ofNullable(entities)
			.orElse(emptyList())
			.stream()
			.filter(Objects::nonNull)
			.map(entity -> toMeasurement((MeasurementDistrictHeatingEntity) entity))
			.map(measurement -> decorateMeasurement(
				measurement,
				searchParams.getPartyId(),
				aggregation,
				DISTRICT_HEATING))
			.toList();
	}

	private Measurement toMeasurement(
		final MeasurementDistrictHeatingEntity entity) {
		return MeasurementMapper.toMeasurement(entity);
	}
}
