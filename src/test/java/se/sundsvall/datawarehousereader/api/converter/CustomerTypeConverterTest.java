package se.sundsvall.datawarehousereader.api.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zalando.problem.Status.BAD_REQUEST;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.datawarehousereader.api.model.CustomerType;

class CustomerTypeConverterTest {

	private static final CustomerTypeConverter CONVERTER = new CustomerTypeConverter();

	@Test
	void testDefinedCustomerTypes() {

		// Verify that all defined customer types can be converted by the converter (i.e. no exceptions are thrown)
		Stream.of(CustomerType.values())
			.map(CustomerType::toString)
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
		ThrowableProblem e = assertThrows(ThrowableProblem.class, () -> CONVERTER.convert("invalid-customer-type"));
		assertThat(e.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(e.getMessage()).isEqualTo("Bad Request: Invalid value for enum CustomerType: invalid-customer-type");
	}
}
