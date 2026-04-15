package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeasurementRepositoryTest {

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplateMock;

	@Mock
	private ResultSet resultSetMock;

	@Captor
	private ArgumentCaptor<MapSqlParameterSource> parametersCaptor;

	@Captor
	private ArgumentCaptor<RowMapper<Measurement>> rowMapperCaptor;

	@InjectMocks
	private MeasurementRepository repository;

	@ParameterizedTest
	@EnumSource(Aggregation.class)
	void getElectricityMeasurementsCallsCorrectStoredProcedure(Aggregation aggregation) {
		final var legalId = "5534567890";
		final var facilityIds = "735999109170208042";
		final var fromDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
		final var toDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);
		final var display = "aggregate";

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), any(MeasurementRepository.ElectricityMeasurementMapper.class)))
			.thenReturn(List.of());

		repository.getElectricityMeasurements(legalId, facilityIds, aggregation, fromDateTime, toDateTime, display);

		verify(jdbcTemplateMock).query(
			eq("{call kundinfo.spMeasurementElectricity(:legalId, :facilityIds, :fromDate, :toDate, :aggregation, :display)}"),
			parametersCaptor.capture(),
			any(MeasurementRepository.ElectricityMeasurementMapper.class));

		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getValue("legalId")).isEqualTo(legalId);
		assertThat(parameters.getValue("facilityIds")).isEqualTo(facilityIds);
		assertThat(parameters.getValue("fromDate")).isEqualTo(Timestamp.valueOf(fromDateTime));
		assertThat(parameters.getValue("toDate")).isEqualTo(Timestamp.valueOf(toDateTime));
		assertThat(parameters.getValue("aggregation")).isEqualTo(aggregation.name());
		assertThat(parameters.getValue("display")).isEqualTo(display);
	}

	@Test
	void getElectricityMeasurementsWithNullAggregation() {
		final var legalId = "5534567890";
		final var facilityIds = "735999109170208042";
		final var fromDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
		final var toDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), any(MeasurementRepository.ElectricityMeasurementMapper.class)))
			.thenReturn(List.of());

		repository.getElectricityMeasurements(legalId, facilityIds, null, fromDateTime, toDateTime, null);

		verify(jdbcTemplateMock).query(
			eq("{call kundinfo.spMeasurementElectricity(:legalId, :facilityIds, :fromDate, :toDate, :aggregation, :display)}"),
			parametersCaptor.capture(),
			any(MeasurementRepository.ElectricityMeasurementMapper.class));

		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getValue("aggregation")).isNull();
		assertThat(parameters.getValue("display")).isNull();
	}

	@ParameterizedTest
	@EnumSource(Aggregation.class)
	void getDistrictHeatingMeasurementsCallsCorrectStoredProcedure(Aggregation aggregation) {
		final var legalId = "5591561234";
		final var facilityIds = "9115803075";
		final var fromDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
		final var toDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);
		final var display = "onlyaggregated";

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), any(MeasurementRepository.DistrictHeatingMeasurementMapper.class)))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements(legalId, facilityIds, aggregation, fromDateTime, toDateTime, display);

		verify(jdbcTemplateMock).query(
			eq("{call kundinfo.spMeasurementDistrictHeating(:legalId, :facilityIds, :fromDate, :toDate, :aggregation, :display)}"),
			parametersCaptor.capture(),
			any(MeasurementRepository.DistrictHeatingMeasurementMapper.class));

		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getValue("legalId")).isEqualTo(legalId);
		assertThat(parameters.getValue("facilityIds")).isEqualTo(facilityIds);
		assertThat(parameters.getValue("fromDate")).isEqualTo(Timestamp.valueOf(fromDateTime));
		assertThat(parameters.getValue("toDate")).isEqualTo(Timestamp.valueOf(toDateTime));
		assertThat(parameters.getValue("aggregation")).isEqualTo(aggregation.name());
		assertThat(parameters.getValue("display")).isEqualTo(display);
	}

	@Test
	void getDistrictHeatingMeasurementsWithNullAggregation() {
		final var legalId = "5591561234";
		final var facilityIds = "9115803075";
		final var fromDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
		final var toDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), any(MeasurementRepository.DistrictHeatingMeasurementMapper.class)))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements(legalId, facilityIds, null, fromDateTime, toDateTime, null);

		verify(jdbcTemplateMock).query(
			eq("{call kundinfo.spMeasurementDistrictHeating(:legalId, :facilityIds, :fromDate, :toDate, :aggregation, :display)}"),
			parametersCaptor.capture(),
			any(MeasurementRepository.DistrictHeatingMeasurementMapper.class));

		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getValue("aggregation")).isNull();
		assertThat(parameters.getValue("display")).isNull();
	}

	@Test
	void electricityMeasurementMapperMapsResultSetCorrectlyForHourAggregation() throws SQLException {
		final var expectedInstant = OffsetDateTime.of(2022, 4, 11, 10, 0, 0, 0, ZoneOffset.UTC).toInstant();
		final var expectedOffsetDateTime = expectedInstant.atZone(ZoneId.systemDefault()).toOffsetDateTime();
		final var expectedTimestamp = Timestamp.from(expectedInstant);
		setupResultSetMock("uuid-123", "5534567890", "facilityIds", "Energy", "kWh", BigDecimal.valueOf(100L), expectedTimestamp);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getElectricityMeasurements("legalId", "facilityIds", Aggregation.HOUR, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getUuid()).isEqualTo("uuid-123");
		assertThat(result.getCustomerOrgId()).isEqualTo("5534567890");
		assertThat(result.getFacilityId()).isEqualTo("facilityIds");
		assertThat(result.getFeedType()).isEqualTo("Energy");
		assertThat(result.getUnit()).isEqualTo("kWh");
		assertThat(result.getUsage()).isEqualTo(BigDecimal.valueOf(100L));
		assertThat(result.getInterpolation()).isZero();
		assertThat(result.getDateAndTime()).isEqualTo(expectedOffsetDateTime);
	}

	@Test
	void electricityMeasurementMapperMapsResultSetCorrectlyForDayAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 4, 11, 0, 0, 0));
		setupResultSetMock("uuid-123", "5534567890", "facilityIds", "Energy", "kWh", BigDecimal.valueOf(100L), expectedTimestamp);
		when(resultSetMock.getInt("isInterpolted")).thenReturn(24);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getElectricityMeasurements("legalId", "facilityIds", Aggregation.DAY, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isEqualTo(24);
	}

	@Test
	void electricityMeasurementMapperMapsResultSetCorrectlyForQuarterAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 1, 1, 0, 0, 0));
		setupResultSetMock("uuid-123", "5534567890", "facilityIds", "Energy", "kWh", BigDecimal.valueOf(100L), expectedTimestamp);
		when(resultSetMock.getInt("isInterpolted")).thenReturn(5);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getElectricityMeasurements("legalId", "facilityIds", Aggregation.QUARTER, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isEqualTo(5);
	}

	@Test
	void electricityMeasurementMapperMapsResultSetCorrectlyForMonthAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 1, 1, 0, 0, 0));
		setupResultSetMock("uuid-123", "5534567890", "facilityIds", "Energy", "kWh", BigDecimal.valueOf(100L), expectedTimestamp);
		when(resultSetMock.getInt("isInterpolated")).thenReturn(0);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getElectricityMeasurements("legalId", "facilityIds", Aggregation.MONTH, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isZero();
	}

	@Test
	void districtHeatingMeasurementMapperMapsResultSetCorrectlyForHourAggregation() throws SQLException {
		final var expectedInstant = OffsetDateTime.of(2022, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC).toInstant();
		final var expectedOffsetDateTime = expectedInstant.atZone(ZoneId.systemDefault()).toOffsetDateTime();
		final var expectedTimestamp = Timestamp.from(expectedInstant);
		setupResultSetMock("uuid-456", "5591561234", "9115803075", "Aktiv", "kWh", BigDecimal.valueOf(7910L), expectedTimestamp);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements("legalId", "facilityIds", Aggregation.HOUR, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getUuid()).isEqualTo("uuid-456");
		assertThat(result.getCustomerOrgId()).isEqualTo("5591561234");
		assertThat(result.getFacilityId()).isEqualTo("9115803075");
		assertThat(result.getFeedType()).isEqualTo("Aktiv");
		assertThat(result.getUnit()).isEqualTo("kWh");
		assertThat(result.getUsage()).isEqualTo(BigDecimal.valueOf(7910L));
		assertThat(result.getInterpolation()).isZero();
		assertThat(result.getDateAndTime()).isEqualTo(expectedOffsetDateTime);
	}

	@Test
	void districtHeatingMeasurementMapperMapsResultSetCorrectlyForQuarterAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.from(OffsetDateTime.of(2022, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC).toInstant());
		setupResultSetMock("uuid-456", "5591561234", "9115803075", "Aktiv", "kWh", BigDecimal.valueOf(7910L), expectedTimestamp);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements("legalId", "facilityIds", Aggregation.QUARTER, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isZero();
	}

	@Test
	void districtHeatingMeasurementMapperMapsResultSetCorrectlyForDayAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 3, 23, 0, 0, 0));
		setupResultSetMock("uuid-789", "5566661234", "9261219043", "energy", "kWh", BigDecimal.valueOf(1393L), expectedTimestamp);
		when(resultSetMock.getInt("isInterpolted")).thenReturn(0);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements("legalId", "facilityIds", Aggregation.DAY, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isZero();
	}

	@Test
	void districtHeatingMeasurementMapperMapsResultSetCorrectlyForMonthAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2018, 2, 1, 0, 0, 0));
		setupResultSetMock("uuid-abc", "5534567890", "735999109113202014", "Aktiv", "kWh", BigDecimal.valueOf(1772), expectedTimestamp);
		when(resultSetMock.getInt("isInterpolated")).thenReturn(0);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements("legalId", "facilityIds", Aggregation.MONTH, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isZero();
	}

	@ParameterizedTest
	@EnumSource(Aggregation.class)
	void getDistrictCoolingMeasurementsCallsCorrectStoredProcedure(Aggregation aggregation) {
		final var legalId = "5591561234";
		final var facilityIds = "9115803075";
		final var fromDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
		final var toDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);
		final var display = "onlyaggregated";

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), any(MeasurementRepository.DistrictCoolingMeasurementMapper.class)))
			.thenReturn(List.of());

		repository.getDistrictCoolingMeasurements(legalId, facilityIds, aggregation, fromDateTime, toDateTime, display);

		verify(jdbcTemplateMock).query(
			eq("{call kundinfo.spMeasurementDistrictCooling(:legalId, :facilityIds, :fromDate, :toDate, :aggregation, :display)}"),
			parametersCaptor.capture(),
			any(MeasurementRepository.DistrictCoolingMeasurementMapper.class));

		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getValue("legalId")).isEqualTo(legalId);
		assertThat(parameters.getValue("facilityIds")).isEqualTo(facilityIds);
		assertThat(parameters.getValue("fromDate")).isEqualTo(Timestamp.valueOf(fromDateTime));
		assertThat(parameters.getValue("toDate")).isEqualTo(Timestamp.valueOf(toDateTime));
		assertThat(parameters.getValue("aggregation")).isEqualTo(aggregation.name());
		assertThat(parameters.getValue("display")).isEqualTo(display);
	}

	@Test
	void getDistrictCoolingMeasurementsWithNullAggregation() {
		final var legalId = "5591561234";
		final var facilityIds = "9115803075";
		final var fromDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
		final var toDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), any(MeasurementRepository.DistrictCoolingMeasurementMapper.class)))
			.thenReturn(List.of());

		repository.getDistrictCoolingMeasurements(legalId, facilityIds, null, fromDateTime, toDateTime, null);

		verify(jdbcTemplateMock).query(
			eq("{call kundinfo.spMeasurementDistrictCooling(:legalId, :facilityIds, :fromDate, :toDate, :aggregation, :display)}"),
			parametersCaptor.capture(),
			any(MeasurementRepository.DistrictCoolingMeasurementMapper.class));

		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getValue("aggregation")).isNull();
		assertThat(parameters.getValue("display")).isNull();
	}

	@Test
	void districtCoolingMeasurementMapperMapsResultSetCorrectlyForHourAggregation() throws SQLException {
		final var expectedInstant = OffsetDateTime.of(2022, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC).toInstant();
		final var expectedOffsetDateTime = expectedInstant.atZone(ZoneId.systemDefault()).toOffsetDateTime();
		final var expectedTimestamp = Timestamp.from(expectedInstant);
		setupResultSetMock("uuid-456", "5591561234", "9115803075", "Aktiv", "kWh", BigDecimal.valueOf(7910L), expectedTimestamp);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictCoolingMeasurements("legalId", "facilityIds", Aggregation.HOUR, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getUuid()).isEqualTo("uuid-456");
		assertThat(result.getCustomerOrgId()).isEqualTo("5591561234");
		assertThat(result.getFacilityId()).isEqualTo("9115803075");
		assertThat(result.getFeedType()).isEqualTo("Aktiv");
		assertThat(result.getUnit()).isEqualTo("kWh");
		assertThat(result.getUsage()).isEqualTo(BigDecimal.valueOf(7910L));
		assertThat(result.getInterpolation()).isZero();
		assertThat(result.getDateAndTime()).isEqualTo(expectedOffsetDateTime);
	}

	@Test
	void districtCoolingMeasurementMapperMapsResultSetCorrectlyForQuarterAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.from(OffsetDateTime.of(2022, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC).toInstant());
		setupResultSetMock("uuid-456", "5591561234", "9115803075", "Aktiv", "kWh", BigDecimal.valueOf(7910L), expectedTimestamp);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictCoolingMeasurements("legalId", "facilityIds", Aggregation.QUARTER, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isZero();
	}

	@Test
	void districtCoolingMeasurementMapperMapsResultSetCorrectlyForDayAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 3, 23, 0, 0, 0));
		setupResultSetMock("uuid-789", "5566661234", "9261219043", "energy", "kWh", BigDecimal.valueOf(1393L), expectedTimestamp);
		when(resultSetMock.getInt("isInterpolted")).thenReturn(0);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictCoolingMeasurements("legalId", "facilityIds", Aggregation.DAY, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isZero();
	}

	@Test
	void districtCoolingMeasurementMapperMapsResultSetCorrectlyForMonthAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2018, 2, 1, 0, 0, 0));
		setupResultSetMock("uuid-abc", "5534567890", "735999109113202014", "Aktiv", "kWh", BigDecimal.valueOf(1772), expectedTimestamp);
		when(resultSetMock.getInt("isInterpolated")).thenReturn(0);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictCoolingMeasurements("legalId", "facilityIds", Aggregation.MONTH, LocalDateTime.now(), LocalDateTime.now(), null);

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isZero();
	}

	private void setupResultSetMock(String uuid, String customerOrgId, String facilityIds, String feedType, String unit, BigDecimal usage, Timestamp dateAndTime) throws SQLException {
		when(resultSetMock.getString("uuid")).thenReturn(uuid);
		when(resultSetMock.getString("customerorgid")).thenReturn(customerOrgId);
		when(resultSetMock.getString("facilityId")).thenReturn(facilityIds);
		when(resultSetMock.getString("feedType")).thenReturn(feedType);
		when(resultSetMock.getString("unit")).thenReturn(unit);
		when(resultSetMock.getBigDecimal("usage")).thenReturn(usage);
		when(resultSetMock.getTimestamp("DateAndTime")).thenReturn(dateAndTime);
	}
}
