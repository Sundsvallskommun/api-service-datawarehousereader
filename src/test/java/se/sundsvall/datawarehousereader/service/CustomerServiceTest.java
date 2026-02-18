package se.sundsvall.datawarehousereader.service;

import generated.se.sundsvall.party.PartyType;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsParameters;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.CustomerDetailRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.CustomerRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerEntity;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.MetadataEmbeddable;
import se.sundsvall.datawarehousereader.service.logic.PartyProvider;

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

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	private static final String RANDOM_UUID = UUID.randomUUID().toString();
	private static final String MUNICIPALITY_ID = "2281";

	@Mock
	private CustomerRepository repositoryMock;

	@Mock
	private CustomerDetailRepository customerDetailRepositoryMock;

	@Mock
	private PartyProvider partyProviderMock;

	@Mock
	private Page<CustomerEntity> pageMock;

	@Mock
	private CustomerEntity entityMock;

	@Mock
	private CustomerDetailEntity detailsEntityMock;

	@Mock
	private MetadataEmbeddable metadataMock;

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
		when(partyProviderMock.translateToPartyId(eq(partyType), eq(MUNICIPALITY_ID), any())).thenReturn(RANDOM_UUID);

		final var response = service.getCustomerEngagements(MUNICIPALITY_ID, params);

		verify(partyProviderMock).translateToPartyId(eq(partyType), eq(MUNICIPALITY_ID), any());
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
		// Arrange
		final var limit = 100;
		final var sortBy = "customerOrgId";
		final var customerEngagementOrgId = "5564786647";
		final var parameters = CustomerDetailsParameters.create()
			.withFromDateTime(OffsetDateTime.now())
			.withCustomerEngagementOrgId(customerEngagementOrgId)
			.withPartyId(List.of(RANDOM_UUID));
		parameters.setPage(1);
		parameters.setLimit(limit);
		parameters.setSortBy(List.of(sortBy));

		when(customerDetailRepositoryMock.findWithCustomerEngagementOrgIdAndPartyIds(any(LocalDateTime.class), eq(customerEngagementOrgId), eq(RANDOM_UUID), eq(1), eq(limit), eq(sortBy))).thenReturn(List.of(detailsEntityMock, detailsEntityMock));
		when(detailsEntityMock.getMetadata()).thenReturn(metadataMock);
		when(metadataMock.getCount()).thenReturn(2);
		when(metadataMock.getTotalPages()).thenReturn(1);
		when(metadataMock.getTotalRecords()).thenReturn(2);

		// Act
		final var response = service.getCustomerDetails(parameters);

		// Assert and verify
		verify(customerDetailRepositoryMock).findWithCustomerEngagementOrgIdAndPartyIds(any(LocalDateTime.class), eq(customerEngagementOrgId), eq(RANDOM_UUID), eq(1), eq(limit), eq(sortBy));

		assertThat(response.getCustomerDetails()).hasSize(2);
		assertThat(response.getMetaData().getPage()).isEqualTo(1);
		assertThat(response.getMetaData().getLimit()).isEqualTo(limit);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(2);
	}

	@Test
	void getDetailsWithOnlyCustomerEngagementOrgIdOrderedDescending() {
		// Arrange
		final var limit = 100;
		final var sortBy = "customerOrgId";
		final var customerEngagementOrgId = "5564786647";
		final var parameters = CustomerDetailsParameters.create()
			.withFromDateTime(OffsetDateTime.now())
			.withCustomerEngagementOrgId(customerEngagementOrgId);
		parameters.setPage(1);
		parameters.setLimit(limit);
		parameters.setSortBy(List.of(sortBy));
		parameters.setSortDirection(Direction.DESC);

		when(customerDetailRepositoryMock.findWithCustomerEngagementOrgId(any(LocalDateTime.class), eq(customerEngagementOrgId), eq(1), eq(limit), eq(sortBy + "#"))).thenReturn(List.of(detailsEntityMock, detailsEntityMock));
		when(detailsEntityMock.getMetadata()).thenReturn(metadataMock);
		when(metadataMock.getCount()).thenReturn(2);
		when(metadataMock.getTotalPages()).thenReturn(1);
		when(metadataMock.getTotalRecords()).thenReturn(2);

		// Act
		final var response = service.getCustomerDetails(parameters);

		// Assert and verify
		verify(customerDetailRepositoryMock).findWithCustomerEngagementOrgId(any(LocalDateTime.class), eq(customerEngagementOrgId), eq(1), eq(limit), eq(sortBy + "#"));

		assertThat(response.getCustomerDetails()).hasSize(2);
		assertThat(response.getMetaData().getPage()).isEqualTo(1);
		assertThat(response.getMetaData().getLimit()).isEqualTo(limit);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(2);
	}

	@Test
	void getDetailsNoSortingReturningNoMatch() {
		// Arrange
		final var limit = 100;
		final var customerEngagementOrgId = "5564786647";
		final var parameters = CustomerDetailsParameters.create()
			.withFromDateTime(OffsetDateTime.now())
			.withCustomerEngagementOrgId(customerEngagementOrgId);
		parameters.setPage(1);
		parameters.setLimit(limit);

		// Act
		final var response = service.getCustomerDetails(parameters);

		// Assert and verify
		verify(customerDetailRepositoryMock).findWithCustomerEngagementOrgId(any(LocalDateTime.class), eq(customerEngagementOrgId), eq(1), eq(limit), eq(""));

		assertThat(response.getCustomerDetails()).isEmpty();
		assertThat(response.getMetaData().getPage()).isEqualTo(1);
		assertThat(response.getMetaData().getLimit()).isEqualTo(100);
		assertThat(response.getMetaData().getTotalPages()).isZero();
		assertThat(response.getMetaData().getTotalRecords()).isZero();
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
		when(partyProviderMock.translateToLegalId(MUNICIPALITY_ID, partyId)).thenReturn(legalId);

		final var response = service.getCustomerEngagements(MUNICIPALITY_ID, params);

		verify(partyProviderMock).translateToLegalId(MUNICIPALITY_ID, partyId);
		verify(partyProviderMock).translateToPartyId(toPartyType(CustomerType.PRIVATE), MUNICIPALITY_ID, legalId);
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

		service.getCustomerEngagements(MUNICIPALITY_ID, params);

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

		final var response = service.getCustomerEngagements(MUNICIPALITY_ID, params);

		verify(repositoryMock).findAllByParameters(any(), any(), any());

		assertThat(response.getMetaData().getCount()).isZero();
		assertThat(response.getMetaData().getLimit()).isEqualTo(100);
		assertThat(response.getMetaData().getPage()).isEqualTo(2);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(1);
		assertThat(response.getCustomerEngagements()).isEmpty();
	}
}
