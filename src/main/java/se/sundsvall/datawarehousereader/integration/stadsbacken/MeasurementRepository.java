package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;

@Repository

@CircuitBreaker(name = "measurementRepository")
public class MeasurementRepository {

	private static final String LEGAL_ID = "legalId";
	private static final String FACILITY_IDS = "facilityIds";
	private static final String FROM_DATE = "fromDate";
	private static final String TO_DATE = "toDate";
	private static final String AGGREGATION = "aggregation";
	private static final String DISPLAY = "display";

	private static final String UUID = "uuid";
	private static final String CUSTOMER_ORG_ID = "customerorgid";
	private static final String FACILITY_ID = "facilityId";
	private static final String FEED_TYPE = "feedType";
	private static final String UNIT = "unit";
	private static final String USAGE = "usage";
	private static final String DATE_AND_TIME = "DateAndTime";
	private static final String IS_INTERPOLATED = "isInterpolated";
	private static final String IS_INTERPOLTED = "isInterpolted";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public MeasurementRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Measurement> getElectricityMeasurements(final String legalId, final String facilityId,
		final Aggregation aggregation, final LocalDateTime fromDateTime, final LocalDateTime toDateTime, final String display) {

		var parameters = new MapSqlParameterSource()
			.addValue(LEGAL_ID, legalId)
			.addValue(FACILITY_IDS, facilityId)
			.addValue(FROM_DATE, Timestamp.valueOf(fromDateTime))
			.addValue(TO_DATE, Timestamp.valueOf(toDateTime))
			.addValue(AGGREGATION, aggregation != null ? aggregation.name() : null)
			.addValue(DISPLAY, display);

		return jdbcTemplate.query(
			"{call kundinfo.spMeasurementElectricity(:legalId, :facilityIds, :fromDate, :toDate, :aggregation, :display)}",
			parameters, new ElectricityMeasurementMapper(aggregation));
	}

	public List<Measurement> getDistrictHeatingMeasurements(final String legalId, final String facilityId,
		final Aggregation aggregation, final LocalDateTime fromDateTime, final LocalDateTime toDateTime, final String display) {

		var parameters = new MapSqlParameterSource()
			.addValue(LEGAL_ID, legalId)
			.addValue(FACILITY_IDS, facilityId)
			.addValue(FROM_DATE, Timestamp.valueOf(fromDateTime))
			.addValue(TO_DATE, Timestamp.valueOf(toDateTime))
			.addValue(AGGREGATION, aggregation != null ? aggregation.name() : null)
			.addValue(DISPLAY, display);

		return jdbcTemplate.query(
			"{call kundinfo.spMeasurementDistrictHeating(:legalId, :facilityIds, :fromDate, :toDate, :aggregation, :display)}",
			parameters, new DistrictHeatingMeasurementMapper(aggregation));
	}

	public List<Measurement> getDistrictCoolingMeasurements(final String legalId, final String facilityId,
		final Aggregation aggregation, final LocalDateTime fromDateTime, final LocalDateTime toDateTime, final String display) {

		var parameters = new MapSqlParameterSource()
			.addValue(LEGAL_ID, legalId)
			.addValue(FACILITY_IDS, facilityId)
			.addValue(FROM_DATE, Timestamp.valueOf(fromDateTime))
			.addValue(TO_DATE, Timestamp.valueOf(toDateTime))
			.addValue(AGGREGATION, aggregation != null ? aggregation.name() : null)
			.addValue(DISPLAY, display);

		return jdbcTemplate.query(
			"{call kundinfo.spMeasurementDistrictCooling(:legalId, :facilityIds, :fromDate, :toDate, :aggregation, :display)}",
			parameters, new DistrictCoolingMeasurementMapper(aggregation));
	}

	static class DistrictCoolingMeasurementMapper implements RowMapper<Measurement> {
		private final Aggregation aggregation;

		DistrictCoolingMeasurementMapper(final Aggregation aggregation) {
			this.aggregation = aggregation;
		}

		@Override
		public Measurement mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
			return Measurement.create()
				.withUuid(resultSet.getString(UUID))
				.withCustomerOrgId(resultSet.getString(CUSTOMER_ORG_ID))
				.withFacilityId(resultSet.getString(FACILITY_ID))
				.withFeedType(resultSet.getString(FEED_TYPE))
				.withUnit(resultSet.getString(UNIT))
				.withUsage(resultSet.getBigDecimal(USAGE))
				.withDateAndTime(resultSet.getTimestamp(DATE_AND_TIME).toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime())
				.withInterpolation(getInterpolation(resultSet));
		}

		private Integer getInterpolation(final ResultSet resultSet) throws SQLException {
			return switch (aggregation) {
				case HOUR, QUARTER -> 0;
				case MONTH -> resultSet.getInt(IS_INTERPOLATED);
				case DAY -> resultSet.getInt(IS_INTERPOLTED);
			};
		}
	}

	static class DistrictHeatingMeasurementMapper implements RowMapper<Measurement> {
		private final Aggregation aggregation;

		DistrictHeatingMeasurementMapper(final Aggregation aggregation) {
			this.aggregation = aggregation;
		}

		@Override
		public Measurement mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
			return Measurement.create()
				.withUuid(resultSet.getString(UUID))
				.withCustomerOrgId(resultSet.getString(CUSTOMER_ORG_ID))
				.withFacilityId(resultSet.getString(FACILITY_ID))
				.withFeedType(resultSet.getString(FEED_TYPE))
				.withUnit(resultSet.getString(UNIT))
				.withUsage(resultSet.getBigDecimal(USAGE))
				.withDateAndTime(resultSet.getTimestamp(DATE_AND_TIME).toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime())
				.withInterpolation(getInterpolation(resultSet));
		}

		private Integer getInterpolation(final ResultSet resultSet) throws SQLException {
			return switch (aggregation) {
				case HOUR, QUARTER -> 0;
				case MONTH -> resultSet.getInt(IS_INTERPOLATED);
				case DAY -> resultSet.getInt(IS_INTERPOLTED);
			};
		}
	}

	static class ElectricityMeasurementMapper implements RowMapper<Measurement> {
		private final Aggregation aggregation;

		ElectricityMeasurementMapper(final Aggregation aggregation) {
			this.aggregation = aggregation;
		}

		@Override
		public Measurement mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
			return Measurement.create()
				.withUuid(resultSet.getString(UUID))
				.withCustomerOrgId(resultSet.getString(CUSTOMER_ORG_ID))
				.withFacilityId(resultSet.getString(FACILITY_ID))
				.withFeedType(resultSet.getString(FEED_TYPE))
				.withUnit(resultSet.getString(UNIT))
				.withUsage(resultSet.getBigDecimal(USAGE))
				.withDateAndTime(resultSet.getTimestamp(DATE_AND_TIME).toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime())
				.withInterpolation(getInterpolation(resultSet));
		}

		private Integer getInterpolation(final ResultSet resultSet) throws SQLException {
			return switch (aggregation) {
				case HOUR -> 0;
				case MONTH -> resultSet.getInt(IS_INTERPOLATED);
				case DAY, QUARTER -> resultSet.getInt(IS_INTERPOLTED);
			};
		}
	}
}
