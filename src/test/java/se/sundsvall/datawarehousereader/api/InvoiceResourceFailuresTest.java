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
import se.sundsvall.datawarehousereader.service.InvoiceService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class InvoiceResourceFailuresTest {

	@MockBean
	private InvoiceService serviceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getInvoicesPageLessThanMinimum() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
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
	void getInvoicesLimitLessThanMinimum() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
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
	void getInvoicesLimitMoreThanMaximum() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
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
	void getInvoicesWithNotNumericValues() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
				.queryParam("invoiceNumber", "not-numeric")
				.queryParam("ocrNumber", "not-numeric")
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
			tuple("invoiceNumber", "Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Long' for property 'invoiceNumber'; For input string: \"not-numeric\""),
			tuple("ocrNumber", "Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Long' for property 'ocrNumber'; For input string: \"not-numeric\""));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getInvoicesWithInvalidDates() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
				.queryParam("invoiceDateFrom", "not-valid-date")
				.queryParam("invoiceDateTo", "not-valid-date")
				.queryParam("dueDateFrom", "not-valid-date")
				.queryParam("dueDateTo", "not-valid-date")
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
			tuple("dueDateFrom", "Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'dueDateFrom'; Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.media.Schema @org.springframework.format.annotation.DateTimeFormat java.time.LocalDate] for value [not-valid-date]"),
			tuple("dueDateTo", "Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'dueDateTo'; Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.media.Schema @org.springframework.format.annotation.DateTimeFormat java.time.LocalDate] for value [not-valid-date]"),
			tuple("invoiceDateFrom", "Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'invoiceDateFrom'; Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.media.Schema @org.springframework.format.annotation.DateTimeFormat java.time.LocalDate] for value [not-valid-date]"),
			tuple("invoiceDateTo", "Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'invoiceDateTo'; Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.media.Schema @org.springframework.format.annotation.DateTimeFormat java.time.LocalDate] for value [not-valid-date]"));

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getInvoicesNoValidSortBy() {
		final var response = webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
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
			tuple("invoiceParameters", "One or more of the sortBy properties [not-valid-property] are not valid. Valid properties to sort by are [invoiceName, ocrNumber, invoiceDescription, city, administration, dueDate, organizationId, customerType, street, invoiceNumber, customerId, invoiceType, currency, organizationGroup, facilityId, amountVatIncluded, vat, vatEligibleAmount, rounding, invoiceDate, totalAmount, reversedVat, pdfAvailable, careOf, postCode, invoiceStatus, amountVatExcluded]."));
		verifyNoInteractions(serviceMock);
	}

	@Test
	void getInvoicesDetailsInvalidOrganizationNumber() {
		final var response = webTestClient.get().uri("/invoices/{organizationNumber}/{invoiceNumber}/details", "INVALID_ORG_NUMBER", "123")
			.exchange()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations()).extracting("field", "message").containsExactlyInAnyOrder(
			tuple("getInvoiceDetails.organizationNumber", "must match the regular expression ^([1235789][\\d][2-9]\\d{7})$"));

		verifyNoInteractions(serviceMock);
	}
}
