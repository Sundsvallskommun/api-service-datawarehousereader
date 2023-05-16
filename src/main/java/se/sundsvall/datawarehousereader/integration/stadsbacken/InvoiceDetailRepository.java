package se.sundsvall.datawarehousereader.integration.stadsbacken;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceDetailEntity;

import java.util.List;

@Transactional
@CircuitBreaker(name = "invoiceDetailRepository")
public interface InvoiceDetailRepository extends CrudRepository<InvoiceDetailEntity, Integer> {

	List<InvoiceDetailEntity> findAllByOrganizationIdAndInvoiceNumber(String organizationNumber, long invoiceNumber);
}
