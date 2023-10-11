package se.sundsvall.datawarehousereader.apptest.customer;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

/**
 * Get customer details tests
 * 
 * @see src/test/resources/db/scripts/initialize.sql for data setup, data is loaded into the function (kundinfo.fnCustomerDetails) and not the table vCustomerDetail.
 */
@WireMockAppTestSuite(files = "classpath:/GetCustomerDetails/", classes = Application.class)
class GetCustomerDetailsIT extends AbstractAppTest {

	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getByPartyId() {
		setupCall()
			.withServicePath("/customer/details?partyId=9f395f51-b5ed-401b-b700-ef70cbb15d81&partyId=9f395f51-b5ed-401b-b700-ef70cbb15d82&partyId=9f395f51-b5ed-401b-b700-ef70cbb15d83")
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
			.withServicePath("/customer/details??page=1&limit=2&fromDateTime=2021-10-12T14:11:16.359Z&customerEngagementOrgId=1020000000")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_getByPartyIdsAndCustomerEngagementOrgId() {
		setupCall()
				.withServicePath("/customer/details?partyId=9f395f51-b5ed-401b-b700-ef70cbb15d81&customerEngagementOrgId=1020000000")
				.withHttpMethod(GET)
				.withExpectedResponseStatus(OK)
				.withExpectedResponse(RESPONSE_FILE)
				.sendRequestAndVerifyResponse();
	}
}
