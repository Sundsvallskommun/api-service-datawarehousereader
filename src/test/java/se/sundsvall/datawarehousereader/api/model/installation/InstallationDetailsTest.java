package se.sundsvall.datawarehousereader.api.model.installation;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.code.beanmatchers.BeanMatchers;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InstallationDetailsTest {

	@BeforeAll
	static void setup() {
		BeanMatchers.registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(InstallationDetails.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var company = "company";
		final var type = "type";
		final var facilityId = "facilityId";
		final var placementId = 5;
		final var careOf = "careOf";
		final var street = "street";
		final var postCode = "postCode";
		final var city = "city";
		final var propertyDesignation = "propertyDesignation";
		final var dateFrom = LocalDate.MIN;
		final var dateTo = LocalDate.MAX;
		final var dateLastModified = LocalDate.MIN;
		final var metaData = List.of(InstallationMetaData.create());

		final var installation = InstallationDetails.create()
			.withCompany(company)
			.withType(type)
			.withFacilityId(facilityId)
			.withPlacementId(placementId)
			.withCareOf(careOf)
			.withStreet(street)
			.withPostCode(postCode)
			.withCity(city)
			.withPropertyDesignation(propertyDesignation)
			.withDateFrom(dateFrom)
			.withDateTo(dateTo)
			.withDateLastModified(dateLastModified)
			.withMetaData(metaData);

		Assertions.assertThat(installation).isNotNull().hasNoNullFieldsOrProperties();
		Assertions.assertThat(installation.getCompany()).isEqualTo(company);
		Assertions.assertThat(installation.getType()).isEqualTo(type);
		Assertions.assertThat(installation.getFacilityId()).isEqualTo(facilityId);
		Assertions.assertThat(installation.getPlacementId()).isEqualTo(placementId);
		Assertions.assertThat(installation.getCareOf()).isEqualTo(careOf);
		Assertions.assertThat(installation.getStreet()).isEqualTo(street);
		Assertions.assertThat(installation.getPostCode()).isEqualTo(postCode);
		Assertions.assertThat(installation.getCity()).isEqualTo(city);
		Assertions.assertThat(installation.getPropertyDesignation()).isEqualTo(propertyDesignation);
		Assertions.assertThat(installation.getDateFrom()).isEqualTo(dateFrom);
		Assertions.assertThat(installation.getDateTo()).isEqualTo(dateTo);
		Assertions.assertThat(installation.getDateLastModified()).isEqualTo(dateLastModified);
		Assertions.assertThat(installation.getMetaData()).isEqualTo(metaData);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		Assertions.assertThat(InstallationDetails.create())
			.hasAllNullFieldsOrPropertiesExcept("placementId")
			.hasFieldOrPropertyWithValue("placementId", 0);

		Assertions.assertThat(new InstallationDetails())
			.hasAllNullFieldsOrPropertiesExcept("placementId")
			.hasFieldOrPropertyWithValue("placementId", 0);
	}

}
