package se.sundsvall.datawarehousereader.api.model.installation;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class InstallationDetailsResponseTest {

	@Test
	void testBean() {
		assertThat(InstallationDetailsResponse.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var metaData = PagingAndSortingMetaData.create();

		final var response = InstallationDetailsResponse.create()
			.withMetaData(metaData)
			.withInstallationDetails(List.of(InstallationDetails.create()));

		Assertions.assertThat(response).isNotNull().hasNoNullFieldsOrProperties();
		Assertions.assertThat(response.getMetaData()).isEqualTo(metaData);
		Assertions.assertThat(response.getInstallationDetails()).hasSize(1);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		Assertions.assertThat(InstallationDetailsResponse.create()).hasAllNullFieldsOrProperties();
		Assertions.assertThat(new InstallationDetailsResponse()).hasAllNullFieldsOrProperties();
	}
}
