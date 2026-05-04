package se.sundsvall.datawarehousereader.api.model.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.sundsvall.datawarehousereader.api.model.CustomerType;

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

class CustomerInvoiceTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(CustomerInvoice.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var customerNumber = "216870";
		final var customerType = CustomerType.PRIVATE;
		final var facilityId = "facilityId";
		final var invoiceNumber = 295334999L;
		final var invoiceId = 1062916396L;
		final var jointInvoiceId = -1L;
		final var invoiceDate = now().minusDays(20);
		final var invoiceName = "295334999.pdf";
		final var invoiceType = "Faktura";
		final var invoiceDescription = "El";
		final var invoiceStatus = "Betalad";
		final var ocrNumber = 295334999L;
		final var dueDate = now().plusDays(10);
		final var periodFrom = now().minusMonths(1);
		final var periodTo = now();
		final var totalAmount = valueOf(1234);
		final var amountVatIncluded = valueOf(1233);
		final var amountVatExcluded = valueOf(986);
		final var vatEligibleAmount = valueOf(986);
		final var rounding = valueOf(0);
		final var organizationGroup = "stadsbacken";
		final var organizationNumber = "5565027223";
		final var administration = "Sundsvall Elnät";
		final var street = "Fuxvägen 11";
		final var postCode = "85752";
		final var city = "Sundsvall";
		final var careOf = "Sjöqvist Nils";
		final var invoiceReference = "ref";
		final var pdfAvailable = true;
		final var details = List.of(InvoiceDetail.create());

		final var invoice = CustomerInvoice.create()
			.withCustomerNumber(customerNumber)
			.withCustomerType(customerType)
			.withFacilityId(facilityId)
			.withInvoiceNumber(invoiceNumber)
			.withInvoiceId(invoiceId)
			.withJointInvoiceId(jointInvoiceId)
			.withInvoiceDate(invoiceDate)
			.withInvoiceName(invoiceName)
			.withInvoiceType(invoiceType)
			.withInvoiceDescription(invoiceDescription)
			.withInvoiceStatus(invoiceStatus)
			.withOcrNumber(ocrNumber)
			.withDueDate(dueDate)
			.withPeriodFrom(periodFrom)
			.withPeriodTo(periodTo)
			.withTotalAmount(totalAmount)
			.withAmountVatIncluded(amountVatIncluded)
			.withAmountVatExcluded(amountVatExcluded)
			.withVatEligibleAmount(vatEligibleAmount)
			.withRounding(rounding)
			.withOrganizationGroup(organizationGroup)
			.withOrganizationNumber(organizationNumber)
			.withAdministration(administration)
			.withStreet(street)
			.withPostCode(postCode)
			.withCity(city)
			.withCareOf(careOf)
			.withInvoiceReference(invoiceReference)
			.withPdfAvailable(pdfAvailable)
			.withDetails(details);

		assertThat(invoice).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(invoice.getCustomerNumber()).isEqualTo(customerNumber);
		assertThat(invoice.getCustomerType()).isEqualTo(customerType);
		assertThat(invoice.getFacilityId()).isEqualTo(facilityId);
		assertThat(invoice.getInvoiceNumber()).isEqualTo(invoiceNumber);
		assertThat(invoice.getInvoiceId()).isEqualTo(invoiceId);
		assertThat(invoice.getJointInvoiceId()).isEqualTo(jointInvoiceId);
		assertThat(invoice.getInvoiceDate()).isEqualTo(invoiceDate);
		assertThat(invoice.getInvoiceName()).isEqualTo(invoiceName);
		assertThat(invoice.getInvoiceType()).isEqualTo(invoiceType);
		assertThat(invoice.getInvoiceDescription()).isEqualTo(invoiceDescription);
		assertThat(invoice.getInvoiceStatus()).isEqualTo(invoiceStatus);
		assertThat(invoice.getOcrNumber()).isEqualTo(ocrNumber);
		assertThat(invoice.getDueDate()).isEqualTo(dueDate);
		assertThat(invoice.getPeriodFrom()).isEqualTo(periodFrom);
		assertThat(invoice.getPeriodTo()).isEqualTo(periodTo);
		assertThat(invoice.getTotalAmount()).isEqualTo(totalAmount);
		assertThat(invoice.getAmountVatIncluded()).isEqualTo(amountVatIncluded);
		assertThat(invoice.getAmountVatExcluded()).isEqualTo(amountVatExcluded);
		assertThat(invoice.getVatEligibleAmount()).isEqualTo(vatEligibleAmount);
		assertThat(invoice.getRounding()).isEqualTo(rounding);
		assertThat(invoice.getOrganizationGroup()).isEqualTo(organizationGroup);
		assertThat(invoice.getOrganizationNumber()).isEqualTo(organizationNumber);
		assertThat(invoice.getAdministration()).isEqualTo(administration);
		assertThat(invoice.getStreet()).isEqualTo(street);
		assertThat(invoice.getPostCode()).isEqualTo(postCode);
		assertThat(invoice.getCity()).isEqualTo(city);
		assertThat(invoice.getCareOf()).isEqualTo(careOf);
		assertThat(invoice.getInvoiceReference()).isEqualTo(invoiceReference);
		assertThat(invoice.getPdfAvailable()).isEqualTo(pdfAvailable);
		assertThat(invoice.getDetails()).isEqualTo(details);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CustomerInvoice.create()).hasAllNullFieldsOrProperties();
		assertThat(new CustomerInvoice()).hasAllNullFieldsOrProperties();
	}
}
