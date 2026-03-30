package se.sundsvall.datawarehousereader.apptest.installedbase;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;

import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

/**
 * Integration tests for GET /{municipalityId}/installedbase/{partyId}
 *
 * @see src/test/resources/db/scripts/functions.sql for fnInstalledBaseWithPagingAndSort.
 */
@WireMockAppTestSuite(files = "classpath:/GetInstalledBaseByPartyId/", classes = Application.class)
class GetInstalledBaseByPartyIdIT extends AbstractAppTest {

	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getByPartyId() {
		setupCall()
			.withServicePath("/2281/installedbase/898b3634-a2f9-483c-8808-37f3f25cf24e")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getByPartyIdNoMatch() {
		setupCall()
			.withServicePath("/2281/installedbase/00000000-0000-0000-0000-000000000000")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
