package se.sundsvall.datawarehousereader.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static se.sundsvall.datawarehousereader.api.model.Category.ELECTRICITY;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.DAY;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.MONTH;
import static se.sundsvall.datawarehousereader.service.mapper.MeasurementMapper.toMeasurements;
import static se.sundsvall.dept44.util.DateUtils.toOffsetDateTimeWithLocalOffset;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityMonthEntity;

class MeasurementMapperTest {
	private static final Category CATEGORY_ELECTRICITY = ELECTRICITY;
	private static final Aggregation AGGREGATION_MONTH = MONTH;
	private static final String FACILITY_ID = "facilityId";
	private static final String UNIT = "unit";
	private static final LocalDateTime TIME_STAMP_LOCAL_DATE_TIME = LocalDateTime.now();
	private static final String CUSTOMER_ORG_ID = "customerOrgId";
	private static final String FEED_TYPE = "feedType";
	private static final Integer INTERPOLATION = 543210;
	private static final BigDecimal USAGE = BigDecimal.valueOf(123.456);
	private static final String UUID = "uuid";

	@Test
	void toMeasurementsWithNull() {
		assertThat(toMeasurements(null, null, null, null)).isEmpty();
	}

	@Test
	void toMeasurementsWithEmptyList() {
		assertThat(toMeasurements(Collections.emptyList(), MeasurementParameters.create(), DAY, ELECTRICITY)).isEmpty();
	}

	@Test
	void toMeasurementsTest() {
		final var entity = createEntity();

		final var searchParams = MeasurementParameters.create();
		searchParams.setPartyId("partyId");

		final var result = toMeasurements(List.of(entity), searchParams, AGGREGATION_MONTH, CATEGORY_ELECTRICITY);

		assertThat(result)
			.hasSize(1)
			.extracting(
				Measurement::getCategory,
				Measurement::getAggregatedOn,
				Measurement::getPartyId,
				Measurement::getFacilityId,
				Measurement::getMeasurementType,
				Measurement::getUnit,
				Measurement::getInterpolation,
				Measurement::getValue,
				Measurement::getTimestamp,
				Measurement::getMetaData)
			.containsExactly(tuple(
				CATEGORY_ELECTRICITY,
				AGGREGATION_MONTH,
				UUID,
				FACILITY_ID,
				FEED_TYPE,
				UNIT,
				INTERPOLATION,
				USAGE,
				toOffsetDateTimeWithLocalOffset(TIME_STAMP_LOCAL_DATE_TIME),
				null));
	}

	private MeasurementElectricityMonthEntity createEntity() {
		final var entity = new MeasurementElectricityMonthEntity();
		entity.setUnit(UNIT);
		entity.setFacilityId(FACILITY_ID);
		entity.setMeasurementTimestamp(TIME_STAMP_LOCAL_DATE_TIME);
		entity.setFacilityId(FACILITY_ID);
		entity.setUnit(UNIT);
		entity.setInterpolation(INTERPOLATION);
		entity.setUsage(USAGE);
		entity.setCustomerOrgId(CUSTOMER_ORG_ID);
		entity.setFeedType(FEED_TYPE);
		entity.setUuid(UUID);
		return entity;
	}
}
