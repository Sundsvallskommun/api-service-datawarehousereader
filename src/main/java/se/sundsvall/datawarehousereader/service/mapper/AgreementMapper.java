package se.sundsvall.datawarehousereader.service.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.agreement.Agreement;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity;
import se.sundsvall.datawarehousereader.service.util.ServiceUtil;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static se.sundsvall.datawarehousereader.service.util.ServiceUtil.toLocalDate;

public class AgreementMapper {

	private AgreementMapper() {}

	public static List<Agreement> toAgreements(List<AgreementEntity> entities) {
		return ofNullable(entities).orElse(emptyList()).stream()
			.map(AgreementMapper::toAgreement)
			.toList();
	}

	private static Agreement toAgreement(AgreementEntity entity) {
		return Agreement.create()
			.withCustomerNumber(ServiceUtil.toString(entity.getCustomerId()))
			.withBillingId(ServiceUtil.toString(entity.getBillingId()))
			.withAgreementId(ServiceUtil.toString(entity.getAgreementId()))
			.withPartyId(entity.getUuid())
			.withDescription(entity.getDescription())
			.withCategory(getCategory(entity.getCategory()))
			.withBinding(Boolean.valueOf(entity.getBinding()))
			.withBindingRule(entity.getBindingRule())
			.withMainAgreement(Boolean.valueOf(entity.getMainAgreement()))
			.withFacilityId(entity.getFacilityId())
			.withPlacementStatus(entity.getPlacementStatus())
			.withProduction(ServiceUtil.toBoolean(entity.getIsProduction()))
			.withNetAreaId(entity.getNetAreaId())
			.withSiteAddress(ofNullable(entity.getSiteAddress()).map(String::trim).orElse(null))
			.withFromDate(toLocalDate(entity.getFromDate()))
			.withToDate(toLocalDate(entity.getToDate()))
			.withActive(isActive(entity));
	}

	private static Boolean isActive(AgreementEntity entity) {
		final var startDateHasOccurred = nonNull(entity.getFromDate()) && (entity.getFromDate().isBefore(LocalDate.now().atTime(LocalTime.MAX)) || entity.getFromDate().isEqual(LocalDate.now().atTime(LocalTime.MAX)));
		final var endDateHasNotOccurred = ofNullable(entity.getToDate()).orElse(LocalDateTime.now()).isAfter(LocalDate.now().atStartOfDay()) || ofNullable(entity.getToDate()).orElse(LocalDateTime.now()).isEqual(LocalDate.now().atStartOfDay());
		return startDateHasOccurred && endDateHasNotOccurred;
	}

	private static Category getCategory(String category) {
		return ofNullable(category)
			.map(Category::forStadsbackenValue)
			.orElse(null);
	}
}
