package se.sundsvall.datawarehousereader.service.mapper;

import java.util.List;
import java.util.Optional;
import se.sundsvall.datawarehousereader.api.model.installation.InstallationDetails;
import se.sundsvall.datawarehousereader.api.model.installation.InstallationMetaData;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationMetaDataEmbeddable;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

public final class InstallationMapper {

	private InstallationMapper() {}

	public static List<InstallationDetails> toInstallationDetailsList(final List<InstallationEntity> entities) {
		return ofNullable(entities).orElse(emptyList()).stream()
			.map(InstallationMapper::toInstallationDetails)
			.toList();
	}

	public static InstallationDetails toInstallationDetails(final InstallationEntity entity) {
		return Optional.ofNullable(entity)
			.map(detail -> InstallationDetails.create()
				.withCareOf(entity.getCareOf())
				.withCity(entity.getCity())
				.withCompany(entity.getCompany())
				.withDateFrom(entity.getDateFrom())
				.withDateTo(entity.getDateTo())
				.withFacilityId(entity.getFacilityId())
				.withDateLastModified(entity.getLastChangedDate())
				.withPlacementId(Optional.ofNullable(entity.getInternalId()).orElse(0))
				.withPostCode(entity.getPostCode())
				.withPropertyDesignation(entity.getHouseName())
				.withStreet(entity.getStreet())
				.withMetaData(toMetaDatas(entity.getMetaData()))
				.withType(entity.getType()))
			.orElse(null);
	}

	private static List<InstallationMetaData> toMetaDatas(final List<InstallationMetaDataEmbeddable> embeddables) {
		return ofNullable(embeddables).orElse(emptyList()).stream()
			.map(InstallationMapper::toMetaData)
			.toList();
	}

	private static InstallationMetaData toMetaData(final InstallationMetaDataEmbeddable embeddable) {
		return InstallationMetaData.create()
			.withDisplayName(embeddable.getDisplayName())
			.withKey(embeddable.getKey())
			.withType(embeddable.getType())
			.withValue(embeddable.getValue());
	}
}
