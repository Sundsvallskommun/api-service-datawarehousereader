package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoiceResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceJdbcRepository.CustomerInvoiceResponseExtractor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceJdbcRepositoryTest {

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;

	@InjectMocks
	private InvoiceJdbcRepository repository;

	@Captor
	private ArgumentCaptor<MapSqlParameterSource> parametersCaptor;

	@Captor
	private ArgumentCaptor<String> sqlCaptor;

	@Nested
	class GetInvoices {

		@Test
		void getInvoices_withAllParameters_passesCorrectParameters() {
			final var organizationIds = "5565027223,5564786647";
			final var customerIds = "123456,600606";
			final var periodFrom = LocalDate.of(2025, 1, 1);
			final var periodTo = LocalDate.of(2025, 12, 31);
			final var facilityIds = "123456789012345670";
			final var invoiceStatus = "Betalad";

			final var query = CustomerInvoiceQuery.create()
				.withPage(2)
				.withLimit(10)
				.withOrganizationIds(organizationIds)
				.withCustomerIds(customerIds)
				.withPeriodFrom(periodFrom)
				.withPeriodTo(periodTo)
				.withSortBy("InvoiceNumber")
				.withFacilityIds(facilityIds)
				.withStatus(invoiceStatus);

			when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any()))
				.thenReturn(CustomerInvoiceResponse.create());

			repository.getInvoices(query);

			verify(jdbcTemplate).query(
				sqlCaptor.capture(),
				parametersCaptor.capture(),
				ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any());

			final var params = parametersCaptor.getValue();
			assertThat(params.getValue("functionPageNumber")).isEqualTo(1);
			assertThat(params.getValue("functionPageSize")).isEqualTo(1_000_000);
			assertThat(params.getValue("organizationIds")).isEqualTo(organizationIds);
			assertThat(params.getValue("customerIds")).isEqualTo(customerIds);
			assertThat(params.getValue("periodFrom")).isEqualTo(periodFrom);
			assertThat(params.getValue("periodTo")).isEqualTo(periodTo);
			assertThat(params.getValue("sortBy")).isEqualTo("InvoiceNumber");
			assertThat(params.getValue("facilityIds")).isEqualTo(facilityIds);
			assertThat(params.getValue("invoiceStatus")).isEqualTo(invoiceStatus);
			assertThat(params.getValue("offset")).isEqualTo(10);
			assertThat(params.getValue("fetch")).isEqualTo(10);
			assertThat(sqlCaptor.getValue()).contains("ORDER BY inner_query.InvoiceNumber");
		}

		@Test
		void getInvoices_withNullOptionalParameters_passesNullAndDefaultedValues() {
			when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any()))
				.thenReturn(CustomerInvoiceResponse.create());

			repository.getInvoices(CustomerInvoiceQuery.create()
				.withPage(1)
				.withLimit(10)
				.withCustomerIds("123456"));

			verify(jdbcTemplate).query(
				sqlCaptor.capture(),
				parametersCaptor.capture(),
				ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any());

			final var params = parametersCaptor.getValue();
			assertThat(params.getValue("organizationIds")).isNull();
			assertThat(params.getValue("customerIds")).isEqualTo("123456");
			assertThat(params.getValue("periodFrom")).isNull();
			assertThat(params.getValue("periodTo")).isNull();
			assertThat(params.getValue("facilityIds")).isNull();
			assertThat(params.getValue("invoiceStatus")).isNull();
			assertThat(params.getValue("offset")).isEqualTo(0);
			assertThat(params.getValue("fetch")).isEqualTo(10);
			// null sortBy is defaulted to periodFrom for both the function argument and the order by
			assertThat(params.getValue("sortBy")).isEqualTo("periodFrom");
			assertThat(sqlCaptor.getValue()).contains("ORDER BY inner_query.periodFrom");
		}

		@Test
		void getInvoices_withInvalidSortBy_defaultsToPeriodFrom() {
			when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any()))
				.thenReturn(CustomerInvoiceResponse.create());

			repository.getInvoices(CustomerInvoiceQuery.create()
				.withPage(1)
				.withLimit(10)
				.withCustomerIds("123456")
				.withSortBy("garble"));

			verify(jdbcTemplate).query(
				sqlCaptor.capture(),
				parametersCaptor.capture(),
				ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any());

			assertThat(parametersCaptor.getValue().getValue("sortBy")).isEqualTo("periodFrom");
			assertThat(sqlCaptor.getValue()).contains("ORDER BY inner_query.periodFrom");
		}
	}

	@Nested
	class ResolveSortColumn {

		@ParameterizedTest
		@CsvSource({
			"periodFrom, periodFrom",
			"PERIODTO, periodTo",
			"invoicedate, InvoiceDate",
			"DueDate, DueDate",
			"invoicenumber, InvoiceNumber",
			"totalamount, TotalAmount",
			"' periodFrom ', periodFrom",
			"garble, periodFrom"
		})
		void resolvesToWhitelistedColumnOrDefault(final String input, final String expected) {
			assertThat(InvoiceJdbcRepository.resolveSortColumn(input)).isEqualTo(expected);
		}

		@ParameterizedTest
		@NullAndEmptySource
		@ValueSource(strings = {
			"   "
		})
		void resolvesNullOrBlankToDefault(final String input) {
			assertThat(InvoiceJdbcRepository.resolveSortColumn(input)).isEqualTo("periodFrom");
		}
	}

	@Nested
	class ExtractData {

		@Mock
		private ResultSet resultSet;

		@Test
		void extractData_withMultipleRows_returnsItemsAndPagination() throws SQLException {
			final var extractor = new CustomerInvoiceResponseExtractor(1, 10, "periodFrom");

			when(resultSet.next()).thenReturn(true, true, false);

			when(resultSet.getString("CustomerId")).thenReturn("123456", "123456");
			when(resultSet.getString("CustomerType")).thenReturn("Private", "Private");
			when(resultSet.getString("FacilityId")).thenReturn("facility1,facility2", "facility3");
			when(resultSet.getLong("InvoiceNumber")).thenReturn(295334999L, 60003118415L);
			when(resultSet.getLong("InvoiceID")).thenReturn(0L, 1062916396L);
			when(resultSet.getLong("JointInvoiceid")).thenReturn(-1L, 0L);
			when(resultSet.wasNull()).thenReturn(false, true, false, true, false, false, false, false);
			when(resultSet.getDate("InvoiceDate")).thenReturn(Date.valueOf(LocalDate.of(2025, 10, 8)), Date.valueOf(LocalDate.of(2025, 11, 10)));
			when(resultSet.getString("InvoiceName")).thenReturn("295334999.pdf", "60003118415.pdf");
			when(resultSet.getString("InvoiceType")).thenReturn("Faktura", "Faktura");
			when(resultSet.getString("InvoiceDescription")).thenReturn("El", "El");
			when(resultSet.getString("InvoiceStatus")).thenReturn("Betalad", "Betalad");
			when(resultSet.getLong("OcrNumber")).thenReturn(295334999L, 60003118415L);
			when(resultSet.getDate("DueDate")).thenReturn(Date.valueOf(LocalDate.of(2025, 10, 28)), Date.valueOf(LocalDate.of(2025, 12, 1)));
			when(resultSet.getDate("periodFrom")).thenReturn(Date.valueOf(LocalDate.of(2025, 9, 1)), Date.valueOf(LocalDate.of(2025, 9, 30)));
			when(resultSet.getDate("periodTo")).thenReturn(Date.valueOf(LocalDate.of(2025, 9, 30)), Date.valueOf(LocalDate.of(2025, 10, 31)));
			when(resultSet.getBigDecimal("TotalAmount")).thenReturn(new BigDecimal("1234.00"), new BigDecimal("1940.00"));
			when(resultSet.getBigDecimal("AmountVatIncluded")).thenReturn(new BigDecimal("1233.51"), new BigDecimal("1940.00"));
			when(resultSet.getBigDecimal("AmountVatExcluded")).thenReturn(new BigDecimal("986.81"), new BigDecimal("1552.05"));
			when(resultSet.getBigDecimal("VatEligiblaAmount")).thenReturn(new BigDecimal("986.81"), new BigDecimal("1552.05"));
			when(resultSet.getBigDecimal("Rounding")).thenReturn(new BigDecimal("0.49"), new BigDecimal("-0.07"));
			when(resultSet.getString("OrganizationGroup")).thenReturn("stadsbacken", "stadsbacken");
			when(resultSet.getString("OrganizationId")).thenReturn("5565027223", "5564786647");
			when(resultSet.getString("Administration")).thenReturn("Sundsvall Elnät", "Sundsvall Energi AB");
			when(resultSet.getString("Street")).thenReturn("Fuxvägen 11", "Fuxvägen 11");
			when(resultSet.getString("PostalCode")).thenReturn("85752", "85752");
			when(resultSet.getString("City")).thenReturn("Sundsvall", "SUNDSVALL");
			when(resultSet.getString("CareOf")).thenReturn("Sjöqvist Nils", "Nils Sjöqvist");
			when(resultSet.getString("InvoiceReference")).thenReturn(null, (String) null);
			when(resultSet.getBoolean("pdfAvailable")).thenReturn(false, true);

			when(resultSet.getInt("FilteredTotalRecords")).thenReturn(23);

			final var result = extractor.extractData(resultSet);

			assertThat(result).isNotNull();
			assertThat(result.getInvoices()).hasSize(2);

			final var first = result.getInvoices().getFirst();
			assertThat(first.getCustomerNumber()).isEqualTo("123456");
			assertThat(first.getCustomerType()).isEqualTo(CustomerType.PRIVATE);
			assertThat(first.getInvoiceNumber()).isEqualTo(295334999L);
			assertThat(first.getOrganizationNumber()).isEqualTo("5565027223");
			assertThat(first.getFacilityIds()).containsExactly("facility1", "facility2");
			assertThat(first.getPeriodFrom()).isEqualTo(LocalDate.of(2025, 9, 1));
			assertThat(first.getPeriodTo()).isEqualTo(LocalDate.of(2025, 9, 30));

			final var second = result.getInvoices().get(1);
			assertThat(second.getOrganizationNumber()).isEqualTo("5564786647");
			assertThat(second.getInvoiceNumber()).isEqualTo(60003118415L);
			assertThat(second.getFacilityIds()).containsExactly("facility3");

			final var meta = result.getMetaData();
			assertThat(meta.getPage()).isEqualTo(1);
			assertThat(meta.getLimit()).isEqualTo(10);
			assertThat(meta.getCount()).isEqualTo(2);
			assertThat(meta.getTotalRecords()).isEqualTo(23);
			assertThat(meta.getTotalPages()).isEqualTo(3);
			assertThat(meta.getSortBy()).containsExactly("periodFrom");
		}

		@Test
		void extractData_withEmptyResultSet_returnsEmptyListAndZeroPagination() throws SQLException {
			final var extractor = new CustomerInvoiceResponseExtractor(1, 10, "periodFrom");
			when(resultSet.next()).thenReturn(false);

			final var result = extractor.extractData(resultSet);

			assertThat(result).isNotNull();
			assertThat(result.getInvoices()).isEmpty();
			assertThat(result.getMetaData().getPage()).isEqualTo(1);
			assertThat(result.getMetaData().getLimit()).isEqualTo(10);
			assertThat(result.getMetaData().getCount()).isZero();
			assertThat(result.getMetaData().getTotalRecords()).isZero();
			assertThat(result.getMetaData().getTotalPages()).isZero();
			assertThat(result.getMetaData().getSortBy()).containsExactly("periodFrom");
		}

		@Test
		void extractData_withNullSortBy_returnsNullSortByInMetadata() throws SQLException {
			final var extractor = new CustomerInvoiceResponseExtractor(1, 10, null);
			when(resultSet.next()).thenReturn(false);

			final var result = extractor.extractData(resultSet);

			assertThat(result.getMetaData().getSortBy()).isNull();
		}

		@Test
		void extractData_withNullDatesAndNumbers_mapsToNulls() throws SQLException {
			final var extractor = new CustomerInvoiceResponseExtractor(1, 10, null);
			when(resultSet.next()).thenReturn(true, false);

			when(resultSet.getString("CustomerId")).thenReturn("123456");
			when(resultSet.getString("CustomerType")).thenReturn("Private");
			when(resultSet.getString("FacilityId")).thenReturn(null);
			when(resultSet.getLong(anyString())).thenReturn(0L);
			when(resultSet.wasNull()).thenReturn(true);
			when(resultSet.getDate(anyString())).thenReturn(null);
			when(resultSet.getString("InvoiceName")).thenReturn(null);
			when(resultSet.getString("InvoiceType")).thenReturn(null);
			when(resultSet.getString("InvoiceDescription")).thenReturn(null);
			when(resultSet.getString("InvoiceStatus")).thenReturn(null);
			when(resultSet.getBigDecimal(anyString())).thenReturn(null);
			when(resultSet.getString("OrganizationGroup")).thenReturn(null);
			when(resultSet.getString("OrganizationId")).thenReturn(null);
			when(resultSet.getString("Administration")).thenReturn(null);
			when(resultSet.getString("Street")).thenReturn(null);
			when(resultSet.getString("PostalCode")).thenReturn(null);
			when(resultSet.getString("City")).thenReturn(null);
			when(resultSet.getString("CareOf")).thenReturn(null);
			when(resultSet.getString("InvoiceReference")).thenReturn(null);
			when(resultSet.getBoolean("pdfAvailable")).thenReturn(false);
			when(resultSet.getInt("FilteredTotalRecords")).thenReturn(1);

			final var result = extractor.extractData(resultSet);

			final var item = result.getInvoices().getFirst();
			assertThat(item.getFacilityIds()).isEmpty();
			assertThat(item.getInvoiceNumber()).isNull();
			assertThat(item.getInvoiceId()).isNull();
			assertThat(item.getJointInvoiceId()).isNull();
			assertThat(item.getOcrNumber()).isNull();
			assertThat(item.getInvoiceDate()).isNull();
			assertThat(item.getDueDate()).isNull();
			assertThat(item.getPeriodFrom()).isNull();
			assertThat(item.getPeriodTo()).isNull();
			assertThat(item.getPdfAvailable()).isNull();
			assertThat(item.getTotalAmount()).isNull();
		}

		@Test
		void extractData_computesPaginationFromFilteredTotal() throws SQLException {
			final var extractor = new CustomerInvoiceResponseExtractor(2, 5, "periodFrom");
			when(resultSet.next()).thenReturn(true, false);

			when(resultSet.getInt("FilteredTotalRecords")).thenReturn(42);

			final var result = extractor.extractData(resultSet);

			assertThat(result.getInvoices()).hasSize(1);
			assertThat(result.getMetaData().getPage()).isEqualTo(2);
			assertThat(result.getMetaData().getLimit()).isEqualTo(5);
			assertThat(result.getMetaData().getCount()).isEqualTo(1);
			assertThat(result.getMetaData().getTotalRecords()).isEqualTo(42);
			assertThat(result.getMetaData().getTotalPages()).isEqualTo(9);
			assertThat(result.getMetaData().getSortBy()).isEqualTo(List.of("periodFrom"));
		}
	}
}
