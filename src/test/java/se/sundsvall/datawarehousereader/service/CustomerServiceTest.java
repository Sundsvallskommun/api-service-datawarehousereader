package se.sundsvall.datawarehousereader.service;

import static generated.se.sundsvall.party.PartyType.ENTERPRISE;
import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.sort;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerMapper.toPartyType;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsParameters;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.CustomerDetailsRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.CustomerRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailsEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerEntity;
import se.sundsvall.datawarehousereader.service.logic.PartyProvider;

import generated.se.sundsvall.party.PartyType;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	private static final String RANDOM_UUID = UUID.randomUUID().toString();

	@Mock
	private CustomerRepository repositoryMock;

	@Mock
	private CustomerDetailsRepository customerDetailsRepositoryMock;

	@Mock
	private PartyProvider partyProviderMock;

	@Mock
	private Page<CustomerEntity> pageMock;

	@Mock
	private CustomerEntity entityMock;

	@Mock
	private CustomerDetailsEntity detailsEntityMock;

	@InjectMocks
	private CustomerService service;

	@Captor
	private ArgumentCaptor<CustomerEngagementParameters> customerParameterCaptor;

	@Captor
	private ArgumentCaptor<List<String>> customerOrgIdsCaptor;

	@Captor
	private ArgumentCaptor<Pageable> pageableCaptor;

	private static Stream<Arguments> toCustomerTypesStreamArguments() {
		return Stream.of(
			Arguments.of(CustomerType.PRIVATE, PRIVATE),
			Arguments.of(CustomerType.ENTERPRISE, ENTERPRISE));
	}

	@ParameterizedTest
	@MethodSource("toCustomerTypesStreamArguments")
	void testGetEngagementsWithEmptyParameters(CustomerType customerType, PartyType partyType) {

		final var params = CustomerEngagementParameters.create();

		when(repositoryMock.findAllByParameters(any(CustomerEngagementParameters.class), ArgumentMatchers.any(), any(Pageable.class))).thenReturn(pageMock);
		when(entityMock.getCustomerType()).thenReturn(customerType.getStadsbackenTranslation());
		when(entityMock.getCustomerOrgId()).thenReturn("customerOrgId");
		when(pageMock.getContent()).thenReturn(List.of(entityMock));
		when(pageMock.getTotalPages()).thenReturn(1);
		when(pageMock.getTotalElements()).thenReturn(1L);
		when(pageMock.getNumber()).thenReturn(params.getPage() - 1);
		when(pageMock.getNumberOfElements()).thenReturn(1);
		when(pageMock.getSize()).thenReturn(params.getLimit());
		when(pageMock.getSort()).thenReturn(params.sort());
		when(partyProviderMock.translateToPartyId(eq(partyType), any())).thenReturn(RANDOM_UUID);

		final var response = service.getCustomerEngagements(params);

		verify(partyProviderMock).translateToPartyId(eq(partyType), any());
		verify(repositoryMock).findAllByParameters(customerParameterCaptor.capture(), customerOrgIdsCaptor.capture(), pageableCaptor.capture());

		assertThat(customerOrgIdsCaptor.getValue()).isEmpty();
		assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(100);
		assertThat(pageableCaptor.getValue().getSort()).isEqualTo(sort(CustomerEntity.class).by(CustomerEntity::getCustomerOrgId));
		assertThat(response.getMetaData().getCount()).isEqualTo(1L);
		assertThat(response.getMetaData().getLimit()).isEqualTo(100);
		assertThat(response.getMetaData().getPage()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(1);
		assertThat(response.getCustomerEngagements()).hasSize(1);
	}

	@Test
	void getDetailsWithPartyIdsAndCustomerEngagementOrgId() {
		final var customerEngagementOrgId = "5564786647";
		var parameters = CustomerDetailsParameters.create()
				.withFromDateTime(OffsetDateTime.now())
				.withCustomerEngagementOrgId(customerEngagementOrgId)
				.withPartyId(List.of(RANDOM_UUID));

		final var limit = 100;

		//Setup paging parameters
		parameters.setPage(1);
		parameters.setLimit(limit);
		parameters.setSortBy(List.of("customerOrgId"));

		//Instantiate page data
		Page<CustomerDetailsEntity> pagedResponse = new PageImpl<>(List.of(detailsEntityMock, detailsEntityMock), Pageable.ofSize(limit), 1);

		when(customerDetailsRepositoryMock.findWithCustomerEngagementOrgIdAndPartyIds(any(LocalDateTime.class), eq(customerEngagementOrgId), eq(List.of(RANDOM_UUID)), pageableCaptor.capture())).thenReturn(pagedResponse);

		var response = service.getCustomerDetails(parameters);

		verify(customerDetailsRepositoryMock).findWithCustomerEngagementOrgIdAndPartyIds(any(LocalDateTime.class), eq(customerEngagementOrgId), eq(List.of(RANDOM_UUID)), pageableCaptor.capture());

		//Assert that response data is somewhat what we send in.
		assertThat(response.getCustomerDetails()).hasSize(2);

		//Assert Page data
		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(limit);
		assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
		assertThat(pageableCaptor.getValue().getSort()).isEqualTo(sort(CustomerDetailsEntity.class).by(CustomerDetailsEntity::getCustomerOrgId).ascending());

		//Assert response paging data
		assertThat(response.getMetaData().getPage()).isEqualTo(1);
		assertThat(response.getMetaData().getLimit()).isEqualTo(limit);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(2L);
	}

	@Test
	void getDetailsWithOnlyCustomerEngagementOrgId() {
		final var customerEngagementOrgId = "5564786647";
		var parameters = CustomerDetailsParameters.create()
				.withFromDateTime(OffsetDateTime.now())
				.withCustomerEngagementOrgId(customerEngagementOrgId);

		final var limit = 100;

		//Setup paging parameters
		parameters.setPage(1);
		parameters.setLimit(limit);
		parameters.setSortBy(List.of("customerOrgId"));

		//Instantiate page data
		Page<CustomerDetailsEntity> pagedResponse = new PageImpl<>(List.of(detailsEntityMock, detailsEntityMock), Pageable.ofSize(limit), 1);

		when(customerDetailsRepositoryMock.findWithCustomerEngagementOrgId(any(LocalDateTime.class), eq(customerEngagementOrgId), pageableCaptor.capture())).thenReturn(pagedResponse);

		var response = service.getCustomerDetails(parameters);

		verify(customerDetailsRepositoryMock).findWithCustomerEngagementOrgId(any(LocalDateTime.class), eq(customerEngagementOrgId), pageableCaptor.capture());

		//Assert that response data is somewhat what we send in.
		assertThat(response.getCustomerDetails()).hasSize(2);

		//Assert Page data
		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(limit);
		assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
		assertThat(pageableCaptor.getValue().getSort()).isEqualTo(sort(CustomerDetailsEntity.class).by(CustomerDetailsEntity::getCustomerOrgId).ascending());

		//Assert response paging data
		assertThat(response.getMetaData().getPage()).isEqualTo(1);
		assertThat(response.getMetaData().getLimit()).isEqualTo(limit);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(2L);
	}

	@Test
	void testGetEngagementsWithAllParametersSet() {
		final var customerNumber = "1337";
		final var partyId = UUID.randomUUID().toString();
		final var legalId = "123456789012";
		final var customerType = CustomerType.PRIVATE;
		final var limit = 1;
		final var organizationNumber = "organizationNumber";
		final var organizationName = "organizationName";
		final var page = 2;
		final var params = CustomerEngagementParameters.create();

		params.setCustomerNumber(customerNumber);
		params.setPartyId(List.of(partyId));
		params.setLimit(limit);
		params.setOrganizationNumber(organizationNumber);
		params.setOrganizationName(organizationName);
		params.setPage(page);

		when(repositoryMock.findAllByParameters(any(CustomerEngagementParameters.class), ArgumentMatchers.any(), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getContent()).thenReturn(List.of(entityMock));
		when(entityMock.getCustomerType()).thenReturn(customerType.getStadsbackenTranslation());
		when(entityMock.getCustomerOrgId()).thenReturn(legalId);
		when(pageMock.getTotalPages()).thenReturn(2);
		when(pageMock.getTotalElements()).thenReturn(2L);
		when(pageMock.getNumber()).thenReturn(params.getPage() - 1);
		when(pageMock.getNumberOfElements()).thenReturn(1);
		when(pageMock.getSize()).thenReturn(params.getLimit());
		when(pageMock.getSort()).thenReturn(params.sort());
		when(partyProviderMock.translateToLegalId(partyId)).thenReturn(legalId);


		final var response = service.getCustomerEngagements(params);

		verify(partyProviderMock).translateToLegalId(partyId);
		verify(partyProviderMock).translateToPartyId(toPartyType(CustomerType.PRIVATE), legalId);
		verify(repositoryMock).findAllByParameters(customerParameterCaptor.capture(), customerOrgIdsCaptor.capture(), pageableCaptor.capture());

		assertThat(customerParameterCaptor.getValue().getCustomerNumber()).isEqualTo(customerNumber);
		assertThat(customerParameterCaptor.getValue().getOrganizationNumber()).isEqualTo(organizationNumber);
		assertThat(customerParameterCaptor.getValue().getOrganizationName()).isEqualTo(organizationName);
		assertThat(customerOrgIdsCaptor.getValue()).hasSize(1);
		assertThat(customerOrgIdsCaptor.getValue().get(0)).isEqualTo(legalId);
		assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(page - 1);
		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(limit);
		assertThat(pageableCaptor.getValue().getSort()).isEqualTo(sort(CustomerEntity.class).by(CustomerEntity::getCustomerOrgId));
		assertThat(response.getMetaData().getCount()).isEqualTo(1L);
		assertThat(response.getMetaData().getLimit()).isEqualTo(limit);
		assertThat(response.getMetaData().getPage()).isEqualTo(page);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(2);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(2);
		assertThat(response.getCustomerEngagements()).hasSize(1);
	}

	@Test
	void testGetEngagementsNoCallToPartyServiceIfLegalIdIsMissing() {
		final var customerNumber = "1337";
		final var params = CustomerEngagementParameters.create().withCustomerNumber(customerNumber);
		final var customerType = CustomerType.PRIVATE;

		when(repositoryMock.findAllByParameters(any(CustomerEngagementParameters.class), ArgumentMatchers.any(), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getContent()).thenReturn(List.of(entityMock));
		when(entityMock.getCustomerType()).thenReturn(customerType.getStadsbackenTranslation());
		when(entityMock.getCustomerOrgId()).thenReturn(null); // Just to show that this is the important prerequisite for this test.
		when(pageMock.getTotalPages()).thenReturn(1);
		when(pageMock.getTotalElements()).thenReturn(0L);
		when(pageMock.getNumber()).thenReturn(params.getPage() - 1);
		when(pageMock.getNumberOfElements()).thenReturn(0);
		when(pageMock.getSize()).thenReturn(params.getLimit());
		when(pageMock.getSort()).thenReturn(params.sort());

		service.getCustomerEngagements(params);

		verify(repositoryMock).findAllByParameters(customerParameterCaptor.capture(), customerOrgIdsCaptor.capture(), pageableCaptor.capture());
		verifyNoInteractions(partyProviderMock);

		assertThat(customerParameterCaptor.getValue().getCustomerNumber()).isEqualTo(customerNumber);
		assertThat(customerOrgIdsCaptor.getValue()).isEmpty();
		assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(100);
		assertThat(pageableCaptor.getValue().getSort()).isEqualTo(sort(CustomerEntity.class).by(CustomerEntity::getCustomerOrgId));
	}

	@Test
	void testGetEngagementsForPageLargerThanResultsMaxPage() {
		final var params = CustomerEngagementParameters.create();
		params.setPage(2);

		when(repositoryMock.findAllByParameters(any(CustomerEngagementParameters.class), ArgumentMatchers.any(), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getTotalPages()).thenReturn(1);
		when(pageMock.getTotalElements()).thenReturn(1L);
		when(pageMock.getNumber()).thenReturn(params.getPage() - 1);
		when(pageMock.getNumberOfElements()).thenReturn(0);
		when(pageMock.getSize()).thenReturn(params.getLimit());
		when(pageMock.getSort()).thenReturn(params.sort());

		final var response = service.getCustomerEngagements(params);

		verify(repositoryMock).findAllByParameters(any(), any(), any());

		assertThat(response.getMetaData().getCount()).isZero();
		assertThat(response.getMetaData().getLimit()).isEqualTo(100);
		assertThat(response.getMetaData().getPage()).isEqualTo(2);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(1);
		assertThat(response.getCustomerEngagements()).isEmpty();
	}
}
