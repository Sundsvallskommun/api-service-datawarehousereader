package se.sundsvall.datawarehousereader.apptest.measurment;

import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static se.sundsvall.datawarehousereader.api.model.Category.DISTRICT_HEATING;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.DAY;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.HOUR;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.MONTH;

import org.junit.jupiter.api.Test;

import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

/**
 * Read district-heating-measurements tests
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@WireMockAppTestSuite(files = "classpath:/GetMeasurementsDistrictHeating/", classes = Application.class)
class GetMeasurementsDistrictHeatingIT extends AbstractAppTest {

	private static final String PATH = "/2281/measurements/%s/%s";
	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getDistrictHeatingMonth() {
		setupCall()
			.withServicePath(format(PATH, DISTRICT_HEATING, MONTH) +
				"?page=1" +
				"&limit=100" +
				"&partyId=B1EDEA3C-1083-4E1A-81FB-7D95E505E102" +
				"&facilityId=735999109113202014" +
				"&fromDateTime=2018-01-01T14:39:22.817Z" +
				"&toDateTime=2018-12-31T14:39:22.817Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getDistrictHeatingMonthWithCustomPageAndLimit() {
		setupCall()
			.withServicePath(format(PATH, DISTRICT_HEATING, MONTH) +
				"?page=2" +
				"&limit=10" +
				"&partyId=B1EDEA3C-1083-4E1A-81FB-7D95E505E102" +
				"&facilityId=735999109113202014" +
				"&fromDateTime=2018-01-01T14:39:22.817Z" +
				"&toDateTime=2018-12-31T14:39:22.817Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_getDistrictHeatingHour() {
		setupCall()
			.withServicePath(format(PATH, DISTRICT_HEATING, HOUR) +
				"?page=1" +
				"&limit=100" +
				"&partyId=A0B52C5B-93AC-480B-821D-E238C8F4D95" +
				"&facilityId=9115803075" +
				"&fromDateTime=2022-01-01T00:00:00.817Z" +
				"&toDateTime=2022-01-01T23:59:59.817Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_getDistrictHeatingDay() {
		setupCall()
			.withServicePath(format(PATH, DISTRICT_HEATING, DAY) +
				"?page=1" +
				"&limit=100" +
				"&partyId=A0B52C5B-93AC-480B-821D-E238C8F4D95" +
				"&facilityId=9261219043" +
				"&fromDateTime=2022-03-23T00:00:00.000Z" +
				"&toDateTime=2022-03-25T23:59:59.817Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
