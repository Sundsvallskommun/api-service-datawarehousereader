package se.sundsvall.datawarehousereader.api;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;
import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.service.MeasurementService;
import se.sundsvall.dept44.problem.Problem;
import se.sundsvall.dept44.problem.violations.ConstraintViolationProblem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
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

	@MockitoBean
	private MeasurementService serviceMock;

	private UriBuilder setupDefaultParams(final UriBuilder uriBuilder) {
		uriBuilder.queryParam("partyId", PARTY_ID);
		uriBuilder.queryParam("facilityId", FACILITY_ID);
		uriBuilder.queryParam("fromDateTime", "{FROM_DATE_TIME}");
		uriBuilder.queryParam("toDateTime", "{TO_DATE_TIME}");

		return uriBuilder;
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
			"Failed to convert 'category' with value: 'not-valid-category'");

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
			"Failed to convert 'aggregateOn' with value: 'not-valid-aggregation'");

		verifyNoInteractions(serviceMock);
	}

}
