package se.sundsvall.datawarehousereader.integration.stadsbacken;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.specification.InvoiceSpecification.createSpecification;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;

@Transactional(readOnly = true)
@CircuitBreaker(name = "invoiceRepository")
public interface InvoiceRepository extends PagingAndSortingRepository<InvoiceEntity, Integer>, JpaSpecificationExecutor<InvoiceEntity> {

	List<InvoiceEntity> findAllByInvoiceNumberIn(final List<Long> invoiceNumbers);

	default Page<Long> findDistinctInvoiceNumbers(final InvoiceParameters parameters, final Pageable pageable) {
		var invoices = findAll(createSpecification(parameters), pageable.getSort());

		var totalDistinctInvoiceNumbers = invoices.stream()
			.map(InvoiceEntity::getInvoiceNumber)
			.distinct()
			.count();

		var invoiceNumbers = invoices.stream()
			.map(InvoiceEntity::getInvoiceNumber)
			.distinct()
			.skip(pageable.getOffset())
			.limit(pageable.getPageSize())
			.collect(Collectors.toCollection(ArrayList::new));

		return new PageImpl<>(invoiceNumbers, pageable, totalDistinctInvoiceNumbers);
	}
}
