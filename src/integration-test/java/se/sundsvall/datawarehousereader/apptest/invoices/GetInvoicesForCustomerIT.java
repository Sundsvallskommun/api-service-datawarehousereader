package se.sundsvall.datawarehousereader.apptest.invoices;

import org.junit.jupiter.api.Test;
import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

/**
 * Read invoices for a customer (paged + line-level details).
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 * @see src/test/resources/db/scripts/functions.sql for the fake function fnInvoiceNumberWithPagingAndSort.
 */
@WireMockAppTestSuite(files = "classpath:/GetInvoicesForCustomer/", classes = Application.class)
class GetInvoicesForCustomerIT extends AbstractAppTest {

	private static final String PATH = "/2281/invoices/customers/";
	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getForCustomerDefaults() {
		setupCall()
			.withServicePath(PATH + "600606")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getForCustomerWithPeriodFilter() {
		setupCall()
			.withServicePath(PATH + "600606?periodFrom=2019-10-09&periodTo=2019-10-09")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_getForCustomerWithOrganizationFilter() {
		setupCall()
			.withServicePath(PATH + "600606?organizationIds=5565027223")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_getForCustomerWithPaging() {
		setupCall()
			.withServicePath(PATH + "600606?page=1&limit=2&sortBy=InvoiceNumber")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test05_getForNonExistingCustomer() {
		setupCall()
			.withServicePath(PATH + "999999999")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
