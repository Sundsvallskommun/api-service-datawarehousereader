package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceDetail;

import static java.util.stream.Collectors.joining;

/**
 * Reads invoice details from the table valued function {@code kundinfo.fnInvoiceDetails}, which replaced the view
 * {@code kundinfo.vInvoiceDetail} for performance reasons. The function takes an invoice number and, optionally, the
 * organization number of the invoice issuer, and returns the rows of that invoice in presentation order
 * ({@code RowSortOrder}).
 */
@Repository
@CircuitBreaker(name = "invoiceDetailJdbcRepository")
public class InvoiceDetailJdbcRepository {

	private static final String INVOICE_NUMBER = "invoiceNumber";
	private static final String ORGANIZATION_NUMBER = "organizationNumber";
	private static final String INVOICE_NUMBERS = "invoiceNumbers";

	private static final String SQL_BY_ORGANIZATION_AND_INVOICE_NUMBER = """
		SELECT details.*
		FROM [kundinfo].[fnInvoiceDetails] ( :invoiceNumber, :organizationNumber ) AS details
		ORDER BY details.RowSortOrder""";

	/**
	 * The function accepts a single invoice number, so the numbers are split into rows and the function is applied to each
	 * of them, keeping the lookup to a single round trip. The organization number is passed as null since it is not known
	 * per invoice here - an invoice number identifies its details on its own.
	 * <p>
	 * Every page size yields this one statement, so without the hint a plan compiled for a page holding a single invoice
	 * would be reused for a page holding a hundred. The JPA lookup this replaced avoided that by carrying @WithRecompile
	 * and padding its IN-clause into a small set of statement shapes; neither reaches a query issued over JDBC, so the
	 * hint is spelled out here instead.
	 */
	private static final String SQL_BY_INVOICE_NUMBERS = """
		SELECT details.*
		FROM STRING_SPLIT(:invoiceNumbers, ',') AS invoice
		CROSS APPLY [kundinfo].[fnInvoiceDetails] ( invoice.value, NULL ) AS details
		ORDER BY details.Invoicenumber, details.RowSortOrder
		OPTION (RECOMPILE)""";

	private static final RowMapper<InvoiceDetail> ROW_MAPPER = (rs, rowNum) -> InvoiceDetail.create()
		.withAdministration(rs.getString("Administration"))
		.withAmount(rs.getBigDecimal("Amount"))
		.withAmountVatExcluded(rs.getBigDecimal("AmountVatExcluded"))
		.withDescription(rs.getString("Description"))
		.withFacilityId(rs.getString("FacilityId"))
		.withInvoiceNumber(getNullableLong(rs, "Invoicenumber"))
		.withInvoiceUnitPrice(rs.getBigDecimal("InvoiceUnitprice"))
		.withInvoiceUnitPriceCurrency(rs.getString("InvoiceUnitpriceCurrency"))
		.withInvoiceUnitPriceUnit(rs.getString("InvoiceUnitpriceunit"))
		.withInvoiceUnitPriceVatExcluded(rs.getBigDecimal("InvoiceUnitpriceVatExcluded"))
		.withOrganizationNumber(rs.getString("OrganizationId"))
		.withPeriodFrom(rs.getString("periodFrom"))
		.withPeriodTo(rs.getString("periodTo"))
		.withProductCode(getNullableInteger(rs, "Productcode"))
		.withProductName(rs.getString("Productname"))
		.withQuantity(getNullableDouble(rs, "Quantity"))
		.withUnit(rs.getString("unit"))
		.withUnitPrice(rs.getBigDecimal("Unitprice"))
		.withUnitPriceVatExcluded(rs.getBigDecimal("UnitpriceVatExcluded"))
		.withVat(rs.getBigDecimal("Vat"))
		.withVatRate(getNullableDouble(rs, "Vatrate"));

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public InvoiceDetailJdbcRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<InvoiceDetail> getInvoiceDetails(final String organizationNumber, final long invoiceNumber) {
		final var parameters = new MapSqlParameterSource()
			.addValue(INVOICE_NUMBER, String.valueOf(invoiceNumber))
			.addValue(ORGANIZATION_NUMBER, organizationNumber);

		return jdbcTemplate.query(SQL_BY_ORGANIZATION_AND_INVOICE_NUMBER, parameters, ROW_MAPPER);
	}

	public List<InvoiceDetail> getInvoiceDetails(final Collection<Long> invoiceNumbers) {
		if (invoiceNumbers.isEmpty()) {
			return List.of();
		}

		final var parameters = new MapSqlParameterSource()
			.addValue(INVOICE_NUMBERS, invoiceNumbers.stream().map(String::valueOf).collect(joining(",")));

		return jdbcTemplate.query(SQL_BY_INVOICE_NUMBERS, parameters, ROW_MAPPER);
	}

	private static Long getNullableLong(final ResultSet rs, final String columnName) throws SQLException {
		final var value = rs.getLong(columnName);
		return rs.wasNull() ? null : value;
	}

	private static Integer getNullableInteger(final ResultSet rs, final String columnName) throws SQLException {
		final var value = rs.getInt(columnName);
		return rs.wasNull() ? null : value;
	}

	private static Double getNullableDouble(final ResultSet rs, final String columnName) throws SQLException {
		final var value = rs.getDouble(columnName);
		return rs.wasNull() ? null : value;
	}
}
