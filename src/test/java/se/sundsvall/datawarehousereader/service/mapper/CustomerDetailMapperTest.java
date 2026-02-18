package se.sundsvall.datawarehousereader.service.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort.Direction;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetails;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.MetadataEmbeddable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerDetailMapper.toCustomerDetails;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerDetailMapper.toPagingAndSortingMetaData;

class CustomerDetailMapperTest {

	@Test
	void toCustomerDetailsToCustomerDetailsWithNullList() {
		assertThat(toCustomerDetails((List<CustomerDetailEntity>) null)).isEmpty();
	}

	@Test
	void testToCustomerDetailsWithEmptyList() {
		assertThat(toCustomerDetails(Collections.emptyList())).isEmpty();
	}

	@Test
	void testToCustomerDetails() {
		final var moveInDate = LocalDateTime.now();

		final var entity = CustomerDetailEntity.create()
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
			.withInstalledChangedFlg(true)
			.withActive(true)
			.withMoveInDate(moveInDate);

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
				CustomerDetails::isInstalledChangedFlg,
				CustomerDetails::isActive,
				CustomerDetails::getMoveInDate)
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
				true,
				true,
				moveInDate.toLocalDate()));
	}

	@Test
	void testToPagingAndSortingMetaData() {

		final var parameters = CustomerDetailsParameters.create();
		parameters.setLimit(111);
		parameters.setPage(222);
		parameters.setSortBy(List.of("sortBy1", "sortby2"));
		parameters.setSortDirection(Direction.DESC);

		final var metadata = MetadataEmbeddable.create()
			.withCount(333)
			.withTotalPages(444)
			.withTotalRecords(555);

		final var bean = toPagingAndSortingMetaData(parameters, metadata);

		assertThat(bean).hasNoNullFieldsOrProperties().satisfies(b -> {
			assertThat(b.getCount()).isEqualTo(333);
			assertThat(b.getLimit()).isEqualTo(111);
			assertThat(b.getPage()).isEqualTo(222);
			assertThat(b.getTotalPages()).isEqualTo(444);
			assertThat(b.getTotalRecords()).isEqualTo(555);
			assertThat(b.getSortBy()).containsExactly("sortBy1", "sortby2");
			assertThat(b.getSortDirection()).isEqualTo(Direction.DESC);
		});
	}

	@Test
	void testToPagingAndSortingMetaDataWithNoSortBy() {

		final var parameters = CustomerDetailsParameters.create();
		parameters.setLimit(111);
		parameters.setPage(222);
		parameters.setSortDirection(Direction.DESC);

		final var metadata = MetadataEmbeddable.create()
			.withCount(333)
			.withTotalPages(444)
			.withTotalRecords(555);

		final var bean = toPagingAndSortingMetaData(parameters, metadata);

		assertThat(bean).hasNoNullFieldsOrPropertiesExcept("sortBy", "sortDirection").satisfies(b -> {
			assertThat(b.getCount()).isEqualTo(333);
			assertThat(b.getLimit()).isEqualTo(111);
			assertThat(b.getPage()).isEqualTo(222);
			assertThat(b.getTotalPages()).isEqualTo(444);
			assertThat(b.getTotalRecords()).isEqualTo(555);
		});
	}

	@Test
	void testToSortStringFromNull() {
		assertThat(CustomerDetailMapper.toSortString(null, Direction.ASC)).isEmpty();
	}

	@Test
	void testToSortStringFromEmptyList() {
		assertThat(CustomerDetailMapper.toSortString(Collections.emptyList(), Direction.ASC)).isEmpty();
	}

	@Test
	void testToAscendingSortString() {
		assertThat(CustomerDetailMapper.toSortString(List.of("a", "b"), Direction.ASC)).isEqualTo("a, b");
	}

	@Test
	void testToDescendingSortString() {
		assertThat(CustomerDetailMapper.toSortString(List.of("a", "b"), Direction.DESC)).isEqualTo("a#, b#");
	}
}
