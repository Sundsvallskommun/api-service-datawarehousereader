package se.sundsvall.datawarehousereader.integration.stadsbacken;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.transaction.annotation.Transactional;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity;

@Transactional(readOnly = true)
@CircuitBreaker(name = "installedBaseRepository")
public interface InstalledBaseRepository extends PagingAndSortingRepository<InstalledBaseItemEntity, Integer>, QueryByExampleExecutor<InstalledBaseItemEntity> {
}
