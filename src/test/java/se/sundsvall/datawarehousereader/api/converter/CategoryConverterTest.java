package se.sundsvall.datawarehousereader.api.converter;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.dept44.problem.ThrowableProblem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class CategoryConverterTest {

	private static final CategoryConverter CONVERTER = new CategoryConverter();

	@Test
	void testDefinedCategories() {
		// Verify that all defined categories can be converted by the converter (i.e. no exceptions are thrown)
		Stream.of(Category.values())
			.map(Category::toString)
			.forEach(string -> assertDoesNotThrow(() -> CONVERTER.convert(string)));
	}

	@Test
	void testNullValue() {
		assertThat(CONVERTER.convert(null)).isNull();
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"", " "
	})
	void testEmptyValues(String category) {
		assertThat(CONVERTER.convert(category)).isNull();
	}

	@Test
	void testInvalidCategory() {
		ThrowableProblem e = assertThrows(ThrowableProblem.class, () -> CONVERTER.convert("invalid-category"));
		assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(e.getMessage()).isEqualTo("Bad Request: Invalid value for enum Category: invalid-category");
	}
}
