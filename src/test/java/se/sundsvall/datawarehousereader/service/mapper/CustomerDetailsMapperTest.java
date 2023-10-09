package se.sundsvall.datawarehousereader.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerDetailsMapper.toCustomerDetails;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetails;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailsEntity;

class CustomerDetailsMapperTest {

	@Test
	void toCustomerDetailsWithNullList() {
		assertThat(toCustomerDetails((List<CustomerDetailsEntity>) null)).isEmpty();
	}

	@Test
	void toCustomerDetailsWithEmptyList() {
		assertThat(toCustomerDetails(Collections.emptyList())).isEmpty();
	}

	@Test
	void toDetails() {
		final var entity = CustomerDetailsEntity.create()
			.withCustomerOrgId("customerOrgId")
			.withCustomerId(1)
			.withCustomerCategoryID(2)
			.withCustomerCategoryDescription("customerCategoryDescription")
			.withName("Name")
			.withCo("co")
			.withAddress("address")
			.withZipcode("zipcode")
			.withCity("city")
			.withPhone1("phone1")
			.withPhone2("phone2")
			.withPhone3("phone3")
			.withEmail1("email1")
			.withEmail2("email2")
			.withCustomerChangedFlg(true)
			.withInstalledChangedFlg(true);

		final var result = toCustomerDetails(List.of(entity));

		assertThat(result)
			.hasSize(1)
			.extracting(
				CustomerDetails::getCustomerOrgNumber,
				CustomerDetails::getPartyId,
				CustomerDetails::getCustomerNumber,
				CustomerDetails::getCustomerName,
				CustomerDetails::getStreet,
				CustomerDetails::getPostalCode,
				CustomerDetails::getCity,
				CustomerDetails::getCareOf,
				CustomerDetails::getPhoneNumbers,
				CustomerDetails::getEmails,
				CustomerDetails::getCustomerCategoryID,
				CustomerDetails::getCustomerCategoryDescription,
				CustomerDetails::isCustomerChangedFlg,
				CustomerDetails::isInstalledChangedFlg)
			.containsExactly(tuple(
				"customerOrgId",
				null,
				"1",
				"Name",
				"address",
				"zipcode",
				"city",
				"co",
				List.of("phone1", "phone2", "phone3"),
				List.of("email1", "email2"),
				2,
				"customerCategoryDescription",
				true,
				true));

	}

}
