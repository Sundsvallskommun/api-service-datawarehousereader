package se.sundsvall.datawarehousereader.apptest.invoices;

import org.junit.jupiter.api.Test;
import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

/**
 * Read invoices for one or more customers (paged + line-level details).
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 * @see src/test/resources/db/scripts/functions.sql for the fake function fnInvoiceNumberWithPagingAndSort.
 */
@WireMockAppTestSuite(files = "classpath:/GetInvoicesForCustomer/", classes = Application.class)
class GetInvoicesForCustomerIT extends AbstractAppTest {

	private static final String PATH = "/2281/invoices/customers";
	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getForCustomerDefaults() {
		setupCall()
			.withServicePath(PATH + "?customerNumbers=600606")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getForCustomerWithPeriodFilter() {
		setupCall()
			.withServicePath(PATH + "?customerNumbers=600606&periodFrom=2019-10-09&periodTo=2019-10-09")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_getForCustomerWithOrganizationFilter() {
		setupCall()
			.withServicePath(PATH + "?customerNumbers=600606&organizationIds=5565027223")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_getForCustomerWithPaging() {
		setupCall()
			.withServicePath(PATH + "?customerNumbers=600606&page=1&limit=2&sortBy=InvoiceNumber")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test05_getForNonExistingCustomer() {
		setupCall()
			.withServicePath(PATH + "?customerNumbers=999999999")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test06_getForCustomerFilteredByStatus() {
		setupCall()
			.withServicePath(PATH + "?customerNumbers=600606&status=Betalad")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test07_getForCustomerFilteredByFacilityId() {
		setupCall()
			.withServicePath(PATH + "?customerNumbers=600606&facilityIds=735999109261218707")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test08_getForCustomerWithInvalidSortByDefaultsToPeriodFrom() {
		setupCall()
			.withServicePath(PATH + "?customerNumbers=600606&sortBy=garble")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test09_getForMultipleCustomersFilteredByFacilityId() {
		setupCall()
			.withServicePath(PATH + "?customerNumbers=600606,10335&facilityIds=735999109261218707")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test10_getForCustomerWithCommaSeparatedFacilityIds() {
		// Customer 707070 has one invoice whose FacilityId column packs two ids ("...0001,...0002") and one with an
		// unrelated id ("...0003"). Filtering by the second packed id must match only the former.
		setupCall()
			.withServicePath(PATH + "?customerNumbers=707070&facilityIds=735999109700000002")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test11_getForCustomerWithMultipleFacilityIds() {
		// Two requested facility ids, each matching a different invoice of customer 707070, must be OR:ed together so
		// both invoices are returned (ordered by the InvoiceNumber tie-breaker, since both share the same period).
		setupCall()
			.withServicePath(PATH + "?customerNumbers=707070&facilityIds=735999109700000001,735999109700000003")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test12_getForCustomerSpanningMultipleOrganizations() {
		// Invoice 767891997 belongs to organization 5564786647 while 767886195 and 767880198 belong to 5565027223, so this
		// page forces the batched detail lookup to attribute rows to invoices across organization boundaries.
		setupCall()
			.withServicePath(PATH + "?customerNumbers=10335&periodFrom=2019-10-08&periodTo=2019-10-08&sortBy=InvoiceNumber&sortDirection=DESC&limit=3")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test13_getForCustomerFilteredByInvoiceNumber() {
		// Narrowing an ordinary customer query down to a single invoice, which is how a caller holding customer number,
		// organization number and invoice number reads one invoice together with its details.
		setupCall()
			.withServicePath(PATH + "?customerNumbers=600606&organizationIds=5565027223&invoiceNumbers=137968194")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test14_getForCustomerWithNonExistingInvoiceNumber() {
		// An unknown invoice number yields an empty page rather than a 404, since this is a filter on a collection
		// resource and not a lookup of a single resource.
		setupCall()
			.withServicePath(PATH + "?customerNumbers=600606&invoiceNumbers=999999999")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test15_getForCustomerFilteredByMultipleInvoiceNumbers() {
		// Two invoice numbers in one call must both come back, which is what separates the list filter from the single
		// value it replaced.
		setupCall()
			.withServicePath(PATH + "?customerNumbers=600606&invoiceNumbers=137968194,137968293")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
