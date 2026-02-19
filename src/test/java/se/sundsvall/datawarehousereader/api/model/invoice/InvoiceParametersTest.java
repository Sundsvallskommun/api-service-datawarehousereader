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
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
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
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToStringExcluding("limit", "page", "sortBy", "sortDirection")));
	}

	@Test
	void builderPattern() {
		var customerNumbers = List.of("39195", "39196");
		var customerType = CustomerType.PRIVATE;
		var facilityIds = List.of("735999109151401011", "735999109151401012");
		var invoiceNumber = 767915994L;
		var invoiceDateFrom = now().minusDays(10);
		var invoiceDateTo = now();
		var invoiceName = "765801493.pdf";
		var invoiceType = "Faktura";
		var invoiceStatus = "Skickad";
		var ocrNumber = 767915994L;
		var dueDateFrom = now().minusDays(10);
		var dueDateTo = now();
		var organizationGroup = "ORGGRP1";
		var organizationNumber = "5561234567";
		var administration = "ADMIN1";

		InvoiceParameters invoiceParameters = InvoiceParameters.create()
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
			.withOrganizationNumber(organizationNumber)
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
		assertThat(invoiceParameters.getOrganizationNumber()).isEqualTo(organizationNumber);
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
