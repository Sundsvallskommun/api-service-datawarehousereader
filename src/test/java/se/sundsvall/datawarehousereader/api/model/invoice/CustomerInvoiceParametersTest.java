package se.sundsvall.datawarehousereader.api.model.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class CustomerInvoiceParametersTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(CustomerInvoiceParameters.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var customerNumbers = List.of("216870", "600606");
		final var organizationIds = List.of("5565027223", "5564786647");
		final var facilityIds = List.of("735999109425048010", "735999109425048011");
		final var status = "Betalad";
		final var periodFrom = now().minusMonths(3);
		final var periodTo = now();
		final var sortBy = "periodFrom";

		final var parameters = CustomerInvoiceParameters.create()
			.withCustomerNumbers(customerNumbers)
			.withOrganizationIds(organizationIds)
			.withFacilityIds(facilityIds)
			.withStatus(status)
			.withPeriodFrom(periodFrom)
			.withPeriodTo(periodTo)
			.withSortBy(sortBy);

		assertThat(parameters).isNotNull();
		assertThat(parameters.getCustomerNumbers()).isEqualTo(customerNumbers);
		assertThat(parameters.getOrganizationIds()).isEqualTo(organizationIds);
		assertThat(parameters.getFacilityIds()).isEqualTo(facilityIds);
		assertThat(parameters.getStatus()).isEqualTo(status);
		assertThat(parameters.getPeriodFrom()).isEqualTo(periodFrom);
		assertThat(parameters.getPeriodTo()).isEqualTo(periodTo);
		assertThat(parameters.getSortBy()).isEqualTo(sortBy);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CustomerInvoiceParameters.create())
			.hasAllNullFieldsOrPropertiesExcept("page", "limit")
			.hasFieldOrPropertyWithValue("page", 1)
			.hasFieldOrPropertyWithValue("limit", 100);

		assertThat(new CustomerInvoiceParameters())
			.hasAllNullFieldsOrPropertiesExcept("page", "limit")
			.hasFieldOrPropertyWithValue("page", 1)
			.hasFieldOrPropertyWithValue("limit", 100);
	}
}
