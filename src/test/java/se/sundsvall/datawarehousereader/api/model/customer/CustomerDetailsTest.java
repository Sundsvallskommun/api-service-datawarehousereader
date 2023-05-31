package se.sundsvall.datawarehousereader.api.model.customer;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import se.sundsvall.datawarehousereader.api.model.CustomerType;

class CustomerDetailsTest {

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
		final var partyId = "partyId";
		final var customerNumber = "customerNumber";
		final var customerName = "customerName";
		final var customerType = CustomerType.PRIVATE;
		final var street = "street";
		final var postalCode = "postalCode";
		final var city = "city";
		final var careOf = "careOf";
		final var phoneNumbers = List.of("phoneNumber1", "phoneNumer2");
		final var emails = List.of("emailAddress1", "emailAddress2");

		final var customer = CustomerDetails.create()
			.withCustomerOrgNumber(customerOrgNumber)
			.withPartyId(partyId)
			.withCustomerNumber(customerNumber)
			.withCustomerName(customerName)
			.withCustomerType(customerType)
			.withStreet(street)
			.withPostalCode(postalCode)
			.withCity(city)
			.withCareOf(careOf)
			.withPhoneNumbers(phoneNumbers)
			.withEmailAddresses(emails);

		Assertions.assertThat(customer.getCustomerOrgNumber()).isEqualTo(customerOrgNumber);
		Assertions.assertThat(customer.getPartyId()).isEqualTo(partyId);
		Assertions.assertThat(customer.getCustomerNumber()).isEqualTo(customerNumber);
		Assertions.assertThat(customer.getCustomerName()).isEqualTo(customerName);
		Assertions.assertThat(customer.getCustomerType()).isEqualTo(customerType);
		Assertions.assertThat(customer.getStreet()).isEqualTo(street);
		Assertions.assertThat(customer.getPostalCode()).isEqualTo(postalCode);
		Assertions.assertThat(customer.getCity()).isEqualTo(city);
		Assertions.assertThat(customer.getCareOf()).isEqualTo(careOf);
		Assertions.assertThat(customer.getPhoneNumbers()).isEqualTo(phoneNumbers);
		Assertions.assertThat(customer.getEmails()).isEqualTo(emails);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		Assertions.assertThat(CustomerDetails.create()).hasAllNullFieldsOrPropertiesExcept("customerCategoryID", "customerChangedFlg", "installedChangedFlg")
			.hasFieldOrPropertyWithValue("customerCategoryID", 0)
			.hasFieldOrPropertyWithValue("customerChangedFlg", false)
			.hasFieldOrPropertyWithValue("installedChangedFlg", false);
		Assertions.assertThat(new CustomerDetails()).hasAllNullFieldsOrPropertiesExcept("customerCategoryID", "customerChangedFlg", "installedChangedFlg")
			.hasFieldOrPropertyWithValue("customerCategoryID", 0)
			.hasFieldOrPropertyWithValue("customerChangedFlg", false)
			.hasFieldOrPropertyWithValue("installedChangedFlg", false);
	}

}