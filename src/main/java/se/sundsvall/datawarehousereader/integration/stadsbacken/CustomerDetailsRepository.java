package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailsEntity;

@Transactional(readOnly = true)
@CircuitBreaker(name = "CustomerDetailsRepository")
public interface CustomerDetailsRepository extends PagingAndSortingRepository<CustomerDetailsEntity, Integer>, JpaSpecificationExecutor<CustomerDetailsEntity> {
	@Query(value = "exec kundinfo.spCustomerDetails :fromDate", nativeQuery = true)
	List<CustomerDetailsEntity> findAllMatching(@Param("fromDate") LocalDateTime dateTimeFrom);
}
