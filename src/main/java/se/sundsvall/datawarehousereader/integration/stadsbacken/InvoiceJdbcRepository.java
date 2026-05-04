package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoice;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoiceResponse;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static se.sundsvall.datawarehousereader.Constants.UNKNOWN_CUSTOMER_TYPE;
import static se.sundsvall.datawarehousereader.api.model.CustomerType.fromValue;

@Repository
@CircuitBreaker(name = "invoiceJdbcRepository")
public class InvoiceJdbcRepository {

	private static final String PAGE_NUMBER = "pageNumber";
	private static final String PAGE_SIZE = "pageSize";
	private static final String ORGANIZATION_IDS = "organizationIds";
	private static final String CUSTOMER_ID = "customerId";
	private static final String PERIOD_FROM = "periodFrom";
	private static final String PERIOD_TO = "periodTo";
	private static final String SORT_BY = "sortBy";

	private static final String SQL = "select * from [kundinfo].[fnInvoiceNumberWithPagingAndSort] "
		+ "( :pageNumber, :pageSize, :organizationIds, :customerId, :periodFrom, :periodTo, :sortBy )";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public InvoiceJdbcRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public CustomerInvoiceResponse getInvoices(final Integer pageNumber, final Integer pageSize,
		final String organizationIds, final String customerId,
		final LocalDate periodFrom, final LocalDate periodTo, final String sortBy) {

		final var parameters = new MapSqlParameterSource()
			.addValue(PAGE_NUMBER, pageNumber)
			.addValue(PAGE_SIZE, pageSize)
			.addValue(ORGANIZATION_IDS, organizationIds)
			.addValue(CUSTOMER_ID, customerId)
			.addValue(PERIOD_FROM, periodFrom)
			.addValue(PERIOD_TO, periodTo)
			.addValue(SORT_BY, sortBy);

		return jdbcTemplate.query(SQL, parameters,
			new CustomerInvoiceResponseExtractor(pageNumber, pageSize, sortBy));
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
			var totalPages = 0;
			var count = 0;

			while (rs.next()) {
				items.add(mapRow(rs));
				if (items.size() == 1) {
					totalRecords = rs.getInt("TotalRecords");
					totalPages = (int) rs.getFloat("TotalPages");
					count = rs.getInt("Count");
				}
			}

			final var metaData = PagingAndSortingMetaData.create()
				.withPage(pageNumber)
				.withLimit(pageSize)
				.withCount(count)
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
