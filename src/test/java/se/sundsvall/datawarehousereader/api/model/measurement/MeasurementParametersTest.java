package se.sundsvall.datawarehousereader.api.model.measurement;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.OffsetDateTime;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MeasurementParametersTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), OffsetDateTime.class);
	}

	@Test
	void testBean() {
		assertThat(MeasurementParameters.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderPattern() {
		final var partyId = "81471222-5798-11e9-ae24-57fa13b361e1";
		final var facilityId = "735999109151401011";
		final var fromDateTime = OffsetDateTime.now().minusDays(30);
		final var toDateTime = OffsetDateTime.now();

		final var parameters = MeasurementParameters.create()
			.withPartyId(partyId)
			.withFacilityId(facilityId)
			.withFromDateTime(fromDateTime)
			.withToDateTime(toDateTime);

		assertThat(parameters).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(parameters.getPartyId()).isEqualTo(partyId);
		assertThat(parameters.getFacilityId()).isEqualTo(facilityId);
		assertThat(parameters.getFromDateTime()).isEqualTo(fromDateTime);
		assertThat(parameters.getToDateTime()).isEqualTo(toDateTime);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(MeasurementParameters.create()).hasAllNullFieldsOrProperties();
		assertThat(new MeasurementParameters())
			.hasAllNullFieldsOrProperties();
	}
}
