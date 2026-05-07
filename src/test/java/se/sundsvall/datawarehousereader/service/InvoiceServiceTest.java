package se.sundsvall.datawarehousereader.service;

import java.time.LocalDate;
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
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoice;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoiceParameters;
import se.sundsvall.datawarehousereader.api.model.invoice.CustomerInvoiceResponse;
import se.sundsvall.datawarehousereader.api.model.invoice.InvoiceParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceDetailRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceJdbcRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InvoiceRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceDetailEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.invoice.InvoiceEntity;
import se.sundsvall.datawarehousereader.service.mapper.InvoiceMapper;

import static org.assertj.core.api.Assertions.assertThat;
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

	@Mock
	private InvoiceJdbcRepository invoiceJdbcRepositoryMock;

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
	void getInvoicesForCustomer_enrichesEachInvoiceWithDetails() {
		final var customerNumber = "216870";
		final var organizationA = "5565027223";
		final var organizationB = "5564786647";
		final var invoiceA = 295334999L;
		final var invoiceB = 60003118415L;

		final var parameters = CustomerInvoiceParameters.create()
			.withOrganizationIds(List.of("5565027223", "5564786647"))
			.withPeriodFrom(LocalDate.of(2025, 1, 1))
			.withPeriodTo(LocalDate.of(2025, 12, 31))
			.withSortBy("periodFrom");
		parameters.setPage(2);
		parameters.setLimit(5);

		final var first = CustomerInvoice.create()
			.withInvoiceNumber(invoiceA)
			.withOrganizationNumber(organizationA);
		final var second = CustomerInvoice.create()
			.withInvoiceNumber(invoiceB)
			.withOrganizationNumber(organizationB);
		final var jdbcResponse = CustomerInvoiceResponse.create().withInvoices(List.of(first, second));

		final var detailA = new InvoiceDetailEntity();
		final var detailB = new InvoiceDetailEntity();

		when(invoiceJdbcRepositoryMock.getInvoices(2, 5, "5565027223,5564786647", customerNumber,
			LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), "periodFrom"))
			.thenReturn(jdbcResponse);
		when(invoiceDetailRepositoryMock.findAllByOrganizationIdAndInvoiceNumber(organizationA, invoiceA))
			.thenReturn(List.of(detailA));
		when(invoiceDetailRepositoryMock.findAllByOrganizationIdAndInvoiceNumber(organizationB, invoiceB))
			.thenReturn(List.of(detailB));

		final var result = service.getInvoicesForCustomer(customerNumber, parameters);

		assertThat(result).isSameAs(jdbcResponse);
		assertThat(result.getInvoices().getFirst().getDetails())
			.usingRecursiveComparison().isEqualTo(InvoiceMapper.toDetails(List.of(detailA)));
		assertThat(result.getInvoices().get(1).getDetails())
			.usingRecursiveComparison().isEqualTo(InvoiceMapper.toDetails(List.of(detailB)));

		verify(invoiceJdbcRepositoryMock).getInvoices(2, 5, "5565027223,5564786647", customerNumber,
			LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), "periodFrom");
		verify(invoiceDetailRepositoryMock).findAllByOrganizationIdAndInvoiceNumber(organizationA, invoiceA);
		verify(invoiceDetailRepositoryMock).findAllByOrganizationIdAndInvoiceNumber(organizationB, invoiceB);
	}

	@Test
	void getInvoicesForCustomer_emptyPage_doesNotCallDetailRepository() {
		final var customerNumber = "216870";
		final var parameters = CustomerInvoiceParameters.create();
		final var emptyResponse = CustomerInvoiceResponse.create().withInvoices(List.of());

		when(invoiceJdbcRepositoryMock.getInvoices(any(), any(), any(), any(), any(), any(), any()))
			.thenReturn(emptyResponse);

		final var result = service.getInvoicesForCustomer(customerNumber, parameters);

		assertThat(result.getInvoices()).isEmpty();
		verify(invoiceJdbcRepositoryMock).getInvoices(any(), any(), any(), any(), any(), any(), any());
	}

	@Test
	void getInvoicesForCustomer_invoiceWithNoDetails_setsEmptyList() {
		final var customerNumber = "216870";
		final var parameters = CustomerInvoiceParameters.create();
		final var invoice = CustomerInvoice.create()
			.withInvoiceNumber(1L)
			.withOrganizationNumber("orgX");

		when(invoiceJdbcRepositoryMock.getInvoices(any(), any(), any(), any(), any(), any(), any()))
			.thenReturn(CustomerInvoiceResponse.create().withInvoices(List.of(invoice)));
		when(invoiceDetailRepositoryMock.findAllByOrganizationIdAndInvoiceNumber("orgX", 1L))
			.thenReturn(List.of());

		final var result = service.getInvoicesForCustomer(customerNumber, parameters);

		assertThat(result.getInvoices().getFirst().getDetails()).isEmpty();
		verify(invoiceDetailRepositoryMock).findAllByOrganizationIdAndInvoiceNumber("orgX", 1L);
	}

	@Test
	void getInvoiceDetails_noMatch() {
		var organizationNumber = "1234567890";
		var invoiceNumber = 987654L;

		when(invoiceDetailRepositoryMock.findAllByOrganizationIdAndInvoiceNumber(organizationNumber, invoiceNumber)).thenReturn(List.of());

		var result = service.getInvoiceDetails(organizationNumber, invoiceNumber);

		assertThat(result).isEmpty();
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
