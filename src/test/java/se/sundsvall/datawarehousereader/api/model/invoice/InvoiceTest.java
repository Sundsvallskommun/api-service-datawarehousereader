package se.sundsvall.datawarehousereader.api.model.invoice;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.sundsvall.datawarehousereader.api.model.CustomerType;

class InvoiceTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(Invoice.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {

		final var administration = "administration";
		final var amountVatExcluded = valueOf(101);
		final var amountVatIncluded = valueOf(202);
		final var careOf = "careOf";
		final var city = "city";
		final var currency = "currency";
		final var customerNumber = "1234";
		final var customerType = CustomerType.PRIVATE;
		final var dueDate = now();
		final var facilityId = "facilityId";
		final var invoiceDate = now().minusDays(15);
		final var invoiceDescription = "invoiceDescription";
		final var invoiceName = "invoiceName";
		final var invoiceNumber = 4321L;
		final var invoiceStatus = "invoiceStatus";
		final var invoiceType = "invoiceType";
		final var ocrNumber = 1337L;
		final var organizationGroup = "organizationGroup";
		final var organizationNumber = "organizationNumber";
		final var postalCode = "postalCode";
		final var reversedVat = true;
		final var rounding = valueOf(303);
		final var street = "street";
		final var totalAmount = valueOf(404);
		final var vat = valueOf(505);
		final var vatEligibleAmount = valueOf(606);
		final var pdfAvailable = false;

		final var invoice = Invoice.create()
			.withAdministration(administration)
			.withAmountVatExcluded(amountVatExcluded)
			.withAmountVatIncluded(amountVatIncluded)
			.withCareOf(careOf)
			.withCity(city)
			.withCurrency(currency)
			.withCustomerNumber(customerNumber)
			.withCustomerType(customerType)
			.withDueDate(dueDate)
			.withFacilityId(facilityId)
			.withInvoiceDate(invoiceDate)
			.withInvoiceDescription(invoiceDescription)
			.withInvoiceName(invoiceName)
			.withInvoiceNumber(invoiceNumber)
			.withInvoiceStatus(invoiceStatus)
			.withInvoiceType(invoiceType)
			.withOcrNumber(ocrNumber)
			.withOrganizationGroup(organizationGroup)
			.withOrganizationNumber(organizationNumber)
			.withPostCode(postalCode)
			.withReversedVat(reversedVat)
			.withRounding(rounding)
			.withStreet(street)
			.withTotalAmount(totalAmount)
			.withVat(vat)
			.withVatEligibleAmount(vatEligibleAmount)
			.withPdfAvailable(pdfAvailable);

		assertThat(invoice).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(invoice)
			.extracting(
				Invoice::getAdministration,
				Invoice::getAmountVatExcluded,
				Invoice::getAmountVatIncluded,
				Invoice::getCareOf,
				Invoice::getCity,
				Invoice::getCurrency,
				Invoice::getCustomerNumber,
				Invoice::getCustomerType,
				Invoice::getDueDate,
				Invoice::getFacilityId,
				Invoice::getInvoiceDate,
				Invoice::getInvoiceDescription,
				Invoice::getInvoiceName,
				Invoice::getInvoiceNumber,
				Invoice::getInvoiceStatus,
				Invoice::getInvoiceType,
				Invoice::getOcrNumber,
				Invoice::getOrganizationGroup,
				Invoice::getOrganizationNumber,
				Invoice::getPostCode,
				Invoice::getReversedVat,
				Invoice::getRounding,
				Invoice::getStreet,
				Invoice::getTotalAmount,
				Invoice::getVat,
				Invoice::getVatEligibleAmount,
				Invoice::getPdfAvailable)
			.containsExactly(
				administration,
				amountVatExcluded,
				amountVatIncluded,
				careOf,
				city,
				currency,
				customerNumber,
				customerType,
				dueDate,
				facilityId,
				invoiceDate,
				invoiceDescription,
				invoiceName,
				invoiceNumber,
				invoiceStatus,
				invoiceType,
				ocrNumber,
				organizationGroup,
				organizationNumber,
				postalCode,
				reversedVat,
				rounding,
				street,
				totalAmount,
				vat,
				vatEligibleAmount,
				pdfAvailable);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Invoice.create()).hasAllNullFieldsOrProperties();
		assertThat(new Invoice()).hasAllNullFieldsOrProperties();
	}
}
