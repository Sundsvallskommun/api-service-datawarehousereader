package se.sundsvall.datawarehousereader.api.model.customer;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

class CustomerDetailsParametersTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), OffsetDateTime.class);
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(CustomerDetailsParameters.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var partyId = List.of("partyId1", "partyId2");
		final var customerEngagementOrgId = "someCustomerEngagementOrgId";
		final var fromDateTime = OffsetDateTime.now();
		final var toDateTime = OffsetDateTime.now().plusDays(1);

		final var parameters = CustomerDetailsParameters.create()
			.withPartyId(partyId)
			.withCustomerEngagementOrgId(customerEngagementOrgId)
			.withToDateTime(toDateTime)
			.withFromDateTime(fromDateTime);

		assertThat(parameters).isNotNull().hasNoNullFieldsOrPropertiesExcept("sortBy");
		assertThat(parameters.getPartyId()).isEqualTo(partyId);
		assertThat(parameters.getCustomerEngagementOrgId()).isEqualTo(customerEngagementOrgId);
		assertThat(parameters.getToDateTime()).isEqualTo(toDateTime);
		assertThat(parameters.getFromDateTime()).isEqualTo(fromDateTime);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CustomerDetailsParameters.create())
			.hasAllNullFieldsOrPropertiesExcept("page", "limit", "sortBy", "sortDirection")
			.hasFieldOrPropertyWithValue("page", 1)
			.hasFieldOrPropertyWithValue("limit", 100)
			.hasFieldOrPropertyWithValue("sortDirection", Sort.DEFAULT_DIRECTION);

		assertThat(new CustomerDetailsParameters())
			.hasAllNullFieldsOrPropertiesExcept("page", "limit", "sortBy", "sortDirection")
			.hasFieldOrPropertyWithValue("page", 1)
			.hasFieldOrPropertyWithValue("limit", 100)
			.hasFieldOrPropertyWithValue("sortDirection", Sort.DEFAULT_DIRECTION);
	}
}