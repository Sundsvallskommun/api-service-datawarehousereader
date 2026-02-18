package se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation;

import com.google.code.beanmatchers.BeanMatchers;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

class InstallationEntityTest {

	@BeforeAll
	static void setup() {
		BeanMatchers.registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(InstallationEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {

		final var id = 666;
		final var careOf = "careOf";
		final var city = "city";
		final var company = "company";
		final var dateFrom = LocalDate.MIN;
		final var dateTo = LocalDate.MAX;
		final var houseName = "houseName";
		final var internalId = 321;
		final var facilityId = "facilityId";
		final var lastChangedDate = LocalDate.now();
		final var postCode = "postCode";
		final var street = "street";
		final var type = "type";
		final var customerFlag = false;
		final var metadata = InstallationMetaDataEmbeddable.create().withValue("value").withKey("key").withDisplayName("displayName").withCompany("company").withType("type");

		final var entity = InstallationEntity.create()
			.withId(id)
			.withCareOf(careOf)
			.withCity(city)
			.withCompany(company)
			.withDateFrom(dateFrom)
			.withDateTo(dateTo)
			.withFacilityId(facilityId)
			.withHouseName(houseName)
			.withInternalId(internalId)
			.withLastChangedDate(lastChangedDate)
			.withPostCode(postCode)
			.withStreet(street)
			.withType(type)
			.withCustomerFlag(customerFlag)
			.withMetaData(List.of(metadata));

		Assertions.assertThat(entity).isNotNull().hasNoNullFieldsOrProperties();
		Assertions.assertThat(entity.getId()).isEqualTo(id);
		Assertions.assertThat(entity.getCareOf()).isEqualTo(careOf);
		Assertions.assertThat(entity.getCity()).isEqualTo(city);
		Assertions.assertThat(entity.getCompany()).isEqualTo(company);
		Assertions.assertThat(entity.getDateFrom()).isEqualTo(dateFrom);
		Assertions.assertThat(entity.getDateTo()).isEqualTo(dateTo);
		Assertions.assertThat(entity.getFacilityId()).isEqualTo(facilityId);
		Assertions.assertThat(entity.getHouseName()).isEqualTo(houseName);
		Assertions.assertThat(entity.getInternalId()).isEqualTo(internalId);
		Assertions.assertThat(entity.getLastChangedDate()).isEqualTo(lastChangedDate);
		Assertions.assertThat(entity.getPostCode()).isEqualTo(postCode);
		Assertions.assertThat(entity.getStreet()).isEqualTo(street);
		Assertions.assertThat(entity.getType()).isEqualTo(type);
		Assertions.assertThat(entity.getCustomerFlag()).isEqualTo(customerFlag);
		Assertions.assertThat(entity.getMetaData()).hasSize(1).containsExactly(metadata);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		Assertions.assertThat(InstallationEntity.create()).hasAllNullFieldsOrProperties();
		Assertions.assertThat(new InstallationEntity()).hasAllNullFieldsOrProperties();
	}
}
