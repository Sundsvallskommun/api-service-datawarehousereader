package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InstalledBaseJdbcRepository.InstalledBaseResponseExtractor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstalledBaseJdbcRepositoryTest {

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;

	@InjectMocks
	private InstalledBaseJdbcRepository repository;

	@Captor
	private ArgumentCaptor<MapSqlParameterSource> parametersCaptor;

	@Nested
	class GetInstalledBases {

		@Test
		void getInstalledBases_withAllParameters_passesCorrectSqlAndParameters() {
			// given
			final var pageNumber = 1;
			final var pageSize = 100;
			final var organizationIds = "5564786647,5565027223";
			final var date = LocalDate.of(2025, 6, 1);
			final var uuid = "898B3634-A2F9-483C-8808-37F3F25CF24E";
			final var sortBy = "Company";

			when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), ArgumentMatchers.<ResultSetExtractor<InstalledBaseResponse>>any()))
				.thenReturn(InstalledBaseResponse.create());

			// when
			repository.getInstalledBases(pageNumber, pageSize, organizationIds, date, uuid, sortBy);

			// then
			verify(jdbcTemplate).query(
				anyString(),
				parametersCaptor.capture(),
				ArgumentMatchers.<ResultSetExtractor<InstalledBaseResponse>>any());

			final var params = parametersCaptor.getValue();
			assertThat(params.getValue("pageNumber")).isEqualTo(pageNumber);
			assertThat(params.getValue("pageSize")).isEqualTo(pageSize);
			assertThat(params.getValue("organizationIds")).isEqualTo(organizationIds);
			assertThat(params.getValue("sortDate")).isEqualTo(date);
			assertThat(params.getValue("identifier")).isEqualTo(uuid);
			assertThat(params.getValue("sortBy")).isEqualTo(sortBy);
		}

		@Test
		void getInstalledBases_withNullOptionalParameters_passesNullValues() {
			// given
			when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), ArgumentMatchers.<ResultSetExtractor<InstalledBaseResponse>>any()))
				.thenReturn(InstalledBaseResponse.create());

			// when
			repository.getInstalledBases(1, 10, null, null, null, null);

			// then
			verify(jdbcTemplate).query(
				anyString(),
				parametersCaptor.capture(),
				ArgumentMatchers.<ResultSetExtractor<InstalledBaseResponse>>any());

			final var params = parametersCaptor.getValue();
			assertThat(params.getValue("organizationIds")).isNull();
			assertThat(params.getValue("sortDate")).isNull();
			assertThat(params.getValue("identifier")).isNull();
			assertThat(params.getValue("sortBy")).isNull();
		}
	}

	@Nested
	class ExtractData {

		@Mock
		private ResultSet resultSet;

		@Test
		void extractData_withMultipleRows_returnsItemsAndPagination() throws SQLException {
			// given
			final var extractor = new InstalledBaseResponseExtractor(1, 100, "Company");

			when(resultSet.next()).thenReturn(true, true, false);

			// Row data - first row
			when(resultSet.getString("Company")).thenReturn("Sundsvall Energi AB", "Sundsvall Elnät");
			when(resultSet.getString("Customerid")).thenReturn("202646", "202646");
			when(resultSet.getString("Type")).thenReturn("Elhandel", "El");
			when(resultSet.getString("FacilityId")).thenReturn("735999109232306013", "735999109224608200");
			when(resultSet.getInt("internalId")).thenReturn(1037597248, 1037590112);
			when(resultSet.getString("Careof")).thenReturn("Mitthem AB", "Mitthem AB");
			when(resultSet.getString("Street")).thenReturn("Skönsbergsvägen 42C", "Trafikgatan 7B");
			when(resultSet.getString("Postcode")).thenReturn("85630", "85644");
			when(resultSet.getString("City")).thenReturn("SUNDSVALL", "SUNDSVALL");
			when(resultSet.getString("HouseName")).thenReturn("VASA 1", "SPELMANNEN 2");
			when(resultSet.getDate("DateFrom")).thenReturn(Date.valueOf(LocalDate.of(2025, 6, 1)), Date.valueOf(LocalDate.of(1950, 1, 1)));
			when(resultSet.getDate("DateTo")).thenReturn(null, (Date) null);
			when(resultSet.getDate("InstalledBaseLastChangedDate")).thenReturn(Date.valueOf(LocalDate.of(2025, 10, 30)), Date.valueOf(LocalDate.of(2025, 10, 30)));

			// Pagination columns (read from the first row only)
			when(resultSet.getInt("TotalRecords")).thenReturn(2766);
			when(resultSet.getFloat("TotalPages")).thenReturn(28.0f);
			when(resultSet.getInt("Count")).thenReturn(100);

			// when
			final var result = extractor.extractData(resultSet);

			// then
			assertThat(result).isNotNull();
			assertThat(result.getInstalledBase()).hasSize(2);

			final var firstItem = result.getInstalledBase().getFirst();
			assertThat(firstItem.getCompany()).isEqualTo("Sundsvall Energi AB");
			assertThat(firstItem.getCustomerNumber()).isEqualTo("202646");
			assertThat(firstItem.getType()).isEqualTo("Elhandel");
			assertThat(firstItem.getFacilityId()).isEqualTo("735999109232306013");
			assertThat(firstItem.getPlacementId()).isEqualTo(1037597248);
			assertThat(firstItem.getCareOf()).isEqualTo("Mitthem AB");
			assertThat(firstItem.getStreet()).isEqualTo("Skönsbergsvägen 42C");
			assertThat(firstItem.getPostCode()).isEqualTo("85630");
			assertThat(firstItem.getCity()).isEqualTo("SUNDSVALL");
			assertThat(firstItem.getPropertyDesignation()).isEqualTo("VASA 1");
			assertThat(firstItem.getDateFrom()).isEqualTo(LocalDate.of(2025, 6, 1));
			assertThat(firstItem.getDateTo()).isNull();
			assertThat(firstItem.getDateLastModified()).isEqualTo(LocalDate.of(2025, 10, 30));

			final var secondItem = result.getInstalledBase().get(1);
			assertThat(secondItem.getCompany()).isEqualTo("Sundsvall Elnät");
			assertThat(secondItem.getType()).isEqualTo("El");
			assertThat(secondItem.getStreet()).isEqualTo("Trafikgatan 7B");
			assertThat(secondItem.getPropertyDesignation()).isEqualTo("SPELMANNEN 2");
			assertThat(secondItem.getDateFrom()).isEqualTo(LocalDate.of(1950, 1, 1));

			final var metaData = result.getMetaData();
			assertThat(metaData.getPage()).isEqualTo(1);
			assertThat(metaData.getLimit()).isEqualTo(100);
			assertThat(metaData.getCount()).isEqualTo(100);
			assertThat(metaData.getTotalRecords()).isEqualTo(2766);
			assertThat(metaData.getTotalPages()).isEqualTo(28);
			assertThat(metaData.getSortBy()).containsExactly("Company");
		}

		@Test
		void extractData_withEmptyResultSet_returnsEmptyListAndZeroPagination() throws SQLException {
			// given
			final var extractor = new InstalledBaseResponseExtractor(1, 100, "Company");
			when(resultSet.next()).thenReturn(false);

			// when
			final var result = extractor.extractData(resultSet);

			// then
			assertThat(result).isNotNull();
			assertThat(result.getInstalledBase()).isEmpty();
			assertThat(result.getMetaData().getPage()).isEqualTo(1);
			assertThat(result.getMetaData().getLimit()).isEqualTo(100);
			assertThat(result.getMetaData().getCount()).isZero();
			assertThat(result.getMetaData().getTotalRecords()).isZero();
			assertThat(result.getMetaData().getTotalPages()).isZero();
			assertThat(result.getMetaData().getSortBy()).containsExactly("Company");
		}

		@Test
		void extractData_withNullSortBy_returnsNullSortByInMetadata() throws SQLException {
			// given
			final var extractor = new InstalledBaseResponseExtractor(1, 50, null);
			when(resultSet.next()).thenReturn(false);

			// when
			final var result = extractor.extractData(resultSet);

			// then
			assertThat(result.getMetaData().getSortBy()).isNull();
			assertThat(result.getMetaData().getLimit()).isEqualTo(50);
		}

		@Test
		void extractData_withNullDates_mapsToNullLocalDates() throws SQLException {
			// given
			final var extractor = new InstalledBaseResponseExtractor(1, 100, null);
			when(resultSet.next()).thenReturn(true, false);

			when(resultSet.getString("Company")).thenReturn("Test AB");
			when(resultSet.getString("Customerid")).thenReturn("123");
			when(resultSet.getString("Type")).thenReturn("El");
			when(resultSet.getString("FacilityId")).thenReturn("facility1");
			when(resultSet.getInt("internalId")).thenReturn(1);
			when(resultSet.getString("Careof")).thenReturn(null);
			when(resultSet.getString("Street")).thenReturn(null);
			when(resultSet.getString("Postcode")).thenReturn(null);
			when(resultSet.getString("City")).thenReturn(null);
			when(resultSet.getString("HouseName")).thenReturn(null);
			when(resultSet.getDate("DateFrom")).thenReturn(null);
			when(resultSet.getDate("DateTo")).thenReturn(null);
			when(resultSet.getDate("InstalledBaseLastChangedDate")).thenReturn(null);
			when(resultSet.getInt("TotalRecords")).thenReturn(1);
			when(resultSet.getFloat("TotalPages")).thenReturn(1.0f);
			when(resultSet.getInt("Count")).thenReturn(1);

			// when
			final var result = extractor.extractData(resultSet);

			// then
			final var item = result.getInstalledBase().getFirst();
			assertThat(item.getDateFrom()).isNull();
			assertThat(item.getDateTo()).isNull();
			assertThat(item.getDateLastModified()).isNull();
			assertThat(item.getCareOf()).isNull();
			assertThat(item.getStreet()).isNull();
			assertThat(item.getCity()).isNull();
		}

		@Test
		void extractData_withSingleRow_readsPaginationFromThatRow() throws SQLException {
			// given
			final var extractor = new InstalledBaseResponseExtractor(5, 10, "Street");
			when(resultSet.next()).thenReturn(true, false);

			when(resultSet.getString("Company")).thenReturn("Company AB");
			when(resultSet.getString("Customerid")).thenReturn("999");
			when(resultSet.getString("Type")).thenReturn("Fjärrvärme");
			when(resultSet.getString("FacilityId")).thenReturn("fac123");
			when(resultSet.getInt("internalId")).thenReturn(42);
			when(resultSet.getString("Careof")).thenReturn("Care Of");
			when(resultSet.getString("Street")).thenReturn("Main Street 1");
			when(resultSet.getString("Postcode")).thenReturn("12345");
			when(resultSet.getString("City")).thenReturn("Stockholm");
			when(resultSet.getString("HouseName")).thenReturn("House 1");
			when(resultSet.getDate("DateFrom")).thenReturn(Date.valueOf(LocalDate.of(2020, 1, 1)));
			when(resultSet.getDate("DateTo")).thenReturn(Date.valueOf(LocalDate.of(2023, 12, 31)));
			when(resultSet.getDate("InstalledBaseLastChangedDate")).thenReturn(Date.valueOf(LocalDate.of(2023, 6, 15)));
			when(resultSet.getInt("TotalRecords")).thenReturn(42);
			when(resultSet.getFloat("TotalPages")).thenReturn(5.0f);
			when(resultSet.getInt("Count")).thenReturn(10);

			// when
			final var result = extractor.extractData(resultSet);

			// then
			assertThat(result.getInstalledBase()).hasSize(1);
			assertThat(result.getMetaData().getPage()).isEqualTo(5);
			assertThat(result.getMetaData().getLimit()).isEqualTo(10);
			assertThat(result.getMetaData().getTotalRecords()).isEqualTo(42);
			assertThat(result.getMetaData().getTotalPages()).isEqualTo(5);
			assertThat(result.getMetaData().getCount()).isEqualTo(10);
			assertThat(result.getMetaData().getSortBy()).isEqualTo(List.of("Street"));

			final var item = result.getInstalledBase().getFirst();
			assertThat(item.getDateFrom()).isEqualTo(LocalDate.of(2020, 1, 1));
			assertThat(item.getDateTo()).isEqualTo(LocalDate.of(2023, 12, 31));
			assertThat(item.getDateLastModified()).isEqualTo(LocalDate.of(2023, 6, 15));
		}
	}
}
