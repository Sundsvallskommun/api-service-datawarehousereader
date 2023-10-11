package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailsEntity;


@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
@Transactional
class CustomerDetailsRepositoryTest {

	@Autowired
	private CustomerDetailsRepository repository;

	@Test
	void testResponseWithHits() {
		final var dateTimeFrom = LocalDateTime.of(2022, 4, 27, 23, 59);

		final var list = repository.findAllMatching(dateTimeFrom, 0, 100);

		assertThat(list).isNotNull()
			.hasSize(9)
			.extracting(
				CustomerDetailsEntity::getCustomerId,
				CustomerDetailsEntity::getCustomerOrgId,
				CustomerDetailsEntity::getCustomerCategoryID,
				CustomerDetailsEntity::getCustomerCategoryDescription,
				CustomerDetailsEntity::getName,
				CustomerDetailsEntity::getCo,
				CustomerDetailsEntity::getAddress,
				CustomerDetailsEntity::getZipcode,
				CustomerDetailsEntity::getCity,
				CustomerDetailsEntity::getPhone1,
				CustomerDetailsEntity::getPhone2,
				CustomerDetailsEntity::getPhone3,
				CustomerDetailsEntity::getEmail1,
				CustomerDetailsEntity::getEmail2,
				CustomerDetailsEntity::isCustomerChangedFlg,
				CustomerDetailsEntity::isInstalledChangedFlg)
			.containsExactlyInAnyOrder(
				tuple(123456, "102000-0000", 2, "Företag", "Test Testorsson", "c/o Testorsson", "Testvägen10 c lgh 1005", "85234", "Sundsvall", "+46761234567", null, "+46761234567", null, null, false, true),
				tuple(123457, "20000101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen21", "85234", "Sundsvall", "+46761234567", "+46761234567", null, "test@sundsvall.com", "test2@sundsvall.com", false, true),
				tuple(123458, "20010101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen2", "85234", "Sundsvall", "+46761234567", null, null, "test@sundsvall.com", "test2@sundsvall.com", true, false),
				tuple(123459, "20020101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen21 E", "85234", "Sundsvall", "+46761234567", null, null, null, "test2@sundsvall.com", false, true),
				tuple(123450, "20030101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen65 A", "85234", "Sundsvall", "+46761234567", null, null, null, "test2@sundsvall.com", true, true),
				tuple(123410, "20040101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen60", "85234", "Sundsvall", "+46761234567", null, null, null, null, false, true),
				tuple(123411, "20050101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen12", "85234", "SUNDSVALL", "+46761234567", null, null, "test@sundsvall.com", null, false, true),
				tuple(123412, "20060101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen2 Lgh 6", "85234", "Sundsvall", null, "+46761234567", null, null, null, true, true),
				tuple(123413, "20070101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen106", "85234", "Sundsvall", null, "+46761234567", null, null, "test2@sundsvall.com", false, true));
	}

	@Test
	void testResponseWithPaging() {
		final var dateTimeFrom = LocalDateTime.of(2022, 4, 27, 23, 59);

		final var list = repository.findAllMatching(dateTimeFrom, 0, 4);

		assertThat(list).isNotNull()
				.hasSize(4)
				.extracting(
						CustomerDetailsEntity::getCustomerId,
						CustomerDetailsEntity::getCustomerOrgId,
						CustomerDetailsEntity::getCustomerCategoryID,
						CustomerDetailsEntity::getCustomerCategoryDescription,
						CustomerDetailsEntity::getName,
						CustomerDetailsEntity::getCo,
						CustomerDetailsEntity::getAddress,
						CustomerDetailsEntity::getZipcode,
						CustomerDetailsEntity::getCity,
						CustomerDetailsEntity::getPhone1,
						CustomerDetailsEntity::getPhone2,
						CustomerDetailsEntity::getPhone3,
						CustomerDetailsEntity::getEmail1,
						CustomerDetailsEntity::getEmail2,
						CustomerDetailsEntity::isCustomerChangedFlg,
						CustomerDetailsEntity::isInstalledChangedFlg)
				.containsExactlyInAnyOrder(
						tuple(123410, "20040101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen60", "85234", "Sundsvall", "+46761234567", null, null, null, null, false, true),
						tuple(123411, "20050101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen12", "85234", "SUNDSVALL", "+46761234567", null, null, "test@sundsvall.com", null, false, true),
						tuple(123412, "20060101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen2 Lgh 6", "85234", "Sundsvall", null, "+46761234567", null, null, null, true, true),
						tuple(123413, "20070101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen106", "85234", "Sundsvall", null, "+46761234567", null, null, "test2@sundsvall.com", false, true));
	}

	@Test
	void testResponseWithNoFiltering() {
		final var list = repository.findAllMatching(null, 0, 100);

		assertThat(list).isNotNull().hasSize(9);
	}

}