package se.sundsvall.datawarehousereader.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.zalando.problem.Status.BAD_REQUEST;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.datawarehousereader.service.InstallationService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class InstallationResourceFailuresTest {

	private static final String PATH = "/2281/installations";

	@MockitoBean
	private InstallationService serviceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getInstallationDetailsPageLessThanMinimum() {
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
	void getInstallationDetailsLimitLessThanMinimum() {
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
	void getInstallationDetailsLimitMoreThanMaximum() {
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
	void getInstallationDetailsNoValidSortBy() {
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
			tuple("installationParameters",
				"One or more of the sortBy properties [not-valid-property] are not valid. Valid properties to sort by are [facilityId, city, customerFlag, type, dateFrom, internalId, houseName, careOf, street, dateTo, company, postCode, id, lastChangedDate]."));

		verifyNoInteractions(serviceMock);
	}
}
