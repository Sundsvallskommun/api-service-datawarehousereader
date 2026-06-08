package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoice;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoiceResponse;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static se.sundsvall.datawarehousereader.Constants.UNKNOWN_CUSTOMER_TYPE;
import static se.sundsvall.datawarehousereader.api.model.CustomerType.fromValue;

// The sortable column names (periodFrom, periodTo, ...) coincidentally match some bind-parameter name constants
// (PERIOD_FROM, PERIOD_TO) but are a different concept - SQL column names - so they are kept as literals like every other
// column name, rather than reusing the parameter constants. Suppresses the resulting duplicate-literal warnings
// (SonarLint java:S1192 / IntelliJ DuplicateStringLiteralInspection).
@SuppressWarnings({
	"java:S1192", "DuplicateStringLiteralInspection"
})
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

	public CustomerInvoiceResponse getInvoices(final CustomerInvoiceQuery query) {

		final var sortColumn = resolveSortColumn(query.getSortBy());
		final var sortDirection = resolveSortDirection(query.getSortDirection());
		final var metadataSortBy = isSupplied(query.getSortBy()) ? sortColumn : null;
		final var metadataSortDirection = metadataSortBy != null ? sortDirection : null;

		final var parameters = new MapSqlParameterSource()
			.addValue(FUNCTION_PAGE_NUMBER, 1)
			.addValue(FUNCTION_PAGE_SIZE, FUNCTION_PAGE_SIZE_VALUE)
			.addValue(ORGANIZATION_IDS, query.getOrganizationIds())
			.addValue(CUSTOMER_IDS, query.getCustomerIds())
			.addValue(PERIOD_FROM, query.getPeriodFrom())
			.addValue(PERIOD_TO, query.getPeriodTo())
			.addValue(SORT_BY, sortColumn)
			.addValue(FACILITY_IDS, query.getFacilityIds())
			.addValue(INVOICE_STATUS, query.getStatus())
			.addValue(OFFSET, (query.getPage() - 1) * query.getLimit())
			.addValue(FETCH, query.getLimit());

		final var sql = SQL_TEMPLATE.replace(ORDER_BY_PLACEHOLDER, buildOrderByColumns(sortColumn, sortDirection));

		return jdbcTemplate.query(sql, parameters,
			new CustomerInvoiceResponseExtractor(query.getPage(), query.getLimit(), metadataSortBy, metadataSortDirection));
	}

	/**
	 * The requested direction is applied to the primary sort column only. The {@link #TIE_BREAKER_COLUMN} is always kept
	 * ascending; it exists solely to make paging deterministic, so its direction is irrelevant. {@code direction.name()}
	 * yields the literal {@code ASC} or {@code DESC}, so concatenating it into the statement is injection safe.
	 */
	private static String buildOrderByColumns(final String sortColumn, final Sort.Direction direction) {
		final var primaryColumn = "inner_query." + sortColumn;
		final var primaryClause = primaryColumn + " " + direction.name();
		// Avoid "column specified more than once in the order by list" when sorting by the tie breaker itself.
		return primaryColumn.equals(TIE_BREAKER_COLUMN) ? primaryClause : primaryClause + ", " + TIE_BREAKER_COLUMN;
	}

	static String resolveSortColumn(final String sortBy) {
		return ofNullable(sortBy)
			.map(value -> value.trim().toLowerCase(Locale.ROOT))
			.map(ALLOWED_SORT_COLUMNS::get)
			.orElse(DEFAULT_SORT_COLUMN);
	}

	/** A null direction (nothing requested) defaults to ascending, mirroring SQL's own default. */
	static Sort.Direction resolveSortDirection(final Sort.Direction sortDirection) {
		return ofNullable(sortDirection).orElse(Sort.DEFAULT_DIRECTION);
	}

	private static boolean isSupplied(final String sortBy) {
		return sortBy != null && !sortBy.isBlank();
	}

	static class CustomerInvoiceResponseExtractor implements ResultSetExtractor<CustomerInvoiceResponse> {

		private final int pageNumber;
		private final int pageSize;
		private final String sortBy;
		private final Sort.Direction sortDirection;

		CustomerInvoiceResponseExtractor(final int pageNumber, final int pageSize, final String sortBy, final Sort.Direction sortDirection) {
			this.pageNumber = pageNumber;
			this.pageSize = pageSize;
			this.sortBy = sortBy;
			this.sortDirection = sortDirection;
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
				.withSortBy(sortBy != null ? List.of(sortBy) : null)
				.withSortDirection(sortDirection);

			return CustomerInvoiceResponse.create()
				.withMetaData(metaData)
				.withInvoices(items);
		}

		private CustomerInvoice mapRow(final ResultSet rs) throws SQLException {
			return CustomerInvoice.create()
				.withCustomerNumber(rs.getString("CustomerId"))
				.withCustomerType(fromValue(rs.getString("CustomerType"), INTERNAL_SERVER_ERROR, UNKNOWN_CUSTOMER_TYPE))
				.withFacilityIds(toFacilityIds(rs.getString("FacilityId")))
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

		/**
		 * The source FacilityId column may pack several facility ids as a comma separated string. Split it into discrete
		 * ids so the response exposes a real list rather than a single comma separated value. Null/blank yields an empty
		 * list.
		 */
		private static List<String> toFacilityIds(final String facilityId) {
			return ofNullable(facilityId)
				.map(value -> Arrays.stream(value.split(","))
					.map(String::trim)
					.filter(not(String::isBlank))
					.toList())
				.orElseGet(List::of);
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
