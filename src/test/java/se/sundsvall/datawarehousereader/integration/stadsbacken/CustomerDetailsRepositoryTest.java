package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
class CustomerDetailsRepositoryTest {

	@Autowired
	private CustomerDetailsRepository repository;


	private final LocalDateTime now = LocalDateTime.now();
	private final Pageable pageable = PageRequest.of(0, 100, Sort.Direction.ASC, "uuid");

	@Test
	void findWithCustomerEngagementOrgIdAndPartyIds() {
		final List<String> uuids = List.of("9f395f51-b5ed-401b-b700-ef70cbb15d89", "9f395f51-b5ed-401b-b700-ef70cbb15d90");
		final String customerEngagementOrgId = "20070101-1234";

		var result = repository.findWithCustomerEngagementOrgIdAndPartyIds(now, customerEngagementOrgId, uuids, pageable);

		assertThat(result).isNotNull()
				.hasSize(2)
				.extracting(
						CustomerDetailsEntity::getUuid,
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
				.containsExactly(
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D89", 123413, "20070101-1234", 1, "Privatperson", "Test Testorsson2", null, "Testvägen106", "85234", "Sundsvall", null, "+46761234567", null, null, "test2@sundsvall.com", false, true),
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D90", 123414, "20070101-1234", 1, "Privatperson", "Test Testorsson3", null, "Testvägen106", "85234", "Sundsvall", null, "+46761234567", null, null, "test3@sundsvall.com", false, true));
	}

	@Test
	void testFindWithCustomerEngagementOrgId() {
		final String customerEngagementOrgId = "20070101-1234";

		var result = repository.findWithCustomerEngagementOrgId(now, customerEngagementOrgId, pageable);

		assertThat(result).isNotNull()
				.hasSize(3)
				.extracting(
						CustomerDetailsEntity::getUuid,
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
				.containsExactly(
						tuple(null, 123415, "20070101-1234", 1, "Privatperson", "Test Testorsson4", null, "Testvägen106", "85234", "Sundsvall", null, "+46761234567", null, null, "test4@sundsvall.com", false, true),
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D89", 123413, "20070101-1234", 1, "Privatperson", "Test Testorsson2", null, "Testvägen106", "85234", "Sundsvall", null, "+46761234567", null, null, "test2@sundsvall.com", false, true),
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D90", 123414, "20070101-1234", 1, "Privatperson", "Test Testorsson3", null, "Testvägen106", "85234", "Sundsvall", null, "+46761234567", null, null, "test3@sundsvall.com", false, true));
	}

	@Test
	void testFindWithPartyIds() {
		final List<String> uuids = List.of("9f395f51-b5ed-401b-b700-ef70cbb15d88", "9f395f51-b5ed-401b-b700-ef70cbb15d89", "9f395f51-b5ed-401b-b700-ef70cbb15d90");

		var result = repository.findWithPartyIds(now, uuids, pageable);

		assertThat(result).isNotNull()
				.hasSize(3)
				.extracting(
						CustomerDetailsEntity::getUuid,
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
				.containsExactly(
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D88", 123412, "20060101-1234", 1, "Privatperson", "Test Testorsson", null, "Testvägen2 Lgh 6", "85234", "Sundsvall", null, "+46761234567", null, null, null, true, true),
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D89", 123413, "20070101-1234", 1, "Privatperson", "Test Testorsson2", null, "Testvägen106", "85234", "Sundsvall", null, "+46761234567", null, null, "test2@sundsvall.com", false, true),
						tuple("9F395F51-B5ED-401B-B700-EF70CBB15D90", 123414, "20070101-1234", 1, "Privatperson", "Test Testorsson3", null, "Testvägen106", "85234", "Sundsvall", null, "+46761234567", null, null, "test3@sundsvall.com", false, true));
	}
}