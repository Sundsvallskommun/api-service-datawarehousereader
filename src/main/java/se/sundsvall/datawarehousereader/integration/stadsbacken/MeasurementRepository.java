package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;

@Repository
public class MeasurementRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public MeasurementRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Measurement> getElectricityMeasurements(final String legalId, final String facilityId,
		final Aggregation aggregation, final LocalDateTime fromDateTime, final LocalDateTime toDateTime) {

		var parameters = new MapSqlParameterSource()
			.addValue("legalId", legalId)
			.addValue("facilityId", facilityId)
			.addValue("fromDate", Timestamp.valueOf(fromDateTime))
			.addValue("toDate", Timestamp.valueOf(toDateTime))
			.addValue("aggregation", aggregation != null ? aggregation.name() : null);

		return jdbcTemplate.query(
			"{call kundinfo.spMeasurementElectricity(:legalId, :facilityId, :fromDate, :toDate, :aggregation)}",
			parameters, new ElectricityMeasurementMapper(aggregation));
	}

	public List<Measurement> getDistrictHeatingMeasurements(final String legalId, final String facilityId,
		final Aggregation aggregation, final LocalDateTime fromDateTime, final LocalDateTime toDateTime) {

		var parameters = new MapSqlParameterSource()
			.addValue("legalId", legalId)
			.addValue("facilityId", facilityId)
			.addValue("fromDate", Timestamp.valueOf(fromDateTime))
			.addValue("toDate", Timestamp.valueOf(toDateTime))
			.addValue("aggregation", aggregation != null ? aggregation.name() : null);

		return jdbcTemplate.query(
			"{call kundinfo.spMeasurementDistrictHeating(:legalId, :facilityId, :fromDate, :toDate, :aggregation)}",
			parameters, new DistrictHeatingMeasurementMapper(aggregation));
	}

	static class DistrictHeatingMeasurementMapper implements RowMapper<Measurement> {
		private final Aggregation aggregation;

		DistrictHeatingMeasurementMapper(final Aggregation aggregation) {
			this.aggregation = aggregation;
		}

		@Override
		public Measurement mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
			return Measurement.create()
				.withUuid(resultSet.getString("uuid"))
				.withCustomerOrgId(resultSet.getString("customerorgid"))
				.withFacilityId(resultSet.getString("facilityId"))
				.withFeedType(resultSet.getString("feedType"))
				.withUnit(resultSet.getString("unit"))
				.withUsage(resultSet.getBigDecimal("usage"))
				.withInterpolation(getInterpolation(resultSet))
				.withDateAndTime(resultSet.getTimestamp("DateAndTime").toInstant().atOffset(ZoneOffset.UTC));
		}

		private Integer getInterpolation(final ResultSet resultSet) throws SQLException {
			return switch (aggregation) {
				case HOUR, QUARTER -> 0;
				case MONTH -> resultSet.getInt("isInterpolated");
				case DAY -> resultSet.getInt("isInterpolted");
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
				.withUuid(resultSet.getString("uuid"))
				.withCustomerOrgId(resultSet.getString("customerorgid"))
				.withFacilityId(resultSet.getString("facilityId"))
				.withFeedType(resultSet.getString("feedType"))
				.withUnit(resultSet.getString("unit"))
				.withUsage(resultSet.getBigDecimal("usage"))
				.withInterpolation(getInterpolation(resultSet))
				.withDateAndTime(resultSet.getTimestamp("DateAndTime").toInstant().atOffset(ZoneOffset.UTC));
		}

		private Integer getInterpolation(final ResultSet resultSet) throws SQLException {
			return switch (aggregation) {
				case HOUR -> 0;
				case MONTH -> resultSet.getInt("isInterpolated");
				case DAY, QUARTER -> resultSet.getInt("isInterpolted");
			};
		}
	}
}
