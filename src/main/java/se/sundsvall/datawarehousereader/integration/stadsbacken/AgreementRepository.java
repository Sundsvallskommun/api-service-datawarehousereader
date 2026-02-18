package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.datawarehousereader.api.model.agreement.AgreementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.inspector.WithRecompile;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.agreement.AgreementKey;
import se.sundsvall.datawarehousereader.service.util.ServiceUtil;

import static java.util.Optional.ofNullable;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withActive;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withAgreementId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withBillingId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withBinding;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withBindingRule;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withCategories;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withCustomerId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withCustomerOrgId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withDescription;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withFacilityId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withFromDate;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withMainAgreement;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withNetAreaId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withPlacementStatus;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withProduction;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withSiteAddress;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.AgreementSpecification.withToDate;

@Transactional(readOnly = true)
@CircuitBreaker(name = "agreementRepository")
public interface AgreementRepository extends PagingAndSortingRepository<AgreementEntity, AgreementKey>, JpaSpecificationExecutor<AgreementEntity> {

	@WithRecompile
	default Page<AgreementEntity> findAllByParameters(AgreementParameters agreementParameters, String customerOrgId, Pageable pageable) {
		final var parameters = ofNullable(agreementParameters).orElse(AgreementParameters.create());
		return this.findAll(withAgreementId(ServiceUtil.toInteger(parameters.getAgreementId()))
			.and(withBillingId(ServiceUtil.toInteger(parameters.getBillingId())))
			.and(withBinding(ServiceUtil.toString(parameters.getBinding())))
			.and(withBindingRule(parameters.getBindingRule()))
			.and(withCategories(parameters.getCategory()))
			.and(withCustomerId(ServiceUtil.toInteger(parameters.getCustomerNumber())))
			.and(withCustomerOrgId(customerOrgId))
			.and(withDescription(parameters.getDescription()))
			.and(withFacilityId(parameters.getFacilityId()))
			.and(withFromDate(parameters.getFromDate()))
			.and(withMainAgreement(ServiceUtil.toString(parameters.getMainAgreement())))
			.and(withNetAreaId(parameters.getNetAreaId()))
			.and(withSiteAddress(parameters.getSiteAddress()))
			.and(withPlacementStatus(parameters.getPlacementStatus()))
			.and(withProduction(parameters.getProduction()))
			.and(withToDate(parameters.getToDate()))
			.and(withActive(parameters.getActive())), pageable);
	}
}
