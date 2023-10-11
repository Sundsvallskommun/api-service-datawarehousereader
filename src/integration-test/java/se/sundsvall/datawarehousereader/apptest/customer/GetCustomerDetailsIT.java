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
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@WireMockAppTestSuite(files = "classpath:/GetCustomerDetails/", classes = Application.class)
class GetCustomerDetailsIT extends AbstractAppTest {

	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getByPartyId() {
		setupCall()
			.withServicePath("/customer/details?partyId=70933c1c-2094-44e6-9abe-57b8a3c0ecd9&partyId=318b7ec8-aab8-41e2-82f5-796bdd5cebf2&partyId=10e69eb7-3cb4-442f-8cdb-2e998080dbb1")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getByCustomerEngagementOrgId() {
		setupCall()
			.withServicePath("/customer/details?customerEngagementOrgId=1020000000")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_getWithPaging() {
		setupCall()
			.withServicePath("/customer/details??page=1&limit=5&fromDateTime=2021-10-12T14:11:16.359Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
