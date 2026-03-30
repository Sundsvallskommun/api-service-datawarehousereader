package se.sundsvall.datawarehousereader.api.model.installedbase;

import java.time.LocalDate;
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

class InstalledBasePartyParametersTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(InstalledBasePartyParameters.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var organizationIds = "123456789,123456987";
		final var date = LocalDate.now().minusDays(30);
		final var sortBy = "Company";

		final var parameters = InstalledBasePartyParameters.create();
		parameters.setOrganizationIds(organizationIds);
		parameters.setDate(date);
		parameters.setSortBy(sortBy);

		assertThat(parameters).isNotNull();
		assertThat(parameters.getOrganizationIds()).isEqualTo(organizationIds);
		assertThat(parameters.getDate()).isEqualTo(date);
		assertThat(parameters.getSortBy()).isEqualTo(sortBy);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(InstalledBasePartyParameters.create())
			.hasAllNullFieldsOrPropertiesExcept("page", "limit")
			.hasFieldOrPropertyWithValue("page", 1)
			.hasFieldOrPropertyWithValue("limit", 100);

		assertThat(new InstalledBasePartyParameters())
			.hasAllNullFieldsOrPropertiesExcept("page", "limit")
			.hasFieldOrPropertyWithValue("page", 1)
			.hasFieldOrPropertyWithValue("limit", 100);
	}
}
