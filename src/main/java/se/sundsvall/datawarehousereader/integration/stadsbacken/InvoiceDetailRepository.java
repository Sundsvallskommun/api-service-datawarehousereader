package se.sundsvall.datawarehousereader.integration.stadsbacken;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceDetailEntity;

@Transactional(readOnly = true)
@CircuitBreaker(name = "invoiceDetailRepository")
public interface InvoiceDetailRepository extends CrudRepository<InvoiceDetailEntity, Integer> {

	List<InvoiceDetailEntity> findAllByOrganizationIdAndInvoiceNumber(String organizationNumber, long invoiceNumber);
}
