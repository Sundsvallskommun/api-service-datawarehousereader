package se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

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
		final var uuid = "123893ec-3b89-4660-a316-f4b2b83c4689";

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
			.withInstalledChangedFlg(installedChangedFlg)
			.withUuid(uuid);

		assertThat(entity).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(entity.getCustomerId()).isEqualTo(customerId);
		assertThat(entity.getCustomerOrgId()).isEqualTo(customerOrgId);
		assertThat(entity.getCustomerCategoryID()).isEqualTo(customerCategoryID);
		assertThat(entity.getCustomerCategoryDescription()).isEqualTo(customerCategoryDescription);
		assertThat(entity.getName()).isEqualTo(name);
		assertThat(entity.getCo()).isEqualTo(co);
		assertThat(entity.getAddress()).isEqualTo(address);
		assertThat(entity.getZipcode()).isEqualTo(zipcode);
		assertThat(entity.getCity()).isEqualTo(city);
		assertThat(entity.getPhone1()).isEqualTo(phone1);
		assertThat(entity.getPhone2()).isEqualTo(phone2);
		assertThat(entity.getPhone3()).isEqualTo(phone3);
		assertThat(entity.getEmail1()).isEqualTo(email1);
		assertThat(entity.getEmail2()).isEqualTo(email2);
		assertThat(entity.isCustomerChangedFlg()).isEqualTo(customerChangedFlg);
		assertThat(entity.isInstalledChangedFlg()).isEqualTo(installedChangedFlg);
		assertThat(entity.getUuid()).isEqualTo(uuid);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CustomerDetailsEntity.create()).hasAllNullFieldsOrPropertiesExcept("customerChangedFlg", "installedChangedFlg")
			.hasFieldOrPropertyWithValue("customerChangedFlg", false)
			.hasFieldOrPropertyWithValue("installedChangedFlg", false);
		assertThat(new CustomerDetailsEntity()).hasAllNullFieldsOrPropertiesExcept("customerChangedFlg", "installedChangedFlg")
			.hasFieldOrPropertyWithValue("customerChangedFlg", false)
			.hasFieldOrPropertyWithValue("installedChangedFlg", false);
	}

}