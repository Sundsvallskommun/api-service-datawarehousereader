package se.sundsvall.datawarehousereader.api.model.invoice;

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
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class CustomerInvoiceParametersTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> LocalDate.parse("2024-01-01").plusDays(new Random().nextInt()), LocalDate.class);
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
		final var customerNumbers = List.of("123456", "600606");
		final var organizationIds = List.of("5565027223", "5564786647");
		final var facilityIds = List.of("123456789012345670", "123456789012345671");
		final var status = "Betalad";
		final var periodFrom = LocalDate.parse("2024-01-01").minusMonths(3);
		final var periodTo = LocalDate.parse("2024-01-01");
		final var sortBy = List.of("periodFrom", "InvoiceDate");
		final var sortDirection = Sort.Direction.DESC;
		final var page = 1;
		final var limit = 5;

		final var parameters = CustomerInvoiceParameters.create()
			.withCustomerNumbers(customerNumbers)
			.withOrganizationIds(organizationIds)
			.withFacilityIds(facilityIds)
			.withStatus(status)
			.withPeriodFrom(periodFrom)
			.withPeriodTo(periodTo)
			.withSortBy(sortBy)
			.withSortDirection(sortDirection)
			.withPage(page)
			.withLimit(limit);

		assertThat(parameters).isNotNull();
		assertThat(parameters.getCustomerNumbers()).isEqualTo(customerNumbers);
		assertThat(parameters.getOrganizationIds()).isEqualTo(organizationIds);
		assertThat(parameters.getFacilityIds()).isEqualTo(facilityIds);
		assertThat(parameters.getStatus()).isEqualTo(status);
		assertThat(parameters.getPeriodFrom()).isEqualTo(periodFrom);
		assertThat(parameters.getPeriodTo()).isEqualTo(periodTo);
		assertThat(parameters.getSortBy()).isEqualTo(sortBy);
		assertThat(parameters.getSortDirection()).isEqualTo(sortDirection);
		assertThat(parameters.getPage()).isEqualTo(page);
		assertThat(parameters.getLimit()).isEqualTo(limit);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CustomerInvoiceParameters.create())
			.hasAllNullFieldsOrPropertiesExcept("page", "limit", "sortDirection")
			.hasFieldOrPropertyWithValue("page", 1)
			.hasFieldOrPropertyWithValue("limit", 100)
			.hasFieldOrPropertyWithValue("sortDirection", Sort.Direction.ASC);

		assertThat(new CustomerInvoiceParameters())
			.hasAllNullFieldsOrPropertiesExcept("page", "limit", "sortDirection")
			.hasFieldOrPropertyWithValue("page", 1)
			.hasFieldOrPropertyWithValue("limit", 100)
			.hasFieldOrPropertyWithValue("sortDirection", Sort.Direction.ASC);
	}
}
