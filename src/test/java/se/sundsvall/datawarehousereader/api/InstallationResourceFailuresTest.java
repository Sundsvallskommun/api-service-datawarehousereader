package se.sundsvall.datawarehousereader.api;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.datawarehousereader.service.InstallationService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class InstallationResourceFailuresTest {

	@MockBean
	private InstallationService serviceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getInstallationDetailsPageLessThanMinimum() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/installations")
				.queryParam("page", 0)
				.build())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("page")
			.jsonPath("$.violations[0].message").isEqualTo("must be greater than or equal to 1");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getInstallationDetailsLimitLessThanMinimum() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/installations")
				.queryParam("limit", 0)
				.build())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("limit")
			.jsonPath("$.violations[0].message").isEqualTo("must be greater than or equal to 1");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getInstallationDetailsLimitMoreThanMaximum() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/installations")
				.queryParam("limit", 1001)
				.build())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("limit")
			.jsonPath("$.violations[0].message").isEqualTo("Page limit cannot be greater than 1000");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getInstallationDetailsNoValidSortBy() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/installations")
				.queryParam("sortBy", "not-valid-property")
				.build())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("installationParameters")
			.jsonPath("$.violations[0].message").isEqualTo("One or more of the sortBy properties [not-valid-property] are not valid. Valid properties to sort by are [facilityId, city, customerFlag, type, dateFrom, internalId, houseName, careOf, street, dateTo, company, postCode, id, lastChangedDate].");

		verifyNoInteractions(serviceMock);
	}
}
