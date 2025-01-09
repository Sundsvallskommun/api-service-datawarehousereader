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
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
@Transactional
class CustomerDetailRepositoryTest {

	@Autowired
	private CustomerDetailRepository repository;

	private final LocalDateTime now = LocalDateTime.now();

	@Test
	void findWithCustomerEngagementOrgIdAndPartyIds() {
		final var uuids = "9f395f51-b5ed-401b-b700-ef70cbb15d79,9f395f51-b5ed-401b-b700-ef70cbb15d80";
		final var customerEngagementOrgId = "5564786647";

		final var result = repository.findWithCustomerEngagementOrgIdAndPartyIds(now, customerEngagementOrgId, uuids, 0, 100, "uuid");

		assertThat(result).isNotNull()
			.hasSize(2)
			.extracting(
				CustomerDetailEntity::getPartyId,
				CustomerDetailEntity::getCustomerId,
				CustomerDetailEntity::getCustomerOrgId,
				CustomerDetailEntity::getOrganizationId,
				CustomerDetailEntity::getOrganizationName,
				CustomerDetailEntity::getCustomerCategoryID,
				CustomerDetailEntity::getCustomerCategoryDescription,
				CustomerDetailEntity::getName,
				CustomerDetailEntity::getCo,
				CustomerDetailEntity::getAddress,
				CustomerDetailEntity::getZipcode,
				CustomerDetailEntity::getCity,
				CustomerDetailEntity::getPhone1,
				CustomerDetailEntity::getPhone2,
				CustomerDetailEntity::getPhone3,
				CustomerDetailEntity::getEmail1,
				CustomerDetailEntity::getEmail2,
				CustomerDetailEntity::isCustomerChangedFlg,
				CustomerDetailEntity::isInstalledChangedFlg,
				CustomerDetailEntity::isActive,
				CustomerDetailEntity::getMoveInDate)
			.containsExactlyInAnyOrder(
				tuple("9F395F51-B5ED-401B-B700-EF70CBB15D79", 123454, "102000-0000", "5564786647", "Sundsvall Energi AB", 2, "Företag1", "Test Testorsson", "c/o Testorsson", "Testvägen12 c lgh 1005", "85234", "Sundsvall", "+46761234567", null,
					"+46761234567", null, null, false, true, true, null),
				tuple("9F395F51-B5ED-401B-B700-EF70CBB15D80", 123455, "102000-0000", "5564786647", "Sundsvall Energi AB", 2, "Företag2", "Test Testorsson", "c/o Testorsson", "Testvägen11 c lgh 1005", "85234", "Sundsvall", "+46761234567", null,
					"+46761234567", null, null, false, true, true, null));
	}

	@Test
	void testFindWithCustomerEngagementOrgId() {
		final var customerEngagementOrgId = "5564786647";

		final var result = repository.findWithCustomerEngagementOrgId(now, customerEngagementOrgId, 0, 100, "uuid");

		assertThat(result).isNotNull()
			.hasSize(3)
			.extracting(
				CustomerDetailEntity::getPartyId,
				CustomerDetailEntity::getCustomerId,
				CustomerDetailEntity::getCustomerOrgId,
				CustomerDetailEntity::getOrganizationId,
				CustomerDetailEntity::getOrganizationName,
				CustomerDetailEntity::getCustomerCategoryID,
				CustomerDetailEntity::getCustomerCategoryDescription,
				CustomerDetailEntity::getName,
				CustomerDetailEntity::getCo,
				CustomerDetailEntity::getAddress,
				CustomerDetailEntity::getZipcode,
				CustomerDetailEntity::getCity,
				CustomerDetailEntity::getPhone1,
				CustomerDetailEntity::getPhone2,
				CustomerDetailEntity::getPhone3,
				CustomerDetailEntity::getEmail1,
				CustomerDetailEntity::getEmail2,
				CustomerDetailEntity::isCustomerChangedFlg,
				CustomerDetailEntity::isInstalledChangedFlg,
				CustomerDetailEntity::isActive,
				CustomerDetailEntity::getMoveInDate)
			.containsExactlyInAnyOrder(
				tuple("9F395F51-B5ED-401B-B700-EF70CBB15D79", 123454, "102000-0000", "5564786647", "Sundsvall Energi AB", 2, "Företag1", "Test Testorsson", "c/o Testorsson", "Testvägen12 c lgh 1005", "85234", "Sundsvall", "+46761234567", null,
					"+46761234567", null, null, false, true, true, null),
				tuple("9F395F51-B5ED-401B-B700-EF70CBB15D80", 123455, "102000-0000", "5564786647", "Sundsvall Energi AB", 2, "Företag2", "Test Testorsson", "c/o Testorsson", "Testvägen11 c lgh 1005", "85234", "Sundsvall", "+46761234567", null,
					"+46761234567", null, null, false, true, true, null),
				tuple("9F395F51-B5ED-401B-B700-EF70CBB15D81", 123456, "102000-0000", "5564786647", "Sundsvall Energi AB", 2, "Företag", "Test Testorsson", "c/o Testorsson", "Testvägen10 c lgh 1005", "85234", "Sundsvall", "+46761234567", null,
					"+46761234567", null, null, false, true, true, null));
	}

	@Test
	void testFindNonActive() {
		final var customerEngagementOrgId = "5565027225";

		final var result = repository.findWithCustomerEngagementOrgId(now, customerEngagementOrgId, 0, 100, "uuid");

		assertThat(result).isNotNull()
			.hasSize(2)
			.extracting(
				CustomerDetailEntity::getPartyId,
				CustomerDetailEntity::getCustomerId,
				CustomerDetailEntity::getCustomerOrgId,
				CustomerDetailEntity::getOrganizationId,
				CustomerDetailEntity::getOrganizationName,
				CustomerDetailEntity::getCustomerCategoryID,
				CustomerDetailEntity::getCustomerCategoryDescription,
				CustomerDetailEntity::getName,
				CustomerDetailEntity::getCo,
				CustomerDetailEntity::getAddress,
				CustomerDetailEntity::getZipcode,
				CustomerDetailEntity::getCity,
				CustomerDetailEntity::getPhone1,
				CustomerDetailEntity::getPhone2,
				CustomerDetailEntity::getPhone3,
				CustomerDetailEntity::getEmail1,
				CustomerDetailEntity::getEmail2,
				CustomerDetailEntity::isCustomerChangedFlg,
				CustomerDetailEntity::isInstalledChangedFlg,
				CustomerDetailEntity::isActive,
				CustomerDetailEntity::getMoveInDate)
			.containsExactlyInAnyOrder(
				tuple("9F395F51-B5ED-401B-B700-EF70CBB15D90", 123414, "20070101-1234", "5565027225", "Some other Elnät", 1, "Privatperson", "Test Testorsson3", null, "Testvägen106", "85234", "Sundsvall", null, "+46761234567",
					null, null, "test3@sundsvall.com", false, true, false, LocalDateTime.of(2095, 1, 1, 0, 0)),
				tuple(null, 123415, "20070101-1234", "5565027225", "Some other Elnät", 1, "Privatperson", "Test Testorsson4", null, "Testvägen106", "85234", "Sundsvall", null, "+46761234567",
					null, null, "test4@sundsvall.com", false, true, false, LocalDateTime.of(2095, 1, 1, 0, 0)));
	}
}
