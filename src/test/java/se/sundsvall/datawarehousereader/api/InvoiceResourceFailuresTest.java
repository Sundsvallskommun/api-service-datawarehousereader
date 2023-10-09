package se.sundsvall.datawarehousereader.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.datawarehousereader.service.InvoiceService;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class InvoiceResourceFailuresTest {

	@MockBean
	private InvoiceService serviceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getInvoicesPageLessThanMinimum() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
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
	void getInvoicesLimitLessThanMinimum() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
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
	void getInvoicesLimitMoreThanMaximum() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
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
	void getInvoicesWithNotNumericValues() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
				.queryParam("invoiceNumber", "not-numeric")
				.queryParam("ocrNumber", "not-numeric")
				.build())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("invoiceNumber")
			.jsonPath("$.violations[0].message").isEqualTo("""
				Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Long' for property 'invoiceNumber'; \
				For input string: "not-numeric\"""")
			.jsonPath("$.violations[1].field").isEqualTo("ocrNumber")
			.jsonPath("$.violations[1].message").isEqualTo("""
				Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Long' for property 'ocrNumber'; \
				For input string: "not-numeric\"""");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getInvoicesWithInvalidDates() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
				.queryParam("invoiceDateFrom", "not-valid-date")
				.queryParam("invoiceDateTo", "not-valid-date")
				.queryParam("dueDateFrom", "not-valid-date")
				.queryParam("dueDateTo", "not-valid-date")
				.build())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("dueDateFrom")
			.jsonPath("$.violations[0].message").isEqualTo("""
				Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'dueDateFrom'; \
				Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.media.Schema \
				@org.springframework.format.annotation.DateTimeFormat java.time.LocalDate] for value [not-valid-date]""")
			.jsonPath("$.violations[1].field").isEqualTo("dueDateTo")
			.jsonPath("$.violations[1].message").isEqualTo("""
				Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'dueDateTo'; \
				Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.media.Schema \
				@org.springframework.format.annotation.DateTimeFormat java.time.LocalDate] for value [not-valid-date]""")
			.jsonPath("$.violations[2].field").isEqualTo("invoiceDateFrom")
			.jsonPath("$.violations[2].message").isEqualTo("""
				Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'invoiceDateFrom'; \
				Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.media.Schema \
				@org.springframework.format.annotation.DateTimeFormat java.time.LocalDate] for value [not-valid-date]""")
			.jsonPath("$.violations[3].field").isEqualTo("invoiceDateTo")
			.jsonPath("$.violations[3].message").isEqualTo("""
				Failed to convert property value of type 'java.lang.String' to required type 'java.time.LocalDate' for property 'invoiceDateTo'; \
				Failed to convert from type [java.lang.String] to type [@io.swagger.v3.oas.annotations.media.Schema \
				@org.springframework.format.annotation.DateTimeFormat java.time.LocalDate] for value [not-valid-date]""");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getInvoicesNoValidSortBy() {
		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/invoices")
				.queryParam("sortBy", "not-valid-property")
				.build())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("invoiceParameters")
			.jsonPath("$.violations[0].message").isEqualTo("""
				One or more of the sortBy properties [not-valid-property] are not valid. Valid properties to sort by are \
				[invoiceName, ocrNumber, invoiceDescription, city, administration, dueDate, organizationId, customerType, \
				street, invoiceNumber, customerId, invoiceType, currency, organizationGroup, facilityId, amountVatIncluded, \
				vat, vatEligibleAmount, rounding, invoiceDate, totalAmount, reversedVat, pdfAvailable, careOf, postCode, \
				invoiceStatus, amountVatExcluded].""");

		verifyNoInteractions(serviceMock);
	}

	@Test
	void getInvoicesDetailsInvalidOrganizationNumber() {
		webTestClient.get().uri("/invoices/{organizationNumber}/{invoiceNumber}/details", "INVALID_ORG_NUMBER", "123")
			.exchange()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("getInvoiceDetails.organizationNumber")
			.jsonPath("$.violations[0].message", "must match the regular expression ^([1235789][\\d][2-9]\\d{7})$");

		verifyNoInteractions(serviceMock);
	}
}
