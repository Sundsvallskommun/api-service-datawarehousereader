package se.sundsvall.datawarehousereader.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.zalando.problem.Status.BAD_REQUEST;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.violations.ConstraintViolationProblem;

import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.datawarehousereader.service.AgreementService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class AgreementResourceFailuresTest {

	private static final String PATH = "/2281/agreements";

	@MockBean
	private AgreementService serviceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getAgreementsPageLessThanMinimum() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("page", 0)
			.build())
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
	void getAgreementsLimitLessThanMinimum() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("limit", 0)
			.build())
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
	void getCustomerEngagementsLimitMoreThanMaximum() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("limit", 1001)
			.build())
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
	void getAgreementsPartyIdNotValidUUID() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("partyId", "not-valid-uuid")
			.build())
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
	void getAgreementsNoValidCategory() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("category", "not-valid-category")
			.build())
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
			tuple("category", """
				Failed to convert property value of type 'java.lang.String' to required type 'java.util.List' for property 'category'; \
				Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.media.ArraySchema \
				se.sundsvall.datawarehousereader.api.model.Category] for value [not-valid-category]"""));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getAgreementsNoValidSortBy() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParam("sortBy", "not-valid-property")
			.build())
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
			tuple("agreementParameters",
				"""
					One or more of the sortBy properties [not-valid-property] are not valid. Valid properties to sort by are [mainAgreement, billingId, facilityId, toDate, description, binding, uuid, fromDate, agreementId, customerId, customerOrgId, category, bindingRule]."""));

		verifyNoInteractions(serviceMock);
	}
}
