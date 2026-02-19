package se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer;

import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

class MetadataEmbeddableTest {

	@Test
	void testBean() {
		assertThat(MetadataEmbeddable.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var count = 123;
		final var totalPages = 234;
		final var totalRecords = 345;

		final var entity = MetadataEmbeddable.create()
			.withCount(count)
			.withTotalPages(totalPages)
			.withTotalRecords(totalRecords);

		assertThat(entity).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(entity.getCount()).isEqualTo(count);
		assertThat(entity.getTotalPages()).isEqualTo(totalPages);
		assertThat(entity.getTotalRecords()).isEqualTo(totalRecords);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(MetadataEmbeddable.create()).hasAllNullFieldsOrPropertiesExcept("count", "totalPages", "totalRecords")
			.hasFieldOrPropertyWithValue("count", 0)
			.hasFieldOrPropertyWithValue("totalPages", 0)
			.hasFieldOrPropertyWithValue("totalRecords", 0);
		assertThat(new MetadataEmbeddable()).hasAllNullFieldsOrPropertiesExcept("count", "totalPages", "totalRecords")
			.hasFieldOrPropertyWithValue("count", 0)
			.hasFieldOrPropertyWithValue("totalPages", 0)
			.hasFieldOrPropertyWithValue("totalRecords", 0);
	}

}
