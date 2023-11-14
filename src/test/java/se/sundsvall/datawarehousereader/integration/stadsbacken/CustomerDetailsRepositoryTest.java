package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	private final LocalDateTime now = LocalDateTime.now();
	private final Pageable pageable = PageRequest.of(0, 100, Sort.Direction.ASC, "uuid");

	@Test
	void findWithCustomerEngagementOrgIdAndPartyIds() {
		final var uuids = List.of("9f395f51-b5ed-401b-b700-ef70cbb15d79", "9f395f51-b5ed-401b-b700-ef70cbb15d80");
		final var customerEngagementOrgId = "5564786647";

		final var result = repository.findWithCustomerEngagementOrgIdAndPartyIds(now, customerEngagementOrgId, uuids, pageable);

		assertThat(result).isNotNull()
				.hasSize(2)
				.extracting(
						CustomerDetailsEntity::getPartyId,
						CustomerDetailsEntity::getCustomerId,
						CustomerDetailsEntity::getCustomerOrgId,
						CustomerDetailsEntity::getOrganizationId,
						CustomerDetailsEntity::getOrganizationName,
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
				.containsExactly(
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D79", 123454, "102000-0000", "5564786647", "Sundsvall Energi AB", 2, "Företag1", "Test Testorsson", "c/o Testorsson", "Testvägen12 c lgh 1005", "85234", "Sundsvall", "+46761234567", null, "+46761234567", null, null, false, true),
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D80", 123455, "102000-0000", "5564786647", "Sundsvall Energi AB", 2, "Företag2", "Test Testorsson", "c/o Testorsson", "Testvägen11 c lgh 1005", "85234", "Sundsvall", "+46761234567", null, "+46761234567", null, null, false, true));
	}

	@Test
	void testFindWithCustomerEngagementOrgId() {
		final var customerEngagementOrgId = "5564786647";

		final var result = repository.findWithCustomerEngagementOrgId(now, customerEngagementOrgId, pageable);

		assertThat(result).isNotNull()
				.hasSize(3)
				.extracting(
						CustomerDetailsEntity::getPartyId,
						CustomerDetailsEntity::getCustomerId,
						CustomerDetailsEntity::getCustomerOrgId,
						CustomerDetailsEntity::getOrganizationId,
						CustomerDetailsEntity::getOrganizationName,
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
				.containsExactly(
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D79", 123454, "102000-0000", "5564786647", "Sundsvall Energi AB", 2, "Företag1", "Test Testorsson", "c/o Testorsson", "Testvägen12 c lgh 1005", "85234", "Sundsvall", "+46761234567", null, "+46761234567", null, null, false, true),
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D80", 123455, "102000-0000", "5564786647", "Sundsvall Energi AB", 2, "Företag2", "Test Testorsson", "c/o Testorsson", "Testvägen11 c lgh 1005", "85234", "Sundsvall", "+46761234567", null, "+46761234567", null, null, false, true),
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D81", 123456, "102000-0000", "5564786647", "Sundsvall Energi AB", 2, "Företag", "Test Testorsson", "c/o Testorsson", "Testvägen10 c lgh 1005", "85234", "Sundsvall", "+46761234567", null, "+46761234567", null, null, false, true));
	}
}
