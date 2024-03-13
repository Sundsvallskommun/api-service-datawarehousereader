package se.sundsvall.datawarehousereader.api.model.installation;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class InstallationMetaDataTest {

	@Test
	void testBean() {
		assertThat(InstallationMetaData.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var displayName = "displayName";
		final var key = "key";
		final var value = "value";
		final var type = "type";

		final var installation = InstallationMetaData.create()
			.withKey(key)
			.withDisplayName(displayName)
			.withValue(value)
			.withType(type);

		Assertions.assertThat(installation).isNotNull().hasNoNullFieldsOrProperties();
		Assertions.assertThat(installation.getType()).isEqualTo(type);
		Assertions.assertThat(installation.getDisplayName()).isEqualTo(displayName);
		Assertions.assertThat(installation.getKey()).isEqualTo(key);
		Assertions.assertThat(installation.getValue()).isEqualTo(value);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		Assertions.assertThat(InstallationMetaData.create())
			.hasAllNullFieldsOrProperties();

		Assertions.assertThat(new InstallationMetaData())
			.hasAllNullFieldsOrProperties();
	}
}
