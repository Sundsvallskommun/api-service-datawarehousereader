package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoiceResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceJdbcRepository.CustomerInvoiceResponseExtractor;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceJdbcRepository.InvoiceRowMapper;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
			final var periodFrom = LocalDate.of(2025, Month.JANUARY, 1);
			final var periodTo = LocalDate.of(2025, Month.DECEMBER, 31);
			final var facilityIds = "123456789012345670";
			final var invoiceStatus = "Betalad";

			final var query = CustomerInvoiceQuery.create()
				.withPage(2)
				.withLimit(10)
				.withOrganizationIds(organizationIds)
				.withCustomerIds(customerIds)
				.withPeriodFrom(periodFrom)
				.withPeriodTo(periodTo)
				.withSortBy(List.of("InvoiceNumber"))
				.withSortDirection(Sort.Direction.DESC)
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
			// InvoiceNumber is also the tie breaker, so it appears once with the requested direction
			assertThat(sqlCaptor.getValue()).contains("ORDER BY inner_query.InvoiceNumber DESC")
				.doesNotContain("inner_query.InvoiceNumber DESC, inner_query.InvoiceNumber");
		}

		@Test
		void getInvoices_withDescendingDirection_appendsDescToPrimaryColumnAndKeepsTieBreakerAscending() {
			when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any()))
				.thenReturn(CustomerInvoiceResponse.create());

			repository.getInvoices(CustomerInvoiceQuery.create()
				.withPage(1)
				.withLimit(10)
				.withCustomerIds("123456")
				.withSortBy(List.of("periodFrom"))
				.withSortDirection(Sort.Direction.DESC));

			verify(jdbcTemplate).query(
				sqlCaptor.capture(),
				parametersCaptor.capture(),
				ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any());

			assertThat(sqlCaptor.getValue()).contains("ORDER BY inner_query.periodFrom DESC, inner_query.InvoiceNumber");
		}

		@Test
		void getInvoices_withMultipleSortColumns_buildsMultiColumnOrderByAndPassesPrimaryToFunction() {
			when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any()))
				.thenReturn(CustomerInvoiceResponse.create());

			repository.getInvoices(CustomerInvoiceQuery.create()
				.withPage(1)
				.withLimit(10)
				.withCustomerIds("123456")
				.withSortBy(List.of("periodFrom", "invoicedate"))
				.withSortDirection(Sort.Direction.DESC));

			verify(jdbcTemplate).query(
				sqlCaptor.capture(),
				parametersCaptor.capture(),
				ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any());

			assertThat(sqlCaptor.getValue())
				.contains("ORDER BY inner_query.periodFrom DESC, inner_query.InvoiceDate DESC, inner_query.InvoiceNumber");
			// the SQL function only takes a single sort column, so it receives the primary (first) one
			assertThat(parametersCaptor.getValue().getValue("sortBy")).isEqualTo("periodFrom");
		}

		@Test
		void getInvoices_withNullDirection_defaultsToAscending() {
			when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any()))
				.thenReturn(CustomerInvoiceResponse.create());

			repository.getInvoices(CustomerInvoiceQuery.create()
				.withPage(1)
				.withLimit(10)
				.withCustomerIds("123456")
				.withSortBy(List.of("periodTo")));

			verify(jdbcTemplate).query(
				sqlCaptor.capture(),
				parametersCaptor.capture(),
				ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any());

			assertThat(sqlCaptor.getValue()).contains("ORDER BY inner_query.periodTo ASC, inner_query.InvoiceNumber");
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
				.withSortBy(List.of("garble")));

			verify(jdbcTemplate).query(
				sqlCaptor.capture(),
				parametersCaptor.capture(),
				ArgumentMatchers.<ResultSetExtractor<CustomerInvoiceResponse>>any());

			assertThat(parametersCaptor.getValue().getValue("sortBy")).isEqualTo("periodFrom");
			assertThat(sqlCaptor.getValue()).contains("ORDER BY inner_query.periodFrom");
		}
	}

	@Nested
	class ResolveSortColumns {

		@ParameterizedTest
		@CsvSource({
			"periodFrom, periodFrom",
			"PERIODTO, periodTo",
			"invoicedate, InvoiceDate",
			"DueDate, DueDate",
			"invoicenumber, InvoiceNumber",
			"totalamount, TotalAmount",
			"' periodFrom ', periodFrom"
		})
		void mapsValueToWhitelistedColumn(final String input, final String expected) {
			assertThat(InvoiceJdbcRepository.resolveSortColumns(List.of(input))).containsExactly(expected);
		}

		@Test
		void keepsAllWhitelistedColumnsInRequestedOrder() {
			assertThat(InvoiceJdbcRepository.resolveSortColumns(List.of("periodFrom", "invoicedate", "DUEDATE")))
				.containsExactly("periodFrom", "InvoiceDate", "DueDate");
		}

		@Test
		void dropsUnknownColumnsButKeepsValidOnes() {
			assertThat(InvoiceJdbcRepository.resolveSortColumns(List.of("periodFrom", "garble", "totalamount")))
				.containsExactly("periodFrom", "TotalAmount");
		}

		@Test
		void ignoresNullElements() {
			assertThat(InvoiceJdbcRepository.resolveSortColumns(Arrays.asList("periodFrom", null)))
				.containsExactly("periodFrom");
		}

		@ParameterizedTest
		@ValueSource(strings = {
			"garble", "   "
		})
		void defaultsToPeriodFromWhenNoValidColumn(final String input) {
			assertThat(InvoiceJdbcRepository.resolveSortColumns(List.of(input))).containsExactly("periodFrom");
		}

		@Test
		void defaultsToPeriodFromWhenNullOrEmpty() {
			assertThat(InvoiceJdbcRepository.resolveSortColumns(null)).containsExactly("periodFrom");
			assertThat(InvoiceJdbcRepository.resolveSortColumns(List.of())).containsExactly("periodFrom");
		}
	}

	@Nested
	class ResolveSortDirection {

		@ParameterizedTest
		@EnumSource(Sort.Direction.class)
		void keepsSuppliedDirection(final Sort.Direction direction) {
			assertThat(InvoiceJdbcRepository.resolveSortDirection(direction)).isEqualTo(direction);
		}

		@Test
		void defaultsNullToAscending() {
			assertThat(InvoiceJdbcRepository.resolveSortDirection(null)).isEqualTo(Sort.Direction.ASC);
		}
	}

	@Nested
	class ExtractData {

		@Mock
		private ResultSet resultSet;

		@Test
		void extractData_withMultipleRows_returnsItemsAndPagination() throws SQLException {
			final var extractor = new CustomerInvoiceResponseExtractor(1, 10, List.of("periodFrom"), Sort.Direction.ASC);

			when(resultSet.next()).thenReturn(true, true, false);

			when(resultSet.getString("CustomerId")).thenReturn("123456", "123456");
			when(resultSet.getString("CustomerType")).thenReturn("Private", "Private");
			when(resultSet.getString("FacilityId")).thenReturn("facility1,facility2", "facility3");
			when(resultSet.getLong("InvoiceNumber")).thenReturn(295334999L, 60003118415L);
			when(resultSet.getLong("InvoiceID")).thenReturn(0L, 1062916396L);
			when(resultSet.getLong("JointInvoiceid")).thenReturn(-1L, 0L);
			when(resultSet.wasNull()).thenReturn(false, true, false, true, false, false, false, false);
			when(resultSet.getDate("InvoiceDate")).thenReturn(Date.valueOf(LocalDate.of(2025, Month.OCTOBER, 8)), Date.valueOf(LocalDate.of(2025, Month.NOVEMBER, 10)));
			when(resultSet.getString("InvoiceName")).thenReturn("295334999.pdf", "60003118415.pdf");
			when(resultSet.getString("InvoiceType")).thenReturn("Faktura", "Faktura");
			when(resultSet.getString("InvoiceDescription")).thenReturn("El", "El");
			when(resultSet.getString("InvoiceStatus")).thenReturn("Betalad", "Betalad");
			when(resultSet.getLong("OcrNumber")).thenReturn(295334999L, 60003118415L);
			when(resultSet.getDate("DueDate")).thenReturn(Date.valueOf(LocalDate.of(2025, Month.OCTOBER, 28)), Date.valueOf(LocalDate.of(2025, Month.DECEMBER, 1)));
			when(resultSet.getDate("periodFrom")).thenReturn(Date.valueOf(LocalDate.of(2025, Month.SEPTEMBER, 1)), Date.valueOf(LocalDate.of(2025, Month.SEPTEMBER, 30)));
			when(resultSet.getDate("periodTo")).thenReturn(Date.valueOf(LocalDate.of(2025, Month.SEPTEMBER, 30)), Date.valueOf(LocalDate.of(2025, Month.OCTOBER, 31)));
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
			assertThat(first.getPeriodFrom()).isEqualTo(LocalDate.of(2025, Month.SEPTEMBER, 1));
			assertThat(first.getPeriodTo()).isEqualTo(LocalDate.of(2025, Month.SEPTEMBER, 30));

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
			assertThat(meta.getSortDirection()).isEqualTo(Sort.Direction.ASC);
		}

		@Test
		void extractData_withEmptyResultSet_returnsEmptyListAndZeroPagination() throws SQLException {
			final var extractor = new CustomerInvoiceResponseExtractor(1, 10, List.of("periodFrom"), Sort.Direction.ASC);
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
			assertThat(result.getMetaData().getSortDirection()).isEqualTo(Sort.Direction.ASC);
		}

		@Test
		void extractData_withNullSortBy_returnsNullSortByInMetadata() throws SQLException {
			final var extractor = new CustomerInvoiceResponseExtractor(1, 10, null, null);
			when(resultSet.next()).thenReturn(false);

			final var result = extractor.extractData(resultSet);

			assertThat(result.getMetaData().getSortBy()).isNull();
			assertThat(result.getMetaData().getSortDirection()).isNull();
		}

		@Test
		void extractData_withNullDatesAndNumbers_mapsToNulls() throws SQLException {
			final var extractor = new CustomerInvoiceResponseExtractor(1, 10, null, null);
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
			final var extractor = new CustomerInvoiceResponseExtractor(2, 5, List.of("periodFrom"), Sort.Direction.DESC);
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
			assertThat(result.getMetaData().getSortDirection()).isEqualTo(Sort.Direction.DESC);
		}
	}

	@Nested
	class FindAllByInvoiceNumberIn {

		@Test
		void withInvoiceNumbers_runsRecompiledInQueryAndReturnsRows() {
			final var invoiceNumbers = List.of(138023999L, 139349898L);
			final var rows = List.of(new InvoiceEntity());

			when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), ArgumentMatchers.<RowMapper<InvoiceEntity>>any()))
				.thenReturn(rows);

			final var result = repository.findAllByInvoiceNumberIn(invoiceNumbers);

			verify(jdbcTemplate).query(sqlCaptor.capture(), parametersCaptor.capture(), ArgumentMatchers.<RowMapper<InvoiceEntity>>any());

			assertThat(result).isSameAs(rows);
			assertThat(sqlCaptor.getValue())
				.contains("[kundinfo].[vInvoice_Test_251126]")
				.contains("WHERE InvoiceNumber IN (:invoiceNumbers)")
				.contains("OPTION (RECOMPILE)");
			assertThat(parametersCaptor.getValue().getValue("invoiceNumbers")).isEqualTo(invoiceNumbers);
		}

		@Test
		void withEmptyList_returnsEmptyAndSkipsQuery() {
			assertThat(repository.findAllByInvoiceNumberIn(List.of())).isEmpty();
			verifyNoInteractions(jdbcTemplate);
		}

		@Test
		void withNull_returnsEmptyAndSkipsQuery() {
			assertThat(repository.findAllByInvoiceNumberIn(null)).isEmpty();
			verifyNoInteractions(jdbcTemplate);
		}
	}

	@Nested
	class InvoiceRowMapperTest {

		@Mock
		private ResultSet resultSet;

		@Test
		void mapRow_mapsAllColumns() throws SQLException {
			when(resultSet.getLong("InvoiceNumber")).thenReturn(137968392L);
			when(resultSet.getLong("OcrNumber")).thenReturn(137968392L);
			when(resultSet.wasNull()).thenReturn(false);
			when(resultSet.getDate("InvoiceDate")).thenReturn(Date.valueOf(LocalDate.of(2019, Month.OCTOBER, 9)));
			when(resultSet.getDate("DueDate")).thenReturn(Date.valueOf(LocalDate.of(2019, Month.OCTOBER, 29)));
			when(resultSet.getString("InvoiceName")).thenReturn("137968392.pdf");
			when(resultSet.getString("InvoiceType")).thenReturn("Faktura");
			when(resultSet.getString("InvoiceDescription")).thenReturn("El");
			when(resultSet.getString("InvoiceStatus")).thenReturn("Skickad");
			when(resultSet.getBigDecimal("TotalAmount")).thenReturn(new BigDecimal("12060.0000"));
			when(resultSet.getBigDecimal("AmountVatIncluded")).thenReturn(new BigDecimal("12059.99"));
			when(resultSet.getBigDecimal("AmountVatExcluded")).thenReturn(new BigDecimal("9647.99"));
			when(resultSet.getBigDecimal("VatEligiblaAmount")).thenReturn(new BigDecimal("9647.99"));
			when(resultSet.getBigDecimal("Rounding")).thenReturn(new BigDecimal("0.01"));
			when(resultSet.getBigDecimal("Vat")).thenReturn(new BigDecimal("9647.99"));
			when(resultSet.getString("ReversedVat")).thenReturn("false");
			when(resultSet.getString("Currency")).thenReturn("sek");
			when(resultSet.getInt("CustomerId")).thenReturn(600606);
			when(resultSet.getString("CustomerType")).thenReturn("Enterprise");
			when(resultSet.getString("FacilityId")).thenReturn("735999109144630886");
			when(resultSet.getString("OrganizationGroup")).thenReturn("stadsbacken");
			when(resultSet.getString("Administration")).thenReturn("Sundsvall Elnät");
			when(resultSet.getString("OrganizationId")).thenReturn("5565027223");
			when(resultSet.getString("Street")).thenReturn("DB79B,  FE 11040");
			when(resultSet.getString("PostalCode")).thenReturn("83882");
			when(resultSet.getString("City")).thenReturn("FRÖSÖN");
			when(resultSet.getString("CareOf")).thenReturn("Fräscha fastigheter AB");
			when(resultSet.getString("PdfAvailable")).thenReturn("true");

			final var entity = new InvoiceRowMapper().mapRow(resultSet, 0);

			assertThat(entity.getInvoiceNumber()).isEqualTo(137968392L);
			assertThat(entity.getOcrNumber()).isEqualTo(137968392L);
			assertThat(entity.getInvoiceDate()).isEqualTo(LocalDate.of(2019, Month.OCTOBER, 9));
			assertThat(entity.getDueDate()).isEqualTo(LocalDate.of(2019, Month.OCTOBER, 29));
			assertThat(entity.getInvoiceName()).isEqualTo("137968392.pdf");
			assertThat(entity.getInvoiceType()).isEqualTo("Faktura");
			assertThat(entity.getInvoiceDescription()).isEqualTo("El");
			assertThat(entity.getInvoiceStatus()).isEqualTo("Skickad");
			assertThat(entity.getTotalAmount()).isEqualByComparingTo("12060.00");
			assertThat(entity.getAmountVatIncluded()).isEqualByComparingTo("12059.99");
			assertThat(entity.getAmountVatExcluded()).isEqualByComparingTo("9647.99");
			assertThat(entity.getVatEligibleAmount()).isEqualByComparingTo("9647.99");
			assertThat(entity.getRounding()).isEqualByComparingTo("0.01");
			assertThat(entity.getVat()).isEqualByComparingTo("9647.99");
			assertThat(entity.getReversedVat()).isFalse();
			assertThat(entity.getCurrency()).isEqualTo("sek");
			assertThat(entity.getCustomerId()).isEqualTo(600606);
			assertThat(entity.getCustomerType()).isEqualTo("Enterprise");
			assertThat(entity.getFacilityId()).isEqualTo("735999109144630886");
			assertThat(entity.getOrganizationGroup()).isEqualTo("stadsbacken");
			assertThat(entity.getAdministration()).isEqualTo("Sundsvall Elnät");
			assertThat(entity.getOrganizationId()).isEqualTo("5565027223");
			assertThat(entity.getStreet()).isEqualTo("DB79B,  FE 11040");
			assertThat(entity.getPostCode()).isEqualTo("83882");
			assertThat(entity.getCity()).isEqualTo("FRÖSÖN");
			assertThat(entity.getCareOf()).isEqualTo("Fräscha fastigheter AB");
			assertThat(entity.getPdfAvailable()).isTrue();
		}

		@Test
		void mapRow_withNullOcrNumber_mapsToNull() throws SQLException {
			when(resultSet.getLong(anyString())).thenReturn(0L);
			when(resultSet.wasNull()).thenReturn(true);

			final var entity = new InvoiceRowMapper().mapRow(resultSet, 0);

			assertThat(entity.getOcrNumber()).isNull();
		}

		// The view stores the boolean flags as strings, including the literal text 'NULL'. Anything other than 'true'/'1'
		// resolves to false, mirroring the previous Hibernate mapping (verified against the 'NULL'-valued fixture invoice).
		@ParameterizedTest
		@CsvSource(nullValues = "java-null", value = {
			"true, true",
			"TRUE, true",
			"false, false",
			"NULL, false",
			"1, true",
			"0, false",
			"java-null, false"
		})
		void mapRow_parsesBooleanFlags(final String stored, final boolean expected) throws SQLException {
			when(resultSet.getString(anyString())).thenReturn(stored);

			final var entity = new InvoiceRowMapper().mapRow(resultSet, 0);

			assertThat(entity.getReversedVat()).isEqualTo(expected);
			assertThat(entity.getPdfAvailable()).isEqualTo(expected);
		}
	}
}
