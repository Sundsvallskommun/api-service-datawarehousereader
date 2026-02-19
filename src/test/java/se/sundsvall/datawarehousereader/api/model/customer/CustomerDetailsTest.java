package se.sundsvall.datawarehousereader.api.model.customer;

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

class CustomerDetailsTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(CustomerDetails.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {

		final var customerOrgNumber = "customerOrgNumber";
		final var customerEngagementOrgName = "customerEngagementOrgName";
		final var customerEngagementOrgId = "customerEngagementOrgId";
		final var partyId = "partyId";
		final var customerNumber = "customerNumber";
		final var customerName = "customerName";
		final var customerCategoryDescription = "customerCategoryDescription";
		final var street = "street";
		final var postalCode = "postalCode";
		final var city = "city";
		final var careOf = "careOf";
		final var phoneNumbers = List.of("phoneNumber1", "phoneNumer2");
		final var emails = List.of("emailAddress1", "emailAddress2");
		final var active = true;
		final var moveInDate = LocalDate.now();

		final var customer = CustomerDetails.create()
			.withCustomerEngagementOrgName(customerEngagementOrgName)
			.withCustomerEngagementOrgId(customerEngagementOrgId)
			.withCustomerOrgNumber(customerOrgNumber)
			.withPartyId(partyId)
			.withCustomerCategoryDescription(customerCategoryDescription)
			.withCustomerNumber(customerNumber)
			.withCustomerName(customerName)
			.withStreet(street)
			.withPostalCode(postalCode)
			.withCity(city)
			.withCareOf(careOf)
			.withPhoneNumbers(phoneNumbers)
			.withEmailAddresses(emails)
			.withActive(active)
			.withMoveInDate(moveInDate);

		assertThat(customer).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(customer.getCustomerOrgNumber()).isEqualTo(customerOrgNumber);
		assertThat(customer.getCustomerEngagementOrgName()).isEqualTo(customerEngagementOrgName);
		assertThat(customer.getCustomerEngagementOrgId()).isEqualTo(customerEngagementOrgId);
		assertThat(customer.getPartyId()).isEqualTo(partyId);
		assertThat(customer.getCustomerNumber()).isEqualTo(customerNumber);
		assertThat(customer.getCustomerName()).isEqualTo(customerName);
		assertThat(customer.getStreet()).isEqualTo(street);
		assertThat(customer.getPostalCode()).isEqualTo(postalCode);
		assertThat(customer.getCity()).isEqualTo(city);
		assertThat(customer.getCareOf()).isEqualTo(careOf);
		assertThat(customer.getPhoneNumbers()).isEqualTo(phoneNumbers);
		assertThat(customer.getEmails()).isEqualTo(emails);
		assertThat(customer.isActive()).isEqualTo(active);
		assertThat(customer.getMoveInDate()).isEqualTo(moveInDate);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CustomerDetails.create()).hasAllNullFieldsOrPropertiesExcept("customerCategoryID", "customerChangedFlg", "installedChangedFlg", "active")
			.hasFieldOrPropertyWithValue("customerCategoryID", 0)
			.hasFieldOrPropertyWithValue("customerChangedFlg", false)
			.hasFieldOrPropertyWithValue("installedChangedFlg", false)
			.hasFieldOrPropertyWithValue("active", false);
		assertThat(new CustomerDetails()).hasAllNullFieldsOrPropertiesExcept("customerCategoryID", "customerChangedFlg", "installedChangedFlg", "active")
			.hasFieldOrPropertyWithValue("customerCategoryID", 0)
			.hasFieldOrPropertyWithValue("customerChangedFlg", false)
			.hasFieldOrPropertyWithValue("installedChangedFlg", false)
			.hasFieldOrPropertyWithValue("active", false);
	}
}
