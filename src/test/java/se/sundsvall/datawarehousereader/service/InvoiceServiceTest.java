package se.sundsvall.datawarehousereader.service;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zalando.problem.Problem;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceDetailRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceDetailEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;
import se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper.toInvoices;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

	@Mock
	private InvoiceRepository invoiceRepositoryMock;

	@Mock
	private InvoiceDetailRepository invoiceDetailRepositoryMock;

	@InjectMocks
	private InvoiceService service;

	@Captor
	private ArgumentCaptor<InvoiceParameters> parametersCaptor;

	@Captor
	private ArgumentCaptor<Pageable> pageableCaptor;

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(invoiceRepositoryMock);
	}

	@Test
	void getInvoices_noParameters() {
		final var invoiceNumbers = List.of(1L);
		final var invoiceEntity1 = new InvoiceEntity();
		invoiceEntity1.setInvoiceNumber(1L);
		final var invoiceEntities = List.of(invoiceEntity1);
		final var params = InvoiceParameters.create();

		final var pageable = Pageable.ofSize(params.getLimit()).withPage(params.getPage() - 1);
		final Page<Long> page = new PageImpl<>(invoiceNumbers, pageable, invoiceEntities.size());

		when(invoiceRepositoryMock.findDistinctInvoiceNumbers(eq(params), any(Pageable.class))).thenReturn(page);
		when(invoiceRepositoryMock.findAllByInvoiceNumberIn(invoiceNumbers)).thenReturn(invoiceEntities);

		final var result = service.getInvoices(params);

		verify(invoiceRepositoryMock).findDistinctInvoiceNumbers(eq(params), pageableCaptor.capture());

		assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(params.getPage() - 1);
		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(params.getLimit());
		assertThat(pageableCaptor.getValue().getSort()).isEqualTo(params.sort());
		assertThat(result.getMetaData().getCount()).isEqualTo(result.getInvoices().size());
		assertThat(result.getMetaData().getLimit()).isEqualTo(params.getLimit());
		assertThat(result.getMetaData().getPage()).isEqualTo(params.getPage());
		assertThat(result.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(result.getMetaData().getTotalRecords()).isEqualTo(1);
		assertThat(result.getInvoices()).isEqualTo(toInvoices(invoiceEntities));
	}

	@Test
	void getInvoices_sortedAndLimited() {
		final var invoiceNumbers = List.of(1L, 2L);
		final var invoiceEntity1 = new InvoiceEntity();
		invoiceEntity1.setInvoiceNumber(1L);
		final var invoiceEntity2 = new InvoiceEntity();
		invoiceEntity2.setInvoiceNumber(2L);
		final var invoiceEntities = List.of(invoiceEntity1, invoiceEntity2);
		final var params = InvoiceParameters.createWithLimit(125)
			.withCustomerNumber(List.of("12345"))
			.withSortBy(List.of("invoiceDate"))
			.withSortDirection(Sort.Direction.DESC);

		final var pageable = Pageable.ofSize(params.getLimit()).withPage(params.getPage() - 1);
		final Page<Long> page = new PageImpl<>(invoiceNumbers, pageable, invoiceEntities.size());

		when(invoiceRepositoryMock.findDistinctInvoiceNumbers(eq(params), any(Pageable.class))).thenReturn(page);
		when(invoiceRepositoryMock.findAllByInvoiceNumberIn(invoiceNumbers)).thenReturn(invoiceEntities);

		final var result = service.getInvoices(params);

		verify(invoiceRepositoryMock).findDistinctInvoiceNumbers(parametersCaptor.capture(), pageableCaptor.capture());

		assertThat(parametersCaptor.getValue()).isEqualTo(params);
		assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(params.getPage() - 1);
		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(params.getLimit());
		assertThat(pageableCaptor.getValue().getSort()).isEqualTo(params.sort());
		assertThat(result.getMetaData().getCount()).isEqualTo(2);
		assertThat(result.getMetaData().getLimit()).isEqualTo(125);
		assertThat(result.getMetaData().getPage()).isEqualTo(1);
		assertThat(result.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(result.getMetaData().getTotalRecords()).isEqualTo(2);
		assertThat(result.getInvoices()).hasSameElementsAs(toInvoices(Map.of(
			1L, List.of(invoiceEntity1),
			2L, List.of(invoiceEntity2))));
	}

	@Test
	void getInvoiceDetails_notFound() {
		var organizationNumber = "1234567890";
		var invoiceNumber = 987654L;

		when(invoiceDetailRepositoryMock.findAllByOrganizationIdAndInvoiceNumber(organizationNumber, invoiceNumber)).thenReturn(List.of());

		assertThatThrownBy(() -> service.getInvoiceDetails(organizationNumber, invoiceNumber))
			.isInstanceOfAny(Problem.class)
			.hasMessageContaining(String.format("Not Found: No invoicedetails found for invoice issuer '%s' and invoicenumber '%s'", organizationNumber, invoiceNumber));

		verify(invoiceDetailRepositoryMock).findAllByOrganizationIdAndInvoiceNumber(organizationNumber, invoiceNumber);
	}

	@Test
	void getInvoiceDetails_found() {
		var organizationNumber = "1234567890";
		var invoiceNumber = 987654L;

		var invoiceDetailEntity = new InvoiceDetailEntity();

		when(invoiceDetailRepositoryMock.findAllByOrganizationIdAndInvoiceNumber(organizationNumber, invoiceNumber)).thenReturn(List.of(invoiceDetailEntity));

		var result = service.getInvoiceDetails(organizationNumber, invoiceNumber);

		assertThat(result).hasSize(1);
		assertThat(result).usingRecursiveComparison().isEqualTo(InvoiceMapper.toDetails(List.of(invoiceDetailEntity)));

		verify(invoiceDetailRepositoryMock).findAllByOrganizationIdAndInvoiceNumber(organizationNumber, invoiceNumber);
	}
}
