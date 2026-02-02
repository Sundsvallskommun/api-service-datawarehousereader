package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;

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
		final var facilityId = "735999109170208042";
		final var fromDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
		final var toDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
			.thenReturn(List.of());

		repository.getElectricityMeasurements(legalId, facilityId, aggregation, fromDateTime, toDateTime);

		verify(jdbcTemplateMock).query(
			eq("{call kundinfo.spMeasurementElectricity(:legalId, :facilityId, :fromDate, :toDate, :aggregation)}"),
			parametersCaptor.capture(),
			any(MeasurementRepository.ElectricityMeasurementMapper.class));

		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getValue("legalId")).isEqualTo(legalId);
		assertThat(parameters.getValue("facilityId")).isEqualTo(facilityId);
		assertThat(parameters.getValue("fromDate")).isEqualTo(Timestamp.valueOf(fromDateTime));
		assertThat(parameters.getValue("toDate")).isEqualTo(Timestamp.valueOf(toDateTime));
		assertThat(parameters.getValue("aggregation")).isEqualTo(aggregation.name());
	}

	@Test
	void getElectricityMeasurementsWithNullAggregation() {
		final var legalId = "5534567890";
		final var facilityId = "735999109170208042";
		final var fromDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
		final var toDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
			.thenReturn(List.of());

		repository.getElectricityMeasurements(legalId, facilityId, null, fromDateTime, toDateTime);

		verify(jdbcTemplateMock).query(
			eq("{call kundinfo.spMeasurementElectricity(:legalId, :facilityId, :fromDate, :toDate, :aggregation)}"),
			parametersCaptor.capture(),
			any(MeasurementRepository.ElectricityMeasurementMapper.class));

		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getValue("aggregation")).isNull();
	}

	@ParameterizedTest
	@EnumSource(Aggregation.class)
	void getDistrictHeatingMeasurementsCallsCorrectStoredProcedure(Aggregation aggregation) {
		final var legalId = "5591561234";
		final var facilityId = "9115803075";
		final var fromDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
		final var toDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements(legalId, facilityId, aggregation, fromDateTime, toDateTime);

		verify(jdbcTemplateMock).query(
			eq("{call kundinfo.spMeasurementDistrictHeating(:legalId, :facilityId, :fromDate, :toDate, :aggregation)}"),
			parametersCaptor.capture(),
			any(MeasurementRepository.DistrictHeatingMeasurementMapper.class));

		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getValue("legalId")).isEqualTo(legalId);
		assertThat(parameters.getValue("facilityId")).isEqualTo(facilityId);
		assertThat(parameters.getValue("fromDate")).isEqualTo(Timestamp.valueOf(fromDateTime));
		assertThat(parameters.getValue("toDate")).isEqualTo(Timestamp.valueOf(toDateTime));
		assertThat(parameters.getValue("aggregation")).isEqualTo(aggregation.name());
	}

	@Test
	void getDistrictHeatingMeasurementsWithNullAggregation() {
		final var legalId = "5591561234";
		final var facilityId = "9115803075";
		final var fromDateTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
		final var toDateTime = LocalDateTime.of(2022, 12, 31, 23, 59, 59);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements(legalId, facilityId, null, fromDateTime, toDateTime);

		verify(jdbcTemplateMock).query(
			eq("{call kundinfo.spMeasurementDistrictHeating(:legalId, :facilityId, :fromDate, :toDate, :aggregation)}"),
			parametersCaptor.capture(),
			any(MeasurementRepository.DistrictHeatingMeasurementMapper.class));

		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getValue("aggregation")).isNull();
	}

	@Test
	void electricityMeasurementMapperMapsResultSetCorrectlyForHourAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 4, 11, 10, 0, 0));
		setupResultSetMock("uuid-123", "5534567890", "facilityId", "Energy", "kWh", 100, expectedTimestamp);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getElectricityMeasurements("legalId", "facilityId", Aggregation.HOUR, LocalDateTime.now(), LocalDateTime.now());

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getUuid()).isEqualTo("uuid-123");
		assertThat(result.getCustomerOrgId()).isEqualTo("5534567890");
		assertThat(result.getFacilityId()).isEqualTo("facilityId");
		assertThat(result.getFeedType()).isEqualTo("Energy");
		assertThat(result.getUnit()).isEqualTo("kWh");
		assertThat(result.getUsage()).isEqualTo(100);
		assertThat(result.getInterpolation()).isNull();
		assertThat(result.getDateAndTime()).isEqualTo(expectedTimestamp.toLocalDateTime());
	}

	@Test
	void electricityMeasurementMapperMapsResultSetCorrectlyForDayAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 4, 11, 0, 0, 0));
		setupResultSetMock("uuid-123", "5534567890", "facilityId", "Energy", "kWh", 100, expectedTimestamp);
		when(resultSetMock.getInt("interpolted")).thenReturn(24);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getElectricityMeasurements("legalId", "facilityId", Aggregation.DAY, LocalDateTime.now(), LocalDateTime.now());

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isEqualTo(24);
	}

	@Test
	void electricityMeasurementMapperMapsResultSetCorrectlyForQuarterAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 1, 1, 0, 0, 0));
		setupResultSetMock("uuid-123", "5534567890", "facilityId", "Energy", "kWh", 100, expectedTimestamp);
		when(resultSetMock.getInt("interpolted")).thenReturn(5);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getElectricityMeasurements("legalId", "facilityId", Aggregation.QUARTER, LocalDateTime.now(), LocalDateTime.now());

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isEqualTo(5);
	}

	@Test
	void electricityMeasurementMapperMapsResultSetCorrectlyForMonthAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 1, 1, 0, 0, 0));
		setupResultSetMock("uuid-123", "5534567890", "facilityId", "Energy", "kWh", 100, expectedTimestamp);
		when(resultSetMock.getInt("interpolated")).thenReturn(0);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getElectricityMeasurements("legalId", "facilityId", Aggregation.MONTH, LocalDateTime.now(), LocalDateTime.now());

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isEqualTo(0);
	}

	@Test
	void districtHeatingMeasurementMapperMapsResultSetCorrectlyForHourAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 1, 1, 10, 0, 0));
		setupResultSetMock("uuid-456", "5591561234", "9115803075", "Aktiv", "kWh", 7910, expectedTimestamp);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements("legalId", "facilityId", Aggregation.HOUR, LocalDateTime.now(), LocalDateTime.now());

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getUuid()).isEqualTo("uuid-456");
		assertThat(result.getCustomerOrgId()).isEqualTo("5591561234");
		assertThat(result.getFacilityId()).isEqualTo("9115803075");
		assertThat(result.getFeedType()).isEqualTo("Aktiv");
		assertThat(result.getUnit()).isEqualTo("kWh");
		assertThat(result.getUsage()).isEqualTo(7910);
		assertThat(result.getInterpolation()).isNull();
		assertThat(result.getDateAndTime()).isEqualTo(expectedTimestamp.toLocalDateTime());
	}

	@Test
	void districtHeatingMeasurementMapperMapsResultSetCorrectlyForQuarterAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 1, 1, 10, 0, 0));
		setupResultSetMock("uuid-456", "5591561234", "9115803075", "Aktiv", "kWh", 7910, expectedTimestamp);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements("legalId", "facilityId", Aggregation.QUARTER, LocalDateTime.now(), LocalDateTime.now());

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isNull();
	}

	@Test
	void districtHeatingMeasurementMapperMapsResultSetCorrectlyForDayAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2022, 3, 23, 0, 0, 0));
		setupResultSetMock("uuid-789", "5566661234", "9261219043", "energy", "kWh", 1393, expectedTimestamp);
		when(resultSetMock.getInt("interpolted")).thenReturn(0);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements("legalId", "facilityId", Aggregation.DAY, LocalDateTime.now(), LocalDateTime.now());

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isEqualTo(0);
	}

	@Test
	void districtHeatingMeasurementMapperMapsResultSetCorrectlyForMonthAggregation() throws SQLException {
		final var expectedTimestamp = Timestamp.valueOf(LocalDateTime.of(2018, 2, 1, 0, 0, 0));
		setupResultSetMock("uuid-abc", "5534567890", "735999109113202014", "Aktiv", "kWh", 1772, expectedTimestamp);
		when(resultSetMock.getInt("interpolated")).thenReturn(0);

		when(jdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), rowMapperCaptor.capture()))
			.thenReturn(List.of());

		repository.getDistrictHeatingMeasurements("legalId", "facilityId", Aggregation.MONTH, LocalDateTime.now(), LocalDateTime.now());

		final var mapper = rowMapperCaptor.getValue();
		final var result = mapper.mapRow(resultSetMock, 0);

		assertThat(result.getInterpolation()).isEqualTo(0);
	}

	private void setupResultSetMock(String uuid, String customerOrgId, String facilityId, String feedType, String unit, int usage, Timestamp dateAndTime) throws SQLException {
		when(resultSetMock.getString("uuid")).thenReturn(uuid);
		when(resultSetMock.getString("customerorgid")).thenReturn(customerOrgId);
		when(resultSetMock.getString("facilityId")).thenReturn(facilityId);
		when(resultSetMock.getString("feedType")).thenReturn(feedType);
		when(resultSetMock.getString("unit")).thenReturn(unit);
		when(resultSetMock.getInt("usage")).thenReturn(usage);
		when(resultSetMock.getTimestamp("DateAndTime")).thenReturn(dateAndTime);
	}
}
