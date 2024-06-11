package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailEntity;

@Transactional(readOnly = true)
@CircuitBreaker(name = "CustomerDetailRepository")
public interface CustomerDetailRepository extends PagingAndSortingRepository<CustomerDetailEntity, Integer>, JpaSpecificationExecutor<CustomerDetailEntity> {

	/**
	 * Method for returning all customer engagements connected to sent in organization id
	 *
	 * @param dateTimeFrom   from date to filter on
	 * @param organizationId organization id to filter on
	 * @param uuids          comma separated list of uuids to filter on
	 * @param pageable       object containing paging information for request
	 * @return page of customer details matching sent in parameters
	 */
	@Query(nativeQuery = true, value = "select * from kundinfo.fnCustomerDetail(:fromDate, :organizationId, :uuids)")
	Page<CustomerDetailEntity> findWithCustomerEngagementOrgIdAndPartyIds(
		@Param("fromDate") LocalDateTime dateTimeFrom, @Param("organizationId") String organizationId, @Param("uuids") String uuids, Pageable pageable);

	/**
	 * Method for returning all customer engagements connected to sent in organization id
	 *
	 * @param dateTimeFrom   from date to filter on
	 * @param organizationId organization id to filter on
	 * @param pageable       object containing paging information for request
	 * @return page of customer details matching sent in parameters
	 */
	@Query(nativeQuery = true, value = "select * from kundinfo.fnCustomerDetail(:fromDate, :organizationId, null)")
	Page<CustomerDetailEntity> findWithCustomerEngagementOrgId(
		@Param("fromDate") LocalDateTime dateTimeFrom, @Param("organizationId") String organizationId, Pageable pageable);
}
