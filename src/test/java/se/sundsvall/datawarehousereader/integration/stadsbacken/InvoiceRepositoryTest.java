package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static java.util.Optional.ofNullable;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceParameters;

/**
 * invoice repository tests.
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
class InvoiceRepositoryTest {

	@Autowired
	private InvoiceRepository repository;

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

	private static BigDecimal toBigDecimal(double number) {
		return BigDecimal.valueOf(number).setScale(4);
	}
}
