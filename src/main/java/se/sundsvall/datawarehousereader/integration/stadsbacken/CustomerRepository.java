package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.inspector.WithRecompile;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerKey;
import se.sundsvall.datawarehousereader.service.util.ServiceUtil;

import static java.util.Optional.ofNullable;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.CustomerSpecification.withCustomerId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.CustomerSpecification.withCustomerOrgIds;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.CustomerSpecification.withOrganizationId;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.CustomerSpecification.withOrganizationName;

@Transactional(readOnly = true)
@CircuitBreaker(name = "customerRepository")
public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity, CustomerKey>, JpaSpecificationExecutor<CustomerEntity> {

	@WithRecompile
	default Page<CustomerEntity> findAllByParameters(final CustomerEngagementParameters customerParameters, final List<String> customerOrgIds, final Pageable pageable) {
		final var parameters = ofNullable(customerParameters).orElse(CustomerEngagementParameters.create());
		return this.findAll(withCustomerId(ServiceUtil.toInteger(parameters.getCustomerNumber()))
			.and(withCustomerOrgIds(customerOrgIds))
			.and(withOrganizationId(parameters.getOrganizationNumber()))
			.and(withOrganizationName(parameters.getOrganizationName())), pageable);
	}
}
