package se.sundsvall.datawarehousereader.apptest.customer;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;

import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

/**
 * Get customer details tests
 *
 * @see src/test/resources/db/scripts/initialize.sql for data setup. Uses the function "kundinfo.fnCustomerDetail".
 */
@WireMockAppTestSuite(files = "classpath:/GetCustomerDetails/", classes = Application.class)
class GetCustomerDetailsIT extends AbstractAppTest {

	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getWithOrgNoAndPartyIds() {
		setupCall()
			.withServicePath("/2281/customer/details?partyId=9f395f51-b5ed-401b-b700-ef70cbb15d80&partyId=9f395f51-b5ed-401b-b700-ef70cbb15d81&customerEngagementOrgId=5564786647&fromDateTime=2021-10-12T14:11:16.359Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getByOnlyCustomerEngagementOrgId() {
		setupCall()
			.withServicePath("/2281/customer/details?customerEngagementOrgId=5564786647&fromDateTime=2021-10-12T14:11:16.359Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_getNonActiveCustomer() {
		setupCall()
			.withServicePath("/2281/customer/details?partyId=9f395f51-b5ed-401b-b700-ef70cbb15d90&customerEngagementOrgId=5565027225&fromDateTime=2021-10-12T14:11:16.359Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
