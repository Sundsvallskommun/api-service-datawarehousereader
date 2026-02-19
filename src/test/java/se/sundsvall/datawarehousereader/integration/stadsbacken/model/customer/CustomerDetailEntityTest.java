package se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer;

import java.time.LocalDateTime;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

class CustomerDetailEntityTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDateTime.class);
	}

	@Test
	void testBean() {
		assertThat(CustomerDetailEntity.class, allOf(
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
		final var organizationId = "organizationId";
		final var organizationName = "organizationName";
		final var active = true;
		final var moveInDate = LocalDateTime.now().plusDays(new Random().nextInt());
		final var metadata = MetadataEmbeddable.create();

		final var entity = CustomerDetailEntity.create()
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
			.withPartyId(uuid)
			.withOrganizationId(organizationId)
			.withOrganizationName(organizationName)
			.withActive(active)
			.withMoveInDate(moveInDate)
			.withMetadata(metadata);

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
		assertThat(entity.getPartyId()).isEqualTo(uuid);
		assertThat(entity.getOrganizationId()).isEqualTo(organizationId);
		assertThat(entity.getOrganizationName()).isEqualTo(organizationName);
		assertThat(entity.isActive()).isEqualTo(active);
		assertThat(entity.getMoveInDate()).isEqualTo(moveInDate);
		assertThat(entity.getMetadata()).isEqualTo(metadata);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CustomerDetailEntity.create()).hasAllNullFieldsOrPropertiesExcept("customerChangedFlg", "installedChangedFlg", "active")
			.hasFieldOrPropertyWithValue("customerChangedFlg", false)
			.hasFieldOrPropertyWithValue("installedChangedFlg", false)
			.hasFieldOrPropertyWithValue("active", false);
		assertThat(new CustomerDetailEntity()).hasAllNullFieldsOrPropertiesExcept("customerChangedFlg", "installedChangedFlg", "active")
			.hasFieldOrPropertyWithValue("customerChangedFlg", false)
			.hasFieldOrPropertyWithValue("installedChangedFlg", false)
			.hasFieldOrPropertyWithValue("active", false);
	}

}
