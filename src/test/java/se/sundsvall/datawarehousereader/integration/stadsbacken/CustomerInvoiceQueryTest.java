package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

class CustomerInvoiceQueryTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> LocalDate.parse("2024-01-01").plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(CustomerInvoiceQuery.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var page = 2;
		final var limit = 10;
		final var customerIds = "123456,600606";
		final var organizationIds = "5565027223,5564786647";
		final var facilityIds = "123456789012345670,123456789012345671";
		final var status = "Betalad";
		final var periodFrom = LocalDate.parse("2025-01-01");
		final var periodTo = LocalDate.parse("2025-12-31");
		final var sortBy = List.of("periodFrom", "InvoiceDate");
		final var sortDirection = Sort.Direction.DESC;

		final var query = CustomerInvoiceQuery.create()
			.withPage(page)
			.withLimit(limit)
			.withCustomerIds(customerIds)
			.withOrganizationIds(organizationIds)
			.withFacilityIds(facilityIds)
			.withStatus(status)
			.withPeriodFrom(periodFrom)
			.withPeriodTo(periodTo)
			.withSortBy(sortBy)
			.withSortDirection(sortDirection);

		assertThat(query).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(query.getPage()).isEqualTo(page);
		assertThat(query.getLimit()).isEqualTo(limit);
		assertThat(query.getCustomerIds()).isEqualTo(customerIds);
		assertThat(query.getOrganizationIds()).isEqualTo(organizationIds);
		assertThat(query.getFacilityIds()).isEqualTo(facilityIds);
		assertThat(query.getStatus()).isEqualTo(status);
		assertThat(query.getPeriodFrom()).isEqualTo(periodFrom);
		assertThat(query.getPeriodTo()).isEqualTo(periodTo);
		assertThat(query.getSortBy()).isEqualTo(sortBy);
		assertThat(query.getSortDirection()).isEqualTo(sortDirection);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CustomerInvoiceQuery.create()).hasAllNullFieldsOrProperties();
		assertThat(new CustomerInvoiceQuery()).hasAllNullFieldsOrProperties();
	}
}
