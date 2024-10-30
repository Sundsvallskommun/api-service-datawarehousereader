package se.sundsvall.datawarehousereader.api.model.customer;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

class CustomerDetailsResponseTest {

	@Test
	void testBean() {
		assertThat(CustomerDetailsResponse.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testCreatePattern() {
		final var metaData = PagingAndSortingMetaData.create();
		final var customer = CustomerDetails.create();

		final var response = CustomerDetailsResponse.create()
			.withMetadata(metaData)
			.withCustomerDetails(List.of(customer));

		assertThat(response).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(response.getMetaData()).isEqualTo(metaData);
		assertThat(response.getCustomerDetails()).hasSize(1).contains(customer);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(CustomerDetailsResponse.create()).hasAllNullFieldsOrProperties();
		assertThat(new CustomerDetailsResponse()).hasAllNullFieldsOrProperties();
	}

}
