package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.springframework.data.jpa.domain.Specification.not;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.AGREEMENT_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.BILLING_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.BINDING;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.BINDING_RULE;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.CATEGORY;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.CUSTOMER_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.CUSTOMER_ORG_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.DESCRIPTION;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.FACILITY_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.FROM_DATE;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.MAIN_AGREEMENT;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity_.TO_DATE;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity;

public interface AgreementSpecification {

	SpecificationBuilder<AgreementEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<AgreementEntity> withAgreementId(Integer agreementId) {
		return BUILDER.buildEqualFilter(AGREEMENT_ID, agreementId);
	}

	static Specification<AgreementEntity> withBillingId(Integer billingId) {
		return BUILDER.buildEqualFilter(BILLING_ID, billingId);
	}

	static Specification<AgreementEntity> withCustomerOrgId(String customerOrgId) {
		return BUILDER.buildEqualFilter(CUSTOMER_ORG_ID, customerOrgId);
	}

	static Specification<AgreementEntity> withCustomerId(Integer customerId) {
		return BUILDER.buildEqualFilter(CUSTOMER_ID, customerId);
	}

	static Specification<AgreementEntity> withFacilityId(String facilityId) {
		return BUILDER.buildEqualFilter(FACILITY_ID, facilityId);
	}

	static Specification<AgreementEntity> withCategories(List<Category> categories) {
		final var stadsbackenCategories = ofNullable(categories).orElse(emptyList()).stream()
			.map(Category::toStadsbackenValue)
			.toList();
		return BUILDER.buildInFilterForString(CATEGORY, stadsbackenCategories);
	}

	static Specification<AgreementEntity> withDescription(String description) {
		return BUILDER.buildEqualFilter(DESCRIPTION, description);
	}

	static Specification<AgreementEntity> withMainAgreement(String mainAgreement) {
		return BUILDER.buildEqualFilter(MAIN_AGREEMENT, mainAgreement);
	}

	static Specification<AgreementEntity> withBinding(String binding) {
		return BUILDER.buildEqualFilter(BINDING, binding);
	}

	static Specification<AgreementEntity> withBindingRule(String bindingRule) {
		return BUILDER.buildEqualFilter(BINDING_RULE, bindingRule);
	}

	static Specification<AgreementEntity> withFromDate(LocalDate date) {
		return nonNull(date) ? BUILDER.buildDateFilter(FROM_DATE, date.atStartOfDay(), null) : (root, query, criteriaBuilder) -> criteriaBuilder.and();
	}

	static Specification<AgreementEntity> withToDate(LocalDate date) {
		return nonNull(date) ? BUILDER.buildDateFilter(TO_DATE, null, date.atTime(LocalTime.MAX)) : (root, query, criteriaBuilder) -> criteriaBuilder.and();
	}

	static Specification<AgreementEntity> withActive(Boolean active) {
		if (isNull(active)) {
			return (root, query, criteriaBuilder) -> criteriaBuilder.and();
		}

		return TRUE.equals(active) ? isActive() : not(isActive());
	}

	static Specification<AgreementEntity> isActive() {
		return BUILDER.buildDateFilter(FROM_DATE, null, LocalDate.now().atTime(LocalTime.MAX))
			.and((entity, cq, cb) -> cb.or(cb.isNull(entity.get(TO_DATE)), cb.greaterThanOrEqualTo(entity.get(TO_DATE), LocalDate.now().atStartOfDay())));
	}
}
