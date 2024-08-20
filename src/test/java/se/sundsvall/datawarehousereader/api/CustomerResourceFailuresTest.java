package se.sundsvall.datawarehousereader.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
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
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsResponse;
import se.sundsvall.datawarehousereader.service.CustomerService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class CustomerResourceFailuresTest {

	private static final String PATH_CUSTOMER_ENGAGEMENTS = "/2281/customer/engagements";
	private static final String PATH_CUSTOMER_DETAILS = "/2281/customer/details";

	@MockBean
	private CustomerService serviceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getCustomerEngagementsPageLessThanMinimum() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH_CUSTOMER_ENGAGEMENTS)
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
	void getCustomerEngagementsLimitLessThanMinimum() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH_CUSTOMER_ENGAGEMENTS)
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
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH_CUSTOMER_ENGAGEMENTS)
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
	void getCustomerEngagementsPartyIdNotValidUUID() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH_CUSTOMER_ENGAGEMENTS)
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
			tuple("partyId[0]", "not a valid UUID"));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getCustomerEngagementsNoValidSortBy() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH_CUSTOMER_ENGAGEMENTS)
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
			tuple("customerEngagementParameters",
				"One or more of the sortBy properties [not-valid-property] are not valid. Valid properties to sort by are [organizationId, customerType, organizationName, moveInDate, customerId, customerOrgId, active]."));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getCustomerDetailsNoPartyIdAndCustomerEngagementOrgId() {
		when(serviceMock.getCustomerDetails(any())).thenReturn(CustomerDetailsResponse.create());

		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH_CUSTOMER_DETAILS)
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
			tuple("customerEngagementOrgId", "must match the regular expression ^([1235789][\\d][2-9]\\d{7})$"),
			tuple("customerDetailsParameters", "'customerEngagementOrgId' must be provided"),
			tuple("customerDetailsParameters",
				"One or more of the sortBy properties [not-valid-property] are not valid. Valid properties to sort by are [installedChangedFlg, address, organizationName, city, phone2, phone3, active, customerCategoryID, co, customerChangedFlg, phone1, organizationId, zipcode, email2, email1, customerCategoryDescription, moveInDate, customerId, name, customerOrgId, partyId]."));

		verifyNoInteractions(serviceMock);
	}
}
