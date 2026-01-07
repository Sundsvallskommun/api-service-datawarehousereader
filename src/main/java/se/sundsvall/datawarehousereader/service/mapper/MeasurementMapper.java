package se.sundsvall.datawarehousereader.service.mapper;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static se.sundsvall.dept44.util.DateUtils.toOffsetDateTimeWithLocalOffset;

import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.DefaultMeasurementAttributesInterface;

public class MeasurementMapper {

	private MeasurementMapper() {}

	public static List<Measurement> toMeasurements(final List<? extends DefaultMeasurementAttributesInterface> entities, final MeasurementParameters searchParams, final Aggregation aggregation, final Category category) {
		return ofNullable(entities).orElse(emptyList()).stream()
			.filter(Objects::nonNull)
			.map(MeasurementMapper::toMeasurement)
			.map(measurement -> decorateMeasurement(measurement, searchParams.getPartyId(), aggregation, category))
			.toList();
	}

	public static Measurement toMeasurement(final DefaultMeasurementAttributesInterface entity) {
		return Measurement.create()
			.withUnit(entity.getUnit())
			.withMeasurementType(entity.getFeedType())
			.withFacilityId(entity.getFacilityId())
			.withUnit(entity.getUnit())
			.withPartyId(entity.getUuid())
			.withInterpolation(entity.getInterpolation())
			.withValue(entity.getUsage())
			.withTimestamp(toOffsetDateTimeWithLocalOffset((entity.getMeasurementTimestamp())));
	}

	public static Measurement decorateMeasurement(final Measurement measurement, final String partyId, final Aggregation aggregation, final Category category) {
		return (measurement.getPartyId() == null) && StringUtils.isNotEmpty(partyId) ? measurement.withPartyId(partyId).withAggregatedOn(aggregation).withCategory(category) : measurement.withAggregatedOn(aggregation).withCategory(category);
	}
}
