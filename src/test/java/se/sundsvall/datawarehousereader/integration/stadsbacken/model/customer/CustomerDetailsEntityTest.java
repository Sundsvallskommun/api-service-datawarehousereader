package se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerDetailsEntityTest {

	@Test
	void testBean() {
		assertThat(CustomerDetailsEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var customerId = 123;
		final var customerOrgId = "customerOrgId";
		final var customerCategoryID = 2;
		final var customerCategoryDescription = "customerCategoryDescription";
		final var name = "name";
		final var co = "co";
		final var address = "address";
		final var zipcode = "zipcode";
		final var city = "city";
		final var phone1 = "phone1";
		final var phone2 = "phone2";
		final var phone3 = "phone3";
		final var email1 = "email1";
		final var email2 = "email2";
		final var customerChangedFlg = true;
		final var installedChangedFlg = true;

		final var entity = CustomerDetailsEntity.create()
			.withCustomerOrgId(customerOrgId)
			.withCustomerId(customerId)
			.withCustomerCategoryID(customerCategoryID)
			.withCustomerCategoryDescription(customerCategoryDescription)
			.withName(name)
			.withCo(co)
			.withAddress(address)
			.withZipcode(zipcode)
			.withCity(city)
			.withPhone1(phone1)
			.withPhone2(phone2)
			.withPhone3(phone3)
			.withEmail1(email1)
			.withEmail2(email2)
			.withCustomerChangedFlg(customerChangedFlg)
			.withInstalledChangedFlg(installedChangedFlg);

		Assertions.assertThat(entity.getCustomerId()).isEqualTo(customerId);
		Assertions.assertThat(entity.getCustomerOrgId()).isEqualTo(customerOrgId);
		Assertions.assertThat(entity.getCustomerCategoryID()).isEqualTo(customerCategoryID);
		Assertions.assertThat(entity.getCustomerCategoryDescription()).isEqualTo(customerCategoryDescription);
		Assertions.assertThat(entity.getName()).isEqualTo(name);
		Assertions.assertThat(entity.getCo()).isEqualTo(co);
		Assertions.assertThat(entity.getAddress()).isEqualTo(address);
		Assertions.assertThat(entity.getZipcode()).isEqualTo(zipcode);
		Assertions.assertThat(entity.getCity()).isEqualTo(city);
		Assertions.assertThat(entity.getPhone1()).isEqualTo(phone1);
		Assertions.assertThat(entity.getPhone2()).isEqualTo(phone2);
		Assertions.assertThat(entity.getPhone3()).isEqualTo(phone3);
		Assertions.assertThat(entity.getEmail1()).isEqualTo(email1);
		Assertions.assertThat(entity.getEmail2()).isEqualTo(email2);
		Assertions.assertThat(entity.isCustomerChangedFlg()).isEqualTo(customerChangedFlg);
		Assertions.assertThat(entity.isInstalledChangedFlg()).isEqualTo(installedChangedFlg);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		Assertions.assertThat(CustomerDetailsEntity.create()).hasAllNullFieldsOrPropertiesExcept("customerChangedFlg", "installedChangedFlg")
			.hasFieldOrPropertyWithValue("customerChangedFlg", false)
			.hasFieldOrPropertyWithValue("installedChangedFlg", false);
		Assertions.assertThat(new CustomerDetailsEntity()).hasAllNullFieldsOrPropertiesExcept( "customerChangedFlg", "installedChangedFlg")
			.hasFieldOrPropertyWithValue("customerChangedFlg", false)
			.hasFieldOrPropertyWithValue("installedChangedFlg", false);
	}

}