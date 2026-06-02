package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoice;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoiceResponse;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static se.sundsvall.datawarehousereader.Constants.UNKNOWN_CUSTOMER_TYPE;
import static se.sundsvall.datawarehousereader.api.model.CustomerType.fromValue;

@Repository
@CircuitBreaker(name = "invoiceJdbcRepository")
public class InvoiceJdbcRepository {

	private static final String FUNCTION_PAGE_NUMBER = "functionPageNumber";
	private static final String FUNCTION_PAGE_SIZE = "functionPageSize";
	private static final String ORGANIZATION_IDS = "organizationIds";
	private static final String CUSTOMER_IDS = "customerIds";
	private static final String PERIOD_FROM = "periodFrom";
	private static final String PERIOD_TO = "periodTo";
	private static final String SORT_BY = "sortBy";
	private static final String FACILITY_IDS = "facilityIds";
	private static final String INVOICE_STATUS = "invoiceStatus";
	private static final String OFFSET = "offset";
	private static final String FETCH = "fetch";

	/**
	 * The function pages internally, but status and facility filtering must be applied to the full result set after the
	 * function has run. The function is therefore called for "all" rows (page one, very large page size) and the actual
	 * filtering, sorting and paging is performed by the wrapping query below.
	 */
	private static final int FUNCTION_PAGE_SIZE_VALUE = 1_000_000;

	private static final String DEFAULT_SORT_COLUMN = "periodFrom";

	/**
	 * Whitelist mapping a (lower cased) requested sortBy value to the actual column name used both as function argument and
	 * in the wrapping ORDER BY clause. Anything not present here (including null/blank) falls back to
	 * {@link #DEFAULT_SORT_COLUMN},
	 * which also guards the ORDER BY against SQL injection since the column name is concatenated into the statement.
	 */
	private static final Map<String, String> ALLOWED_SORT_COLUMNS = Map.of(
		"periodfrom", "periodFrom",
		"periodto", "periodTo",
		"invoicedate", "InvoiceDate",
		"duedate", "DueDate",
		"invoicenumber", "InvoiceNumber",
		"totalamount", "TotalAmount");

	private static final String ORDER_BY_PLACEHOLDER = "{orderByColumns}";

	/** Stable tie breaker so paging is deterministic when the primary sort column has duplicate values. */
	private static final String TIE_BREAKER_COLUMN = "inner_query.InvoiceNumber";

	private static final String SQL_TEMPLATE = """
		SELECT inner_query.*, COUNT(*) OVER () AS FilteredTotalRecords
		FROM [kundinfo].[fnInvoiceNumberWithPagingAndSort] ( :functionPageNumber, :functionPageSize, :organizationIds, :customerIds, :periodFrom, :periodTo, :sortBy ) AS inner_query
		WHERE (:invoiceStatus IS NULL OR inner_query.InvoiceStatus = :invoiceStatus)
		  AND (:facilityIds IS NULL OR EXISTS (
		        SELECT 1 FROM STRING_SPLIT(:facilityIds, ',') facility
		        WHERE inner_query.FacilityId LIKE '%' + LTRIM(RTRIM(facility.value)) + '%'))
		ORDER BY {orderByColumns}
		OFFSET :offset ROWS FETCH NEXT :fetch ROWS ONLY""";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public InvoiceJdbcRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public CustomerInvoiceResponse getInvoices(final Integer pageNumber, final Integer pageSize,
		final String organizationIds, final String customerIds,
		final LocalDate periodFrom, final LocalDate periodTo, final String sortBy,
		final String facilityIds, final String invoiceStatus) {

		final var sortColumn = resolveSortColumn(sortBy);
		final var metadataSortBy = isSupplied(sortBy) ? sortColumn : null;

		final var parameters = new MapSqlParameterSource()
			.addValue(FUNCTION_PAGE_NUMBER, 1)
			.addValue(FUNCTION_PAGE_SIZE, FUNCTION_PAGE_SIZE_VALUE)
			.addValue(ORGANIZATION_IDS, organizationIds)
			.addValue(CUSTOMER_IDS, customerIds)
			.addValue(PERIOD_FROM, periodFrom)
			.addValue(PERIOD_TO, periodTo)
			.addValue(SORT_BY, sortColumn)
			.addValue(FACILITY_IDS, facilityIds)
			.addValue(INVOICE_STATUS, invoiceStatus)
			.addValue(OFFSET, (pageNumber - 1) * pageSize)
			.addValue(FETCH, pageSize);

		final var sql = SQL_TEMPLATE.replace(ORDER_BY_PLACEHOLDER, buildOrderByColumns(sortColumn));

		return jdbcTemplate.query(sql, parameters,
			new CustomerInvoiceResponseExtractor(pageNumber, pageSize, metadataSortBy));
	}

	private static String buildOrderByColumns(final String sortColumn) {
		final var primaryColumn = "inner_query." + sortColumn;
		// Avoid "column specified more than once in the order by list" when sorting by the tie breaker itself.
		return primaryColumn.equals(TIE_BREAKER_COLUMN) ? primaryColumn : primaryColumn + ", " + TIE_BREAKER_COLUMN;
	}

	static String resolveSortColumn(final String sortBy) {
		return ofNullable(sortBy)
			.map(value -> value.trim().toLowerCase(Locale.ROOT))
			.map(ALLOWED_SORT_COLUMNS::get)
			.orElse(DEFAULT_SORT_COLUMN);
	}

	private static boolean isSupplied(final String sortBy) {
		return sortBy != null && !sortBy.isBlank();
	}

	static class CustomerInvoiceResponseExtractor implements ResultSetExtractor<CustomerInvoiceResponse> {

		private final int pageNumber;
		private final int pageSize;
		private final String sortBy;

		CustomerInvoiceResponseExtractor(final int pageNumber, final int pageSize, final String sortBy) {
			this.pageNumber = pageNumber;
			this.pageSize = pageSize;
			this.sortBy = sortBy;
		}

		@Override
		public CustomerInvoiceResponse extractData(final ResultSet rs) throws SQLException, DataAccessException {
			final List<CustomerInvoice> items = new ArrayList<>();
			var totalRecords = 0;

			while (rs.next()) {
				items.add(mapRow(rs));
				if (items.size() == 1) {
					totalRecords = rs.getInt("FilteredTotalRecords");
				}
			}

			final var totalPages = pageSize == 0 ? 0 : (int) Math.ceil((double) totalRecords / pageSize);

			final var metaData = PagingAndSortingMetaData.create()
				.withPage(pageNumber)
				.withLimit(pageSize)
				.withCount(items.size())
				.withTotalRecords(totalRecords)
				.withTotalPages(totalPages)
				.withSortBy(sortBy != null ? List.of(sortBy) : null);

			return CustomerInvoiceResponse.create()
				.withMetaData(metaData)
				.withInvoices(items);
		}

		private CustomerInvoice mapRow(final ResultSet rs) throws SQLException {
			return CustomerInvoice.create()
				.withCustomerNumber(rs.getString("CustomerId"))
				.withCustomerType(fromValue(rs.getString("CustomerType"), INTERNAL_SERVER_ERROR, UNKNOWN_CUSTOMER_TYPE))
				.withFacilityId(rs.getString("FacilityId"))
				.withInvoiceNumber(getNullableLong(rs, "InvoiceNumber"))
				.withInvoiceId(getNullableLong(rs, "InvoiceID"))
				.withJointInvoiceId(getNullableLong(rs, "JointInvoiceid"))
				.withInvoiceDate(toLocalDate(rs.getDate("InvoiceDate")))
				.withInvoiceName(rs.getString("InvoiceName"))
				.withInvoiceType(rs.getString("InvoiceType"))
				.withInvoiceDescription(rs.getString("InvoiceDescription"))
				.withInvoiceStatus(rs.getString("InvoiceStatus"))
				.withOcrNumber(getNullableLong(rs, "OcrNumber"))
				.withDueDate(toLocalDate(rs.getDate("DueDate")))
				.withPeriodFrom(toLocalDate(rs.getDate("periodFrom")))
				.withPeriodTo(toLocalDate(rs.getDate("periodTo")))
				.withTotalAmount(rs.getBigDecimal("TotalAmount"))
				.withAmountVatIncluded(rs.getBigDecimal("AmountVatIncluded"))
				.withAmountVatExcluded(rs.getBigDecimal("AmountVatExcluded"))
				.withVatEligibleAmount(rs.getBigDecimal("VatEligiblaAmount"))
				.withRounding(rs.getBigDecimal("Rounding"))
				.withOrganizationGroup(rs.getString("OrganizationGroup"))
				.withOrganizationNumber(rs.getString("OrganizationId"))
				.withAdministration(rs.getString("Administration"))
				.withStreet(rs.getString("Street"))
				.withPostCode(rs.getString("PostalCode"))
				.withCity(rs.getString("City"))
				.withCareOf(rs.getString("CareOf"))
				.withInvoiceReference(rs.getString("InvoiceReference"))
				.withPdfAvailable(getNullableBoolean(rs, "pdfAvailable"));
		}

		private static LocalDate toLocalDate(final java.sql.Date date) {
			return date != null ? date.toLocalDate() : null;
		}

		private static Long getNullableLong(final ResultSet rs, final String columnName) throws SQLException {
			final var value = rs.getLong(columnName);
			return rs.wasNull() ? null : value;
		}

		private static Boolean getNullableBoolean(final ResultSet rs, final String columnName) throws SQLException {
			final var value = rs.getBoolean(columnName);
			return rs.wasNull() ? null : value;
		}
	}
}
