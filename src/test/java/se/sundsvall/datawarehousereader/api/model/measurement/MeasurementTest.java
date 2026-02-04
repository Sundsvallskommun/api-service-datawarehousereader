package se.sundsvall.datawarehousereader.api.model.measurement;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MeasurementTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDateTime.class);
	}

	@Test
	void testBean() {
		assertThat(Measurement.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderPattern() {
		final var uuid = "uuid";
		final var customerOrgId = "customerOrgId";
		final var facilityId = "facilityId";
		final var feedType = "feedType";
		final var unit = "unit";
		final var usage = 123;
		final var interpolation = 456;
		final var dateAndTime = LocalDateTime.now();

		final var measurement = Measurement.create()
			.withUuid(uuid)
			.withCustomerOrgId(customerOrgId)
			.withFacilityId(facilityId)
			.withFeedType(feedType)
			.withUnit(unit)
			.withUsage(usage)
			.withInterpolation(interpolation)
			.withDateAndTime(dateAndTime);

		assertThat(measurement).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(measurement.getUuid()).isEqualTo(uuid);
		assertThat(measurement.getCustomerOrgId()).isEqualTo(customerOrgId);
		assertThat(measurement.getFacilityId()).isEqualTo(facilityId);
		assertThat(measurement.getFeedType()).isEqualTo(feedType);
		assertThat(measurement.getUnit()).isEqualTo(unit);
		assertThat(measurement.getUsage()).isEqualTo(usage);
		assertThat(measurement.getInterpolation()).isEqualTo(interpolation);
		assertThat(measurement.getDateAndTime()).isEqualTo(dateAndTime);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Measurement.create()).hasAllNullFieldsOrProperties();

		assertThat(new Measurement()).hasAllNullFieldsOrProperties();
	}
}
