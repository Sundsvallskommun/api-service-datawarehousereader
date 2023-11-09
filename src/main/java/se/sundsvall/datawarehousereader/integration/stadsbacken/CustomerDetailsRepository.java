package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailsEntity;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Transactional(readOnly = true)
@CircuitBreaker(name = "CustomerDetailsRepository")
public interface CustomerDetailsRepository extends PagingAndSortingRepository<CustomerDetailsEntity, Integer>, JpaSpecificationExecutor<CustomerDetailsEntity> {

	@Query(nativeQuery = true, value ="select * from kundinfo.fnCustomerDetails(:fromDate, :organizationId) where uuid in :uuids")
	Page<CustomerDetailsEntity> findWithCustomerEngagementOrgIdAndPartyIds(
			@Param("fromDate") LocalDateTime dateTimeFrom, @Param("organizationId") String organizationId, @Param("uuids") List<String> uuids, Pageable pageable);

	@Query(nativeQuery = true, value ="select * from kundinfo.fnCustomerDetails(:fromDate, :organizationId)")
	Page<CustomerDetailsEntity> findWithCustomerEngagementOrgId(
			@Param("fromDate") LocalDateTime dateTimeFrom, @Param("organizationId") String organizationId, Pageable pageable);
}
