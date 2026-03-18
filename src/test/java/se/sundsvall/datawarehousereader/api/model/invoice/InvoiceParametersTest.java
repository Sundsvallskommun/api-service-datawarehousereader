package se.sundsvall.datawarehousereader.api.model.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import se.sundsvall.datawarehousereader.api.model.CustomerType;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToStringExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class InvoiceParametersTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(InvoiceParameters.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSettersExcluding("organizationNumber"),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToStringExcluding("limit", "page", "sortBy", "sortDirection")));
	}

	@Test
	void builderPattern() {
		final var customerNumbers = List.of("39195", "39196");
		final var customerType = CustomerType.PRIVATE;
		final var facilityIds = List.of("735999109151401011", "735999109151401012");
		final var invoiceNumber = 767915994L;
		final var invoiceDateFrom = now().minusDays(10);
		final var invoiceDateTo = now();
		final var invoiceName = "765801493.pdf";
		final var invoiceType = "Faktura";
		final var invoiceStatus = "Skickad";
		final var ocrNumber = 767915994L;
		final var dueDateFrom = now().minusDays(10);
		final var dueDateTo = now();
		final var organizationGroup = "ORGGRP1";
		final var organizationNumbers = List.of("5561234567");
		final var administration = "ADMIN1";

		final InvoiceParameters invoiceParameters = InvoiceParameters.create()
			.withCustomerNumber(customerNumbers)
			.withCustomerType(customerType)
			.withFacilityIds(facilityIds)
			.withInvoiceNumber(invoiceNumber)
			.withInvoiceDateFrom(invoiceDateFrom)
			.withInvoiceDateTo(invoiceDateTo)
			.withInvoiceName(invoiceName)
			.withInvoiceType(invoiceType)
			.withInvoiceStatus(invoiceStatus)
			.withOcrNumber(ocrNumber)
			.withDueDateFrom(dueDateFrom)
			.withDueDateTo(dueDateTo)
			.withOrganizationGroup(organizationGroup)
			.withOrganizationNumbers(organizationNumbers)
			.withAdministration(administration);

		assertThat(invoiceParameters.getCustomerNumber()).isEqualTo(customerNumbers);
		assertThat(invoiceParameters.getCustomerType()).isEqualTo(customerType);
		assertThat(invoiceParameters.getFacilityIds()).isEqualTo(facilityIds);
		assertThat(invoiceParameters.getInvoiceNumber()).isEqualTo(invoiceNumber);
		assertThat(invoiceParameters.getInvoiceDateFrom()).isEqualTo(invoiceDateFrom);
		assertThat(invoiceParameters.getInvoiceDateTo()).isEqualTo(invoiceDateTo);
		assertThat(invoiceParameters.getInvoiceName()).isEqualTo(invoiceName);
		assertThat(invoiceParameters.getInvoiceType()).isEqualTo(invoiceType);
		assertThat(invoiceParameters.getInvoiceStatus()).isEqualTo(invoiceStatus);
		assertThat(invoiceParameters.getOcrNumber()).isEqualTo(ocrNumber);
		assertThat(invoiceParameters.getDueDateFrom()).isEqualTo(dueDateFrom);
		assertThat(invoiceParameters.getDueDateTo()).isEqualTo(dueDateTo);
		assertThat(invoiceParameters.getOrganizationGroup()).isEqualTo(organizationGroup);
		assertThat(invoiceParameters.getOrganizationNumbers()).isEqualTo(organizationNumbers);
		assertThat(invoiceParameters.getAdministration()).isEqualTo(administration);

	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(InvoiceParameters.create())
			.hasAllNullFieldsOrPropertiesExcept("page", "limit", "sortBy", "sortDirection")
			.hasFieldOrPropertyWithValue("page", 1)
			.hasFieldOrPropertyWithValue("limit", 100)
			.hasFieldOrPropertyWithValue("sortDirection", Sort.DEFAULT_DIRECTION);

		assertThat(new InvoiceParameters())
			.hasAllNullFieldsOrPropertiesExcept("page", "limit", "sortBy", "sortDirection")
			.hasFieldOrPropertyWithValue("page", 1)
			.hasFieldOrPropertyWithValue("limit", 100)
			.hasFieldOrPropertyWithValue("sortDirection", Sort.DEFAULT_DIRECTION);
	}
}
