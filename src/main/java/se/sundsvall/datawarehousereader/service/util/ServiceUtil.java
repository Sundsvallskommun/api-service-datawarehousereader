package se.sundsvall.datawarehousereader.service.util;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.containsNone;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ServiceUtil {

	private static final int PERSON_NUMBER_MIN_LENGTH = 4;
	private static final int PERSON_NUMBER_HYPHEN_POSITION_FROM_RIGHT = 4;

	private ServiceUtil() {}

	/**
	 * Method to add a hyphen after position 4 when string passes the following tests:
	 * - string is not null
	 * - string has a minimum length of 4
	 * - string contains no hyphen
	 * If sent in string doesn't pass the test above, the string is returned untouched.
	 *
	 * @param  personNumber string
	 * @return              string with hyphen added or untouched string if it doesn't pass the tests above
	 */
	public static String addHyphen(String personNumber) {
		return ofNullable(personNumber)
			.filter(string -> string.length() >= PERSON_NUMBER_MIN_LENGTH)
			.filter(string -> containsNone(string, "-"))
			.map(string -> new StringBuilder(string).insert(string.length() - PERSON_NUMBER_HYPHEN_POSITION_FROM_RIGHT, "-").toString())
			.orElse(personNumber);
	}

	/**
	 * Method to remove hyphen from sent in person number
	 *
	 * @param  personNumber string
	 * @return              string without hyphen or untouched string if it is null
	 */
	public static String removeHyphen(String personNumber) {
		return ofNullable(personNumber)
			.map(string -> string.replace("-", ""))
			.orElse(personNumber);
	}

	/**
	 * Method for converting Integer to String
	 *
	 * @param  integer integer
	 * @return         string representation of sent in integer or null if integer equals null
	 */
	public static String toString(Integer integer) {
		return ofNullable(integer)
			.map(String::valueOf)
			.orElse(null);
	}

	/**
	 * Method for converting String to Boolean
	 *
	 * @param  string The string to be converted (may be null) where case insensitive comparison to value 'true'
	 *                after string trimming will result in true. All other values execpt null will be converted
	 *                to false.
	 * @return        Boolean representation of sent in string or null if string equals null
	 */
	public static Boolean toBoolean(String string) {
		return ofNullable(string)
			.map(String::trim)
			.map(Boolean::valueOf)
			.orElse(null);
	}

	/**
	 * Method for converting Boolean to String
	 *
	 * @param  bool boolean
	 * @return      string representation of sent in boolean or null if boolean equals null
	 */
	public static String toString(Boolean bool) {
		return ofNullable(bool)
			.map(String::valueOf)
			.orElse(null);
	}

	/**
	 * Method for converting String to Integer
	 *
	 * @param  value string
	 * @return       integer representation of sent in string or null if string equals null
	 */
	public static Integer toInteger(String value) {
		return ofNullable(value)
			.map(Integer::valueOf)
			.orElse(null);
	}

	/**
	 * Method for converting list of strings to list of integers
	 *
	 * @param  values list of strings
	 * @return        list of integer representation of sent in list of strings or empty-list if list of strings equals null
	 */
	public static List<Integer> toIntegers(List<String> values) {
		return ofNullable(values).orElse(emptyList()).stream()
			.filter(Objects::nonNull)
			.map(Integer::valueOf)
			.toList();
	}

	public static LocalDate toLocalDate(LocalDateTime localDateTime) {
		return ofNullable(localDateTime)
			.map(LocalDateTime::toLocalDate)
			.orElse(null);
	}
}
