package se.sundsvall.datawarehousereader.service.mapper;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static se.sundsvall.datawarehousereader.service.mapper.InstallationMapper.toInstallationDetails;
import static se.sundsvall.datawarehousereader.service.mapper.InstallationMapper.toInstallationDetailsList;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import se.sundsvall.datawarehousereader.api.model.installation.InstallationMetaData;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationMetaDataEmbeddable;

class InstallationMapperTest {

	final InstallationEntity installationEntity = InstallationEntity.create()
		.withCareOf("careOf")
		.withCity("city")
		.withCompany("company")
		.withDateFrom(LocalDate.now().minusWeeks(1))
		.withDateTo(LocalDate.now().plusWeeks(1))
		.withFacilityId("facilityId")
		.withHouseName("houseName")
		.withInternalId(456)
		.withLastChangedDate(LocalDate.now())
		.withPostCode("postCode")
		.withStreet("street")
		.withType("type")
		.withMetaData(List.of(InstallationMetaDataEmbeddable.create().withValue("value").withKey("key").withDisplayName("displayName").withCompany("company").withType("type")));


	@Test
	void toInstallationDetailsWithNull() {
		assertThat(toInstallationDetails(null)).isNull();
	}

	@Test
	void toInstallationDetailsListWithEmptyList() {
		assertThat(toInstallationDetailsList(emptyList())).isEmpty();
	}

	@Test
	void toInstallationDetailsListTest() {

		final var list = toInstallationDetailsList(List.of(installationEntity, installationEntity));

		assertThat(list).isNotEmpty().hasSize(2);
	}

	@Test
	void toInstallationDetailsTest() {
		final var details = toInstallationDetails(installationEntity);

		assertThat(details).satisfies(installationDetails -> {
			assertThat(installationDetails.getCareOf()).isEqualTo("careOf");
			assertThat(installationDetails.getCity()).isEqualTo("city");
			assertThat(installationDetails.getCompany()).isEqualTo("company");
			assertThat(installationDetails.getDateFrom()).isEqualTo(LocalDate.now().minusWeeks(1));
			assertThat(installationDetails.getDateTo()).isEqualTo(LocalDate.now().plusWeeks(1));
			assertThat(installationDetails.getFacilityId()).isEqualTo("facilityId");
			assertThat(installationDetails.getPlacementId()).isEqualTo(456);
			assertThat(installationDetails.getPostCode()).isEqualTo("postCode");
			assertThat(installationDetails.getPropertyDesignation()).isEqualTo("houseName");
			assertThat(installationDetails.getStreet()).isEqualTo("street");
			assertThat(installationDetails.getType()).isEqualTo("type");
		});
		assertThat(details.getMetaData()).hasSize(1)
			.extracting(
				InstallationMetaData::getDisplayName,
				InstallationMetaData::getKey,
				InstallationMetaData::getValue,
				InstallationMetaData::getType)
			.containsExactlyInAnyOrder(
				tuple("displayName", "key", "value", "type"));
	}


}
