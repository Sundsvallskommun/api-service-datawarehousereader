package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

public interface AgreementSpecification {

	SpecificationBuilder<AgreementEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<AgreementEntity> withAgreementId(Integer agreementId) {
		return BUILDER.buildEqualFilter("agreementId", agreementId);
	}

	static Specification<AgreementEntity> withBillingId(Integer billingId) {
		return BUILDER.buildEqualFilter("billingId", billingId);
	}

	static Specification<AgreementEntity> withCustomerOrgId(String customerOrgId) {
		return BUILDER.buildEqualFilter("customerOrgId", customerOrgId);
	}

	static Specification<AgreementEntity> withCustomerId(Integer customerId) {
		return BUILDER.buildEqualFilter("customerId", customerId);
	}

	static Specification<AgreementEntity> withFacilityId(String facilityId) {
		return BUILDER.buildEqualFilter("facilityId", facilityId);
	}

	static Specification<AgreementEntity> withCategories(List<Category> categories) {
		final var stadsbackenCategories = ofNullable(categories).orElse(emptyList()).stream()
											  .map(Category::toStadsbackenValue)
											  .toList();
		return BUILDER.buildInFilterForString("category", stadsbackenCategories);
	}

	static Specification<AgreementEntity> withDescription(String description) {
		return BUILDER.buildEqualFilter("description", description);
	}

	static Specification<AgreementEntity> withMainAgreement(String mainAgreement) {
		return BUILDER.buildEqualFilter("mainAgreement", mainAgreement);
	}

	static Specification<AgreementEntity> withBinding(String binding) {
		return BUILDER.buildEqualFilter("binding", binding);
	}

	static Specification<AgreementEntity> withBindingRule(String bindingRule) {
		return BUILDER.buildEqualFilter("bindingRule", bindingRule);
	}

	static Specification<AgreementEntity> withFromDate(LocalDate date) {
		return Objects.nonNull(date) ? BUILDER.buildDateFilter("fromDate", date.atStartOfDay(), null) : (root, query, criteriaBuilder) -> criteriaBuilder.and();
	}

	static Specification<AgreementEntity> withToDate(LocalDate date) {
		return Objects.nonNull(date) ? BUILDER.buildDateFilter("toDate", null, date.atTime(LocalTime.MAX)) : (root, query, criteriaBuilder) -> criteriaBuilder.and();
	}


}
