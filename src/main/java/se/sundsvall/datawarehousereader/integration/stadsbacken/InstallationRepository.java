package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static java.util.Optional.ofNullable;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstallationSpecification.withCustomerFlag;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstallationSpecification.withFacilityId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstallationSpecification.withLastModifiedDateBetween;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InstallationSpecification.withType;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.installation.InstallationParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.inspector.WithRecompile;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationEntity;

@Transactional(readOnly = true)
@CircuitBreaker(name = "installationRepository")
public interface InstallationRepository extends PagingAndSortingRepository<InstallationEntity, Integer>, JpaSpecificationExecutor<InstallationEntity> {

	@WithRecompile
	default Page<InstallationEntity> findAllByParameters(final InstallationParameters parameters, final Pageable pageable) {
		return findAll(withCustomerFlag(parameters.getInstalled())
			.and(withLastModifiedDateBetween(parameters.getLastModifiedDateFrom(), parameters.getLastModifiedDateTo()))
			.and(withType(ofNullable(parameters.getCategory()).map(Category::toStadsbackenValue).orElse(null)))
			.and(withFacilityId(parameters.getFacilityId())),
			pageable);
	}
}
