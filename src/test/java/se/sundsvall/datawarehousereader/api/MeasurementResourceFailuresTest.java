package se.sundsvall.datawarehousereader.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.service.MeasurementService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.zalando.problem.Status.BAD_REQUEST;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class MeasurementResourceFailuresTest {

	private static final String PATH = "/{municipalityId}/measurements/{category}/{aggregateOn}";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String PARTY_ID = UUID.randomUUID().toString();
	private static final String FACILITY_ID = "facilityId";
	private static final Category CATEGORY = Category.DISTRICT_HEATING;
	private static final Aggregation AGGREGATION = Aggregation.DAY;
	private static final String FROM_DATE_TIME = "2022-05-01T00:00:00+01:00";
	private static final String TO_DATE_TIME = "2022-05-31T23:59:59+01:00";

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private MeasurementService serviceMock;

	private UriBuilder setupDefaultParams(final UriBuilder uriBuilder) {
		uriBuilder.queryParam("partyId", PARTY_ID);
		uriBuilder.queryParam("facilityId", FACILITY_ID);
		uriBuilder.queryParam("fromDateTime", "{FROM_DATE_TIME}");
		uriBuilder.queryParam("toDateTime", "{TO_DATE_TIME}");

		return uriBuilder;
	}

	@Test
	void getMeasurementsPageLessThanMinimum() {
		final var response = webTestClient.get().uri(uriBuilder -> setupDefaultParams(uriBuilder.path(PATH))
			.queryParam("page", 0)
			.build(MUNICIPALITY_ID, CATEGORY, AGGREGATION, FROM_DATE_TIME, TO_DATE_TIME))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations()).extracting("field", "message").containsExactlyInAnyOrder(
			tuple("page", "must be greater than or equal to 1"));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getMeasurementsLimitLessThanMinimum() {
		final var response = webTestClient.get().uri(uriBuilder -> setupDefaultParams(uriBuilder.path(PATH))
			.queryParam("limit", 0)
			.build(MUNICIPALITY_ID, CATEGORY, AGGREGATION, FROM_DATE_TIME, TO_DATE_TIME))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations()).extracting("field", "message").containsExactlyInAnyOrder(
			tuple("limit", "must be greater than or equal to 1"));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getMeasurementsLimitMoreThanMaximum() {
		final var response = webTestClient.get().uri(uriBuilder -> setupDefaultParams(uriBuilder.path(PATH))
			.queryParam("limit", 1001)
			.build(MUNICIPALITY_ID, CATEGORY, AGGREGATION, FROM_DATE_TIME, TO_DATE_TIME))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations()).extracting("field", "message").containsExactlyInAnyOrder(
			tuple("limit", "Page limit cannot be greater than 1000"));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getMeasurementsPartyIdNotValidUUID() {
		final var response = webTestClient.get().uri(uriBuilder -> setupDefaultParams(uriBuilder.path(PATH))
			.replaceQueryParam("partyId", "not-valid-uuid")
			.build(MUNICIPALITY_ID, CATEGORY, AGGREGATION, FROM_DATE_TIME, TO_DATE_TIME))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations()).extracting("field", "message").containsExactlyInAnyOrder(
			tuple("partyId", "not a valid UUID"));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getMeasurementsNotValidCategory() {
		final var response = webTestClient.get().uri(uriBuilder -> setupDefaultParams(uriBuilder.path(PATH))
			.build(MUNICIPALITY_ID, "not-valid-category", AGGREGATION, FROM_DATE_TIME, TO_DATE_TIME))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Bad Request");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo(
			"Method parameter 'category': Failed to convert value of type 'java.lang.String' to required type 'se.sundsvall.datawarehousereader.api.model.Category'; Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.Parameter @org.springframework.web.bind.annotation.PathVariable se.sundsvall.datawarehousereader.api.model.Category] for value [not-valid-category]");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getMeasurementsNotValidAggregation() {
		final var response = webTestClient.get().uri(uriBuilder -> setupDefaultParams(uriBuilder.path(PATH))
			.build(MUNICIPALITY_ID, CATEGORY, "not-valid-aggregation", FROM_DATE_TIME, TO_DATE_TIME))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Bad Request");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo(
			"Method parameter 'aggregateOn': Failed to convert value of type 'java.lang.String' to required type 'se.sundsvall.datawarehousereader.api.model.measurement.Aggregation'; Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.Parameter @org.springframework.web.bind.annotation.PathVariable se.sundsvall.datawarehousereader.api.model.measurement.Aggregation] for value [not-valid-aggregation]");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getMeasurementsNoValidSortBy() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("sortBy", "not-valid-property")
			.build(MUNICIPALITY_ID, CATEGORY, AGGREGATION))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations()).extracting("field", "message").containsExactlyInAnyOrder(
			tuple("measurementParameters",
				"One or more of the sortBy properties [not-valid-property] are not valid. Valid properties to sort by are [interpolation, unit, readingSequence, facilityId, feedType, measurementTimestamp, usage, customerOrgId, uuid]."));

		verifyNoInteractions(serviceMock);
	}
}
