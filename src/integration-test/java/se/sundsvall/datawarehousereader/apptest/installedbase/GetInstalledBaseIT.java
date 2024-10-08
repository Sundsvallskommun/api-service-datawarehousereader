package se.sundsvall.datawarehousereader.apptest.installedbase;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;

import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

/**
 * Read installed base tests
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@WireMockAppTestSuite(files = "classpath:/GetInstalledBase/", classes = Application.class)
class GetInstalledBaseIT extends AbstractAppTest {

	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getByTypeAndFacilityId() {
		setupCall()
			.withServicePath("/2281/installedbase?type=Fjärrkyla&facilityId=735999226000059909")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getWithNoParams() {
		setupCall()
			.withServicePath("/2281/installedbase")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_getByTypeWithCustomPageAndLimit() {
		setupCall()
			.withServicePath("/2281/installedbase?page=18&limit=10&type=El")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_getByCompany() {
		setupCall()
			.withServicePath("/2281/installedbase?company=Sundsvall Energi AB")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test05_getByCustomerNumber() {
		setupCall()
			.withServicePath("/2281/installedbase?customerNumber=600606")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test06_getByCustomerNumberAndCompany() {
		setupCall()
			.withServicePath("/2281/installedbase?customerNumber=600606&company=Sundsvall Energi AB")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test07_getByCareOf() {
		setupCall()
			.withServicePath("/2281/installedbase?careOf=Statlig instutition")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test08_getByAddress() {
		setupCall()
			.withServicePath("/2281/installedbase?street=Vägen 1112&postCode=85353&city=Sundsvall")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test09_allParametersWithMatch() {
		setupCall()
			.withServicePath("""
				/2281/installedbase\
				?facilityId=735999109141107350\
				&company=Sundsvall Energi AB\
				&customerId=10335\
				&type=Elhandel\
				&careOf=Fastighetsförmedling AB\
				&street=Vägen 4\
				&postCode=85353\
				&city=Sundsvall""")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test10_allParametersNoMatch() {
		setupCall()
			.withServicePath("""
				/2281/installedbase\
				?facilityId=735999109141107350\
				&company=Sundsvall Elnät\
				&customerId=10335\
				&type=El\
				&careOf=Fastighetsförmedling AB\
				&street=Sallyhillsvägen 4\
				&postCode=85353\
				&city=Sundsvall""")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test11_getWithPageLargerThanResult() {
		setupCall()
			.withServicePath("/2281/installedbase?page=5")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test12_getCustomerInstallementsModifiedFrom() {
		setupCall()
			.withServicePath("""
				/2281/installedbase\
				?customerId=10335\
				&lastModifiedDateFrom=2017-12-07""")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
