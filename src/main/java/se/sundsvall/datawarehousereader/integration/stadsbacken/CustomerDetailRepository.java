package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailEntity;

@Transactional(readOnly = true)
@CircuitBreaker(name = "CustomerDetailRepository")
public interface CustomerDetailRepository extends JpaRepository<CustomerDetailEntity, Integer> {

	/**
	 * /**
	 * Method for returning all customer engagements connected to sent in organization id
	 *
	 * @param  dateTimeFrom   from date to filter on
	 * @param  organizationId organization id to filter on
	 * @param  uuids          comma separated list of uuids to filter on
	 * @param  pageNumber     the page number to fetch
	 * @param  pageSize       the number of items to fetch
	 * @param  sortBy         a comma separated string with the column(s) to sort by (if column is suffixed by '#' it will
	 *                        be sorted descending, otherwise its sorted ascending)
	 * @return                list of customer details matching sent in parameters
	 */
	@Query(nativeQuery = true, value = "select * from kundinfo.fnCustomerDetailWithPagingAndSort(:fromDate, :organizationId, :uuid, :pageNumber, :pageSize, :sortBy)")
	List<CustomerDetailEntity> findWithCustomerEngagementOrgIdAndPartyIds(
		@Param("fromDate") LocalDateTime dateTimeFrom,
		@Param("organizationId") String organizationId,
		@Param("uuid") String uuids,
		@Param("pageNumber") int pageNumber,
		@Param("pageSize") int pageSize,
		@Param("sortBy") String sortBy);

	/**
	 * Method for returning all customer engagements connected to sent in organization id
	 *
	 * @param  dateTimeFrom   from date to filter on
	 * @param  organizationId organization id to filter on
	 * @param  pageNumber     the page number to fetch
	 * @param  pageSize       the number of items to fetch
	 * @param  sortBy         a comma separated string with the column(s) to sort by (if column is suffixed by '#' it will
	 *                        be sorted descending, otherwise its sorted ascending)
	 * @return                list of customer details matching sent in parameters
	 */
	@Query(nativeQuery = true, value = "select * from kundinfo.fnCustomerDetailWithPagingAndSort(:fromDate, :organizationId, null, :pageNumber, :pageSize, :sortBy)")
	List<CustomerDetailEntity> findWithCustomerEngagementOrgId(
		@Param("fromDate") LocalDateTime dateTimeFrom,
		@Param("organizationId") String organizationId,
		@Param("pageNumber") int pageNumber,
		@Param("pageSize") int pageSize,
		@Param("sortBy") String sortBy);
}
