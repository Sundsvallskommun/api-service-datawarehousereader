package se.sundsvall.datawarehousereader.service.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ServiceUtilTest {

	@Test
	void testAddHyphenOnNull() {
		assertThat(ServiceUtil.addHyphen(null)).isNull();
	}

	@Test
	void testAddHyphenOnEmptyString() {
		assertThat(ServiceUtil.addHyphen("")).isEmpty();
	}

	@Test
	void testAddHyphenOnStringWithHyphen() {
		assertThat(ServiceUtil.addHyphen("12345-67890")).isEqualTo("12345-67890");
	}

	@Test
	void testAddHyphenOnInvalidStrings() {
		assertThat(ServiceUtil.addHyphen("1")).isEqualTo("1");
		assertThat(ServiceUtil.addHyphen("12")).isEqualTo("12");
		assertThat(ServiceUtil.addHyphen("123")).isEqualTo("123");
	}

	@Test
	void testAddHyphenOnValidStrings() {
		assertThat(ServiceUtil.addHyphen("1234")).isEqualTo("-1234");
		assertThat(ServiceUtil.addHyphen("12345")).isEqualTo("1-2345");
		assertThat(ServiceUtil.addHyphen("123456")).isEqualTo("12-3456");
		assertThat(ServiceUtil.addHyphen("1234567")).isEqualTo("123-4567");
		assertThat(ServiceUtil.addHyphen("12345678")).isEqualTo("1234-5678");
		assertThat(ServiceUtil.addHyphen("123456789")).isEqualTo("12345-6789");
		assertThat(ServiceUtil.addHyphen("1234567890")).isEqualTo("123456-7890");
		assertThat(ServiceUtil.addHyphen("12345678901")).isEqualTo("1234567-8901");
		assertThat(ServiceUtil.addHyphen("123456789012")).isEqualTo("12345678-9012");
		assertThat(ServiceUtil.addHyphen("1234567890123")).isEqualTo("123456789-0123");
	}

	@Test
	void testRemoveHyphenFromNull() {
		assertThat(ServiceUtil.removeHyphen(null)).isNull();
	}

	@Test
	void testRemoveHyphenFromEmptyString() {
		assertThat(ServiceUtil.removeHyphen("")).isEmpty();
	}

	@Test
	void testRemoveHyphenFromStringNotContainingHyphen() {
		assertThat(ServiceUtil.removeHyphen("abcd1234")).isEqualTo("abcd1234");
	}

	@Test
	void testRemoveHyphen() {
		assertThat(ServiceUtil.removeHyphen("abcd-1234-efgh-5678")).isEqualTo("abcd1234efgh5678");
	}

	@Test
	void testStringToInteger() {
		assertThat(ServiceUtil.toInteger((String) null)).isNull();
		assertThat(ServiceUtil.toInteger("1337")).isEqualTo(Integer.valueOf(1337));
	}

	@Test
	void testStringsToIntegers() {
		assertThat(ServiceUtil.toIntegers(null)).isEmpty();
		assertThat(ServiceUtil.toIntegers(List.of("1337", "1338"))).isEqualTo(List.of(1337, 1338));
	}

	@Test
	void testIntegerToString() {
		assertThat(ServiceUtil.toString((Integer) null)).isNull();
		assertThat(ServiceUtil.toString(1337)).isEqualTo("1337");
	}

	@Test
	void testBooleanToString() {
		assertThat(ServiceUtil.toString((Boolean) null)).isNull();
		assertThat(ServiceUtil.toString(Boolean.TRUE)).isEqualTo("true");
		assertThat(ServiceUtil.toString(Boolean.FALSE)).isEqualTo("false");
	}

	private static Stream<Arguments> stringToBooleanAgrumentProvider() {
		return Stream.of(
			Arguments.of("true", Boolean.TRUE),
			Arguments.of("TRUE", Boolean.TRUE),
			Arguments.of("TrUe", Boolean.TRUE),
			Arguments.of(" true ", Boolean.TRUE),
			Arguments.of("false", Boolean.FALSE),
			Arguments.of("trie", Boolean.FALSE),
			Arguments.of(null, null));
	}

	@ParameterizedTest
	@MethodSource("stringToBooleanAgrumentProvider")
	void testStringToBoolean(String value, Boolean expected) {
		assertThat(ServiceUtil.toBoolean(value)).isEqualTo(expected);
	}

	@Test
	void testToLocalDate() {
		assertThat(ServiceUtil.toLocalDate(null)).isNull();
		assertThat(ServiceUtil.toLocalDate(LocalDateTime.now())).isEqualTo(LocalDate.now());
	}
}
