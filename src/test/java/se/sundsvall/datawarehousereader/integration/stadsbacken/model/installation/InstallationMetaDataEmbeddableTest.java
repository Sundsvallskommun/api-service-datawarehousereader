package se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class InstallationMetaDataEmbeddableTest {

	@Test
	void testBean() {
		assertThat(InstallationMetaDataEmbeddable.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var key = "key";
		final var value = "value";
		final var type = "type";
		final var displayName = "displayName";
		final var company = "company";

		final var entity = InstallationMetaDataEmbeddable.create()
			.withKey(key)
			.withValue(value)
			.withDisplayName(displayName)
			.withType(type)
			.withCompany(company);

		Assertions.assertThat(entity).isNotNull().hasNoNullFieldsOrProperties();
		Assertions.assertThat(entity.getKey()).isEqualTo(key);
		Assertions.assertThat(entity.getValue()).isEqualTo(value);
		Assertions.assertThat(entity.getDisplayName()).isEqualTo(displayName);
		Assertions.assertThat(entity.getType()).isEqualTo(type);
		Assertions.assertThat(entity.getCompany()).isEqualTo(company);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		Assertions.assertThat(InstallationMetaDataEmbeddable.create()).hasAllNullFieldsOrProperties();
		Assertions.assertThat(new InstallationMetaDataEmbeddable()).hasAllNullFieldsOrProperties();

	}
}
