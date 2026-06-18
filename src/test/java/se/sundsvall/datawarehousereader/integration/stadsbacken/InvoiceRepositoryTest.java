package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceParameters;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace.NONE;

/**
 * invoice repository tests.
 * <p>
 * These tests exercise the {@code InvoiceSpecification} filtering through
 * {@link InvoiceRepository#findDistinctInvoiceNumbers},
 * which is the contract this repository owns. The raw row fetch (and entity column mapping) lives in
 * {@link InvoiceJdbcRepository} and is covered by its own test.
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
class InvoiceRepositoryTest {

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Test
	void getInvoiceNumbersNoParameters() {

		var result = invoiceRepository.findDistinctInvoiceNumbers(InvoiceParameters.create(), PageRequest.of(0, 100));

		assertThat(result).isNotNull();
		assertThat(result.getNumber()).isZero();
		assertThat(result.getNumberOfElements()).isEqualTo(100);
		assertThat(result.getTotalPages()).isEqualTo(2);
		assertThat(result.getTotalElements()).isEqualTo(179);
		assertThat(result.getContent()).hasSize(100);
	}

	@Test
	void getInvoicesByAdministration() {

		var result = invoiceRepository.findDistinctInvoiceNumbers(createParameters("Sundsvall Energi AB ", null, null, null, null, null, null), PageRequest.of(0, 100));

		assertThat(result.getContent()).containsExactlyInAnyOrder(
			138023890L, 138023999L, 138042593L, 139034094L, 139345193L, 139348890L, 766763197L, 767234891L, 767891997L);
	}

	@Test
	void getInvoiceByAdministrationAndDueDateBetween() {

		var result = invoiceRepository.findDistinctInvoiceNumbers(createParameters("Sundsvall Energi AB ", null, LocalDate.of(2019, Month.OCTOBER, 30), LocalDate.of(2019, Month.NOVEMBER, 7), null, null, null), PageRequest.of(0, 100));

		assertThat(result.getContent()).containsExactlyInAnyOrder(766763197L, 767234891L, 767891997L);
	}

	@Test
	void getInvoiceByAdministrationAndDueDateLargerOrEqualThan() {

		var result = invoiceRepository.findDistinctInvoiceNumbers(createParameters("Sundsvall Energi AB ", null, LocalDate.of(2019, Month.NOVEMBER, 8), null, null, null, null), PageRequest.of(0, 100));

		assertThat(result.getContent()).containsExactlyInAnyOrder(
			138023890L, 138023999L, 138042593L, 139034094L, 139345193L, 139348890L);
	}

	@Test
	void getInvoiceByCustomerId() {

		var result = invoiceRepository.findDistinctInvoiceNumbers(createParameters(null, List.of("600675"), null, null, null, null, null), PageRequest.of(0, 100));

		assertThat(result.getContent()).containsExactlyInAnyOrder(138042593L);
	}

	@Test
	void getInvoiceByMultipleCustomerIds() {

		var result = invoiceRepository.findDistinctInvoiceNumbers(createParameters(null, List.of("600675", "600606"), null, null, null, null, null), PageRequest.of(0, 100));

		assertThat(result.getContent()).containsExactlyInAnyOrder(
			138042593L, 137968194L, 137968293L, 137968392L, 765386099L, 767916190L);
	}

	@Test
	void getInvoiceByInvoiceNumber() {

		var result = invoiceRepository.findDistinctInvoiceNumbers(createParameters(null, null, null, null, 138023999L, null, null), PageRequest.of(0, 100));

		assertThat(result.getContent()).containsExactlyInAnyOrder(138023999L);
	}

	@Test
	void getInvoiceByOcrNumber() {

		var result = invoiceRepository.findDistinctInvoiceNumbers(createParameters(null, null, null, null, null, 138023999L, null), PageRequest.of(0, 100));

		assertThat(result.getContent()).containsExactlyInAnyOrder(138023999L);
	}

	@Test
	void getInvoiceByFacilityId() {

		var result = invoiceRepository.findDistinctInvoiceNumbers(createParameters(null, null, null, null, null, null, List.of("735999109324119255")), PageRequest.of(0, 100));

		assertThat(result.getContent()).containsExactlyInAnyOrder(138023999L);
	}

	@Test
	void getInvoiceByMultipleFacilityIds() {

		var result = invoiceRepository.findDistinctInvoiceNumbers(createParameters(null, null, null, null, null, null, List.of("735999109324119255", "735999109451436027")), PageRequest.of(0, 100));

		assertThat(result.getContent()).containsExactlyInAnyOrder(138023999L, 139349898L);
	}

	private static InvoiceParameters createParameters(String administration, List<String> customerNumber, LocalDate dueDateFrom, LocalDate dueDateTo, Long invoiceNumber, Long ocrNumber, List<String> facilityId) {
		InvoiceParameters parameters = InvoiceParameters.create();
		parameters.setAdministration(administration);
		ofNullable(customerNumber).ifPresent(p -> parameters.setCustomerNumber(customerNumber));
		parameters.setDueDateFrom(dueDateFrom);
		parameters.setDueDateTo(dueDateTo);
		parameters.setInvoiceNumber(invoiceNumber);
		parameters.setOcrNumber(ocrNumber);
		ofNullable(facilityId).ifPresent(p -> parameters.setFacilityIds(facilityId));

		return parameters;
	}
}
