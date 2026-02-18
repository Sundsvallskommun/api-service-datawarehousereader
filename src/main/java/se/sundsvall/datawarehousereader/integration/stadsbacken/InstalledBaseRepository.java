package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.inspector.WithRecompile;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity;
import se.sundsvall.datawarehousereader.service.util.ServiceUtil;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstalledBaseSpecification.withCareOf;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstalledBaseSpecification.withCity;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstalledBaseSpecification.withCompany;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstalledBaseSpecification.withCustomerId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstalledBaseSpecification.withFacilityId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstalledBaseSpecification.withHouseName;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstalledBaseSpecification.withLastModifiedDateBetween;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstalledBaseSpecification.withPostCode;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstalledBaseSpecification.withStreet;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstalledBaseSpecification.withType;

@Transactional(readOnly = true)
@CircuitBreaker(name = "installedBaseRepository")
public interface InstalledBaseRepository extends PagingAndSortingRepository<InstalledBaseItemEntity, Integer>, JpaSpecificationExecutor<InstalledBaseItemEntity> {

	@WithRecompile
	default Page<InstalledBaseItemEntity> findAllByParameters(final InstalledBaseParameters parameters, final Pageable pageable) {
		return findAll(withCareOf(parameters.getCareOf())
			.and(withCity(parameters.getCity()))
			.and(withCompany(parameters.getCompany()))
			.and(withCustomerId(ServiceUtil.toInteger(parameters.getCustomerNumber())))
			.and(withFacilityId(parameters.getFacilityId()))
			.and(withHouseName(parameters.getPropertyDesignation()))
			.and(withLastModifiedDateBetween(parameters.getLastModifiedDateFrom(), parameters.getLastModifiedDateTom()))
			.and(withPostCode(parameters.getPostCode()))
			.and(withStreet(parameters.getStreet()))
			.and(withType(parameters.getType())),
			pageable);
	}
}
