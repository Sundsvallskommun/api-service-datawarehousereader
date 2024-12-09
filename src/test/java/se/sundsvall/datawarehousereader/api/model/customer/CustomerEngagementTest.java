package se.sundsvall.datawarehousereader.api.model.customer;

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

import java.time.LocalDate;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.sundsvall.datawarehousereader.api.model.CustomerType;

class CustomerEngagementTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(CustomerEngagement.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var customerNumber = "101";
		final var partyId = "partyId";
		final var customerType = CustomerType.PRIVATE;
		final var customerOrgNumber = "customerOrgNumber";
		final var organizationNumber = "organizationNumber";
		final var organizationName = "organizationName";
		final var active = true;
		final var moveInDate = LocalDate.now();

		final var customer = CustomerEngagement.create()
			.withCustomerNumber(customerNumber)
			.withPartyId(partyId)
			.withCustomerOrgNumber(customerOrgNumber)
			.withCustomerType(customerType)
			.withOrganizationNumber(organizationNumber)
			.withOrganizationName(organizationName)
			.withActive(active)
			.withMoveInDate(moveInDate);

		assertThat(customer).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(customer.getCustomerNumber()).isEqualTo(customerNumber);
		assertThat(customer.getPartyId()).isEqualTo(partyId);
		assertThat(customer.getCustomerType()).isEqualTo(customerType);
		assertThat(customer.getCustomerOrgNumber()).isEqualTo(customerOrgNumber);
		assertThat(customer.getOrganizationNumber()).isEqualTo(organizationNumber);
		assertThat(customer.getOrganizationName()).isEqualTo(organizationName);
		assertThat(customer.isActive()).isEqualTo(active);
		assertThat(customer.getMoveInDate()).isEqualTo(moveInDate);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CustomerEngagement.create()).hasAllNullFieldsOrPropertiesExcept("active")
			.hasFieldOrPropertyWithValue("active", false);

		assertThat(new CustomerEngagement()).hasAllNullFieldsOrPropertiesExcept("active")
			.hasFieldOrPropertyWithValue("active", false);

	}
}
