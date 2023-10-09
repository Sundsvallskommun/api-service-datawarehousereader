package se.sundsvall.datawarehousereader.api.model.agreement;

import org.junit.jupiter.api.Test;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import java.util.List;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class AgreementResponseTest {

	@Test
	void testBean() {
		assertThat(AgreementResponse.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var metaData = PagingAndSortingMetaData.create();
		final var agreement = Agreement.create();

		final var response = AgreementResponse.create()
			.withMetaData(metaData)
			.withAgreements(List.of(agreement));

		assertThat(response.getMetaData()).isEqualTo(metaData);
		assertThat(response.getAgreements()).hasSize(1).contains(agreement);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(AgreementResponse.create()).hasAllNullFieldsOrProperties();
		assertThat(new AgreementResponse()).hasAllNullFieldsOrProperties();
	}
}
