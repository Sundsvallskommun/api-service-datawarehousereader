package se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.code.beanmatchers.BeanMatchers;

class InstalledBaseItemEntityTest {
	@BeforeAll
	static void setup() {
		BeanMatchers.registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(InstalledBaseItemEntity.class, allOf(
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
		final var customerId = 123;
		final var dateFrom = LocalDate.MIN;
		final var dateTo = LocalDate.MAX;
		final var houseName = "houseName";
		final var internalId = 321;
		final var metaData = List.of(new InstalledBaseItemMetaDataEmbeddable());
		final var facilityId = "facilityId";
		final var placementModified = LocalDate.now();
		final var postCode = "postCode";
		final var street = "street";
		final var type = "type";

		final var entity = InstalledBaseItemEntity.create()
			.withId(id)
			.withCareOf(careOf)
			.withCity(city)
			.withCompany(company)
			.withCustomerId(customerId)
			.withDateFrom(dateFrom)
			.withDateTo(dateTo)
			.withFacilityId(facilityId)
			.withHouseName(houseName)
			.withInternalId(internalId)
			.withMetaData(metaData)
			.withPlacementModified(placementModified)
			.withPostCode(postCode)
			.withStreet(street)
			.withType(type);

		assertThat(entity).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(entity.getId()).isEqualTo(id);
		assertThat(entity.getCareOf()).isEqualTo(careOf);
		assertThat(entity.getCity()).isEqualTo(city);
		assertThat(entity.getCompany()).isEqualTo(company);
		assertThat(entity.getCustomerId()).isEqualTo(customerId);
		assertThat(entity.getDateFrom()).isEqualTo(dateFrom);
		assertThat(entity.getDateTo()).isEqualTo(dateTo);
		assertThat(entity.getFacilityId()).isEqualTo(facilityId);
		assertThat(entity.getHouseName()).isEqualTo(houseName);
		assertThat(entity.getInternalId()).isEqualTo(internalId);
		assertThat(entity.getMetaData()).isEqualTo(metaData);
		assertThat(entity.getPlacementModified()).isEqualTo(placementModified);
		assertThat(entity.getPostCode()).isEqualTo(postCode);
		assertThat(entity.getStreet()).isEqualTo(street);
		assertThat(entity.getType()).isEqualTo(type);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(InstalledBaseItemEntity.create()).hasAllNullFieldsOrProperties();
		assertThat(new InstalledBaseItemEntity()).hasAllNullFieldsOrProperties();
	}
}
