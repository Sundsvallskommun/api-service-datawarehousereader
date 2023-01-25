package se.sundsvall.datawarehousereader.apptest.measurment;

import org.junit.jupiter.api.Test;
import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;

import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static se.sundsvall.datawarehousereader.api.model.Category.ELECTRICITY;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.DAY;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.HOUR;
import static se.sundsvall.datawarehousereader.api.model.measurement.Aggregation.MONTH;

/**
 * Read electricity-measurements tests
 *
 * @see src/test/resources/db/scripts/testdata.sql for data setup.
 */
@WireMockAppTestSuite(files = "classpath:/GetMeasurementsElectricity/", classes = Application.class)
class GetMeasurementsElectricityIT extends AbstractAppTest {

	private static final String PATH = "/measurements/%s/%s";
	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getElectricityDay() {
		setupCall()
			.withServicePath(format(PATH, ELECTRICITY, DAY) +
				"/?page=1" +
				"&limit=100" +
				"&partyId=16A64870-DF4D-4A27-A514-56297AB6F8D9" +
				"&facilityId=735999109170208042" +
				"&fromDateTime=2022-04-01T14:39:22.817Z" +
				"&toDateTime=2022-04-10T14:39:22.817Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getElectricityDayWithCustomPageAndLimit() {
		setupCall()
			.withServicePath(format(PATH, ELECTRICITY, DAY) +
				"/?page=2" +
				"&limit=2" +
				"&partyId=16A64870-DF4D-4A27-A514-56297AB6F8D9" +
				"&facilityId=735999109170208042" +
				"&fromDateTime=2022-04-01T14:39:22.817Z" +
				"&toDateTime=2022-04-10T14:39:22.817Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_getElectricityMonth() {
		setupCall()
			.withServicePath(format(PATH, ELECTRICITY, MONTH) +
				"/?page=1" +
				"&limit=100" +
				"&partyId=B1EDEA3C-1083-4E1A-81FB-7D95E505E102" +
				"&facilityId=9151530012" +
				"&fromDateTime=2019-01-01T14:39:22.817Z" +
				"&toDateTime=2019-06-02T14:39:22.817Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_getElectricityHour() {
		setupCall()
			.withServicePath(format(PATH, ELECTRICITY, HOUR) +
				"/?page=1" +
				"&limit=100" +
				"&partyId=B1EDEA3C-1083-4E1A-81FB-7D95E505E102" +
				"&facilityId=735999109320425015" +
				"&fromDateTime=2022-04-11T00:00:00.000Z" +
				"&toDateTime=2022-04-11T23:59:59.999Z")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
