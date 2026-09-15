package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceDetail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace.NONE;

/**
 * Invoice detail repository tests.
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup, and
 *      src/test/resources/db/scripts/functions.sql for the stand in for kundinfo.fnInvoiceDetails.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
@Import(InvoiceDetailJdbcRepository.class)
class InvoiceDetailJdbcRepositoryTest {

	@Autowired
	private InvoiceDetailJdbcRepository repository;

	@Test
	void getInvoiceDetailsNoMatch() {
		assertThat(repository.getInvoiceDetails("5565027223", 999999999)).isEmpty();
	}

	@Test
	void getInvoiceDetailsForOtherOrganization() {
		assertThat(repository.getInvoiceDetails("5564786647", 139345995)).isEmpty();
	}

	@Test
	void getInvoiceDetails() {
		final var details = repository.getInvoiceDetails("5565027223", 139345995);

		assertThat(details)
			.hasSize(3)
			.extracting(
				InvoiceDetail::getAmount,
				InvoiceDetail::getAmountVatExcluded,
				InvoiceDetail::getInvoiceNumber,
				InvoiceDetail::getPeriodFrom,
				InvoiceDetail::getPeriodTo,
				InvoiceDetail::getProductCode,
				InvoiceDetail::getProductName,
				InvoiceDetail::getQuantity,
				InvoiceDetail::getUnit,
				InvoiceDetail::getUnitPrice,
				InvoiceDetail::getVat,
				InvoiceDetail::getVatRate,
				InvoiceDetail::getOrganizationNumber)
			.containsExactlyInAnyOrder(
				tuple(toBigDecimal(701.52), toBigDecimal(561.21), 139345995L, "2019-09-01", "2019-09-30", 1403, "Fast Elnätsavgift", 30.0, "Dagar", toBigDecimal(18.7068), toBigDecimal(140.31), 25.0, "5565027223"),
				tuple(toBigDecimal(147.20), toBigDecimal(117.76), 139345995L, "2019-09-01", "2019-09-30", 1404, "Elöverföring", 1070.57, "kWh", toBigDecimal(0.11), toBigDecimal(29.44), 25.0, "5565027223"),
				tuple(toBigDecimal(464.36), toBigDecimal(371.49), 139345995L, "2019-09-01", "2019-09-30", 1413, "Energiskatt", 1070.57, "kWh", toBigDecimal(0.347), toBigDecimal(92.87), 25.0, "5565027223"));
	}

	/**
	 * The columns fnInvoiceDetails added on top of the columns the replaced view carried. The values are the ones
	 * testdata.sql derives from Unitprice, one factor per column, so a column mapped to the wrong field shows up here.
	 */
	@Test
	void getInvoiceDetailsCarriesTheInvoiceUnitPriceColumns() {
		final var details = repository.getInvoiceDetails("5565027223", 139345995);

		assertThat(details)
			.extracting(
				InvoiceDetail::getUnitPrice,
				InvoiceDetail::getUnitPriceVatExcluded,
				InvoiceDetail::getInvoiceUnitPrice,
				InvoiceDetail::getInvoiceUnitPriceVatExcluded,
				InvoiceDetail::getInvoiceUnitPriceCurrency,
				InvoiceDetail::getInvoiceUnitPriceUnit)
			.containsExactlyInAnyOrder(
				tuple(toBigDecimal(18.7068), toBigDecimal(14.9654), toBigDecimal(1870.68), toBigDecimal(1496.544), "öre", "Dagar"),
				tuple(toBigDecimal(0.11), toBigDecimal(0.088), toBigDecimal(11.0), toBigDecimal(8.8), "öre", "kWh"),
				tuple(toBigDecimal(0.347), toBigDecimal(0.2776), toBigDecimal(34.7), toBigDecimal(27.76), "öre", "kWh"));
	}

	/**
	 * The rows come back in the order the function sorts them, which the repository asks for with ORDER BY RowSortOrder.
	 */
	@Test
	void getInvoiceDetailsIsSortedByRowSortOrder() {
		final var details = repository.getInvoiceDetails("5564786647", 766763197);

		assertThat(details)
			.extracting(InvoiceDetail::getProductName)
			.containsExactly("El, påslag", "Elpris", "Elcertifikat", "Månadsavgift");
	}

	@Test
	void getInvoiceDetailsForSeveralInvoicesNoMatch() {
		assertThat(repository.getInvoiceDetails(List.of(999999999L))).isEmpty();
	}

	@Test
	void getInvoiceDetailsForSeveralInvoicesWithoutInvoiceNumbers() {
		assertThat(repository.getInvoiceDetails(List.of())).isEmpty();
	}

	@Test
	void getInvoiceDetailsForSeveralInvoices() {
		final var details = repository.getInvoiceDetails(List.of(139345995L, 766763197L));

		assertThat(details)
			.hasSize(7)
			.extracting(
				InvoiceDetail::getOrganizationNumber,
				InvoiceDetail::getInvoiceNumber)
			.containsExactlyInAnyOrder(
				tuple("5565027223", 139345995L),
				tuple("5565027223", 139345995L),
				tuple("5565027223", 139345995L),
				tuple("5564786647", 766763197L),
				tuple("5564786647", 766763197L),
				tuple("5564786647", 766763197L),
				tuple("5564786647", 766763197L));
	}

	private static BigDecimal toBigDecimal(final double number) {
		return BigDecimal.valueOf(number).setScale(4);
	}
}
