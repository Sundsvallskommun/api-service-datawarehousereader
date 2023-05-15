package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity;

@Transactional
@CircuitBreaker(name = "installedBaseRepository")
public interface InstalledBaseRepository extends PagingAndSortingRepository<InstalledBaseItemEntity, Integer>, QueryByExampleExecutor<InstalledBaseItemEntity> {}
