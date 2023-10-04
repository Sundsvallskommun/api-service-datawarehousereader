package se.sundsvall.datawarehousereader.service;

import static generated.se.sundsvall.party.PartyType.ENTERPRISE;
import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.sort;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerMapper.toPartyType;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
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
import org.springframework.data.domain.Sort;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetails;
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
		when(repositoryMock.findAllByParameters(any(CustomerEngagementParameters.class), ArgumentMatchers.<List<String>>any(), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getContent()).thenReturn(List.of(entityMock));
		when(entityMock.getCustomerType()).thenReturn(customerType.getStadsbackenTranslation());
		when(entityMock.getCustomerOrgId()).thenReturn("customerOrgId");
		when(pageMock.getTotalPages()).thenReturn(1);
		when(pageMock.getTotalElements()).thenReturn(1L);
		when(partyProviderMock.translateToPartyId(eq(partyType), any())).thenReturn(RANDOM_UUID);

		final var response = service.getCustomerEngagements(CustomerEngagementParameters.create());

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
	void getDetailsWithPartyIdSet() {
		final var randomUUID = UUID.randomUUID().toString();

		when(partyProviderMock.translateToPartyId(any(PartyType.class), any(String.class))).thenReturn(randomUUID).thenReturn("");

		when(customerDetailsRepositoryMock.findAllMatching(any(LocalDateTime.class))).thenReturn(List.of(
			CustomerDetailsEntity.create()
				.withCustomerOrgId("102000-0000")
				.withCustomerId(1)
				.withCustomerCategoryID(2)
				.withCustomerCategoryDescription("customerCategoryDescription")
				.withName("Name")
				.withCo("co")
				.withAddress("address")
				.withZipcode("zipcode")
				.withCity("city")
				.withPhone1("phone1")
				.withPhone2("phone2")
				.withPhone3("phone3")
				.withEmail1("email1")
				.withEmail2("email2")
				.withCustomerChangedFlg(true)
				.withInstalledChangedFlg(true),
			CustomerDetailsEntity.create()
				.withCustomerOrgId("19990101-1235")
				.withCustomerCategoryID(2)));

		final var params = CustomerDetailsParameters.create();
		params.setPartyId(List.of(randomUUID));
		params.setFromDateTime(OffsetDateTime.now());
		params.setLimit(100);
		params.setPage(1);
		params.setSortBy(Collections.singletonList("customerOrgId"));

		final var result = service.getCustomerDetails(params);

		verify(customerDetailsRepositoryMock).findAllMatching(any(LocalDateTime.class));
		verify(partyProviderMock, times(1)).translateToPartyId(eq(ENTERPRISE), any());
		verify(partyProviderMock, times(1)).translateToPartyId(eq(PRIVATE), any());

		assertThat(result.getCustomerDetails())
			.hasSize(1)
			.extracting(
				CustomerDetails::getPartyId,
				CustomerDetails::getCustomerOrgNumber,
				CustomerDetails::getCustomerCategoryID,
				CustomerDetails::getCustomerCategoryDescription,
				CustomerDetails::getCustomerName,
				CustomerDetails::getCareOf,
				CustomerDetails::getStreet,
				CustomerDetails::getPostalCode,
				CustomerDetails::getCity,
				CustomerDetails::getPhoneNumbers,
				CustomerDetails::getEmails,
				CustomerDetails::isCustomerChangedFlg,
				CustomerDetails::isInstalledChangedFlg)
			.containsExactly(tuple(
				randomUUID,
				"102000-0000",
				2,
				"customerCategoryDescription",
				"Name",
				"co",
				"address",
				"zipcode",
				"city",
				List.of("phone1", "phone2", "phone3"),
				List.of("email1", "email2"),
				true,
				true));

		assertThat(result.getMetaData().getCount()).isEqualTo(1L);
		assertThat(result.getMetaData().getLimit()).isEqualTo(100);
		assertThat(result.getMetaData().getPage()).isEqualTo(1);
		assertThat(result.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(result.getMetaData().getTotalRecords()).isEqualTo(1);
		assertThat(result.getMetaData().getSortBy()).isEqualTo(Collections.singletonList("customerOrgId"));
		assertThat(result.getMetaData().getSortDirection()).isEqualTo(Sort.Direction.ASC);
	}

	@Test
	void testGetDetailsWithoutPartyId() {
		when(partyProviderMock.translateToPartyId(any(PartyType.class), any(String.class))).thenThrow(Problem.valueOf(Status.NOT_FOUND, "Party not found"));

		when(customerDetailsRepositoryMock.findAllMatching(any(LocalDateTime.class))).thenReturn(List.of(
			CustomerDetailsEntity.create()
				.withCustomerOrgId("102000-0000")
				.withCustomerId(1)
				.withCustomerCategoryID(2)
				.withCustomerCategoryDescription("customerCategoryDescription")
				.withName("Name")
				.withCo("co")
				.withAddress("address")
				.withZipcode("zipcode")
				.withCity("city")
				.withPhone1("phone1")
				.withPhone2("phone2")
				.withPhone3("phone3")
				.withEmail1("email1")
				.withEmail2("email2")
				.withCustomerChangedFlg(true)
				.withInstalledChangedFlg(true),
			CustomerDetailsEntity.create()
				.withCustomerOrgId("19990101-1235")
				.withCustomerCategoryID(2)));

		final var params = CustomerDetailsParameters.create();
		params.setFromDateTime(OffsetDateTime.now());
		params.setLimit(100);
		params.setPage(1);
		params.setSortBy(Collections.singletonList("customerOrgId"));

		final var result = service.getCustomerDetails(params);

		verify(customerDetailsRepositoryMock).findAllMatching(any(LocalDateTime.class));
		verify(partyProviderMock, times(1)).translateToPartyId(eq(ENTERPRISE), any());
		verify(partyProviderMock, times(1)).translateToPartyId(eq(PRIVATE), any());

		assertThat(result.getCustomerDetails())
			.hasSize(2)
			.extracting(
				CustomerDetails::getPartyId,
				CustomerDetails::getCustomerOrgNumber,
				CustomerDetails::getCustomerCategoryID,
				CustomerDetails::getCustomerCategoryDescription,
				CustomerDetails::getCustomerName,
				CustomerDetails::getCareOf,
				CustomerDetails::getStreet,
				CustomerDetails::getPostalCode,
				CustomerDetails::getCity,
				CustomerDetails::getPhoneNumbers,
				CustomerDetails::getEmails,
				CustomerDetails::isCustomerChangedFlg,
				CustomerDetails::isInstalledChangedFlg)
			.containsExactly(
				tuple(null, "102000-0000", 2, "customerCategoryDescription", "Name", "co", "address", "zipcode", "city", List.of("phone1", "phone2", "phone3"), List.of("email1", "email2"), true, true),
				tuple(null, "19990101-1235", 2, null, null, null, null, null, null, emptyList(), emptyList(), false, false));

		assertThat(result.getMetaData().getCount()).isEqualTo(2L);
		assertThat(result.getMetaData().getLimit()).isEqualTo(100);
		assertThat(result.getMetaData().getPage()).isEqualTo(1);
		assertThat(result.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(result.getMetaData().getTotalRecords()).isEqualTo(2);
		assertThat(result.getMetaData().getSortBy()).isEqualTo(Collections.singletonList("customerOrgId"));
		assertThat(result.getMetaData().getSortDirection()).isEqualTo(Sort.Direction.ASC);
	}

	@Test
	void getDetailsWithCustomerEngagementOrgIdSet() {
		final var randomUUID = UUID.randomUUID().toString();

		when(partyProviderMock.translateToPartyId(any(PartyType.class), any(String.class))).thenReturn(randomUUID).thenReturn("");

		when(customerDetailsRepositoryMock.findAllMatching(any(LocalDateTime.class))).thenReturn(List.of(
			CustomerDetailsEntity.create()
				.withCustomerOrgId("102000-0000")
				.withCustomerId(1)
				.withCustomerCategoryID(2)
				.withCustomerCategoryDescription("customerCategoryDescription")
				.withName("Name")
				.withCo("co")
				.withAddress("address")
				.withZipcode("zipcode")
				.withCity("city")
				.withPhone1("phone1")
				.withPhone2("phone2")
				.withPhone3("phone3")
				.withEmail1("email1")
				.withEmail2("email2")
				.withCustomerChangedFlg(true)
				.withInstalledChangedFlg(true),
			CustomerDetailsEntity.create()
				.withCustomerOrgId("19990101-1235")
				.withCustomerCategoryID(2)));

		final var params = CustomerDetailsParameters.create();
		params.setCustomerEngagementOrgId("1020000000");
		params.setFromDateTime(OffsetDateTime.now());
		params.setLimit(100);
		params.setPage(1);
		params.setSortBy(Collections.singletonList("customerOrgId"));

		final var result = service.getCustomerDetails(params);

		verify(customerDetailsRepositoryMock).findAllMatching(any(LocalDateTime.class));
		verify(partyProviderMock, times(1)).translateToPartyId(eq(ENTERPRISE), any());
		verify(partyProviderMock, times(1)).translateToPartyId(eq(PRIVATE), any());

		assertThat(result.getCustomerDetails())
			.hasSize(1)
			.extracting(
				CustomerDetails::getPartyId,
				CustomerDetails::getCustomerOrgNumber,
				CustomerDetails::getCustomerCategoryID,
				CustomerDetails::getCustomerCategoryDescription,
				CustomerDetails::getCustomerName,
				CustomerDetails::getCareOf,
				CustomerDetails::getStreet,
				CustomerDetails::getPostalCode,
				CustomerDetails::getCity,
				CustomerDetails::getPhoneNumbers,
				CustomerDetails::getEmails,
				CustomerDetails::isCustomerChangedFlg,
				CustomerDetails::isInstalledChangedFlg)
			.containsExactly(tuple(
				randomUUID,
				"102000-0000",
				2,
				"customerCategoryDescription",
				"Name",
				"co",
				"address",
				"zipcode",
				"city",
				List.of("phone1", "phone2", "phone3"),
				List.of("email1", "email2"),
				true,
				true));

		assertThat(result.getMetaData().getCount()).isEqualTo(1L);
		assertThat(result.getMetaData().getLimit()).isEqualTo(100);
		assertThat(result.getMetaData().getPage()).isEqualTo(1);
		assertThat(result.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(result.getMetaData().getTotalRecords()).isEqualTo(1);
		assertThat(result.getMetaData().getSortBy()).isEqualTo(Collections.singletonList("customerOrgId"));
		assertThat(result.getMetaData().getSortDirection()).isEqualTo(Sort.Direction.ASC);
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

		when(repositoryMock.findAllByParameters(any(CustomerEngagementParameters.class), ArgumentMatchers.<List<String>>any(), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getContent()).thenReturn(List.of(entityMock));
		when(entityMock.getCustomerType()).thenReturn(customerType.getStadsbackenTranslation());
		when(entityMock.getCustomerOrgId()).thenReturn(legalId);
		when(pageMock.getTotalPages()).thenReturn(2);
		when(pageMock.getTotalElements()).thenReturn(2L);
		when(partyProviderMock.translateToLegalId(partyId)).thenReturn(legalId);

		params.setCustomerNumber(customerNumber);
		params.setPartyId(List.of(partyId));
		params.setLimit(limit);
		params.setOrganizationNumber(organizationNumber);
		params.setOrganizationName(organizationName);
		params.setPage(page);

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

		when(repositoryMock.findAllByParameters(any(CustomerEngagementParameters.class), ArgumentMatchers.<List<String>>any(), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getContent()).thenReturn(List.of(entityMock));
		when(entityMock.getCustomerType()).thenReturn(customerType.getStadsbackenTranslation());
		when(entityMock.getCustomerOrgId()).thenReturn(null); // Just to show that this is the important prerequisite for this test.
		when(pageMock.getTotalPages()).thenReturn(2);
		when(pageMock.getTotalElements()).thenReturn(2L);

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
		when(repositoryMock.findAllByParameters(any(CustomerEngagementParameters.class), ArgumentMatchers.<List<String>>any(), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getTotalPages()).thenReturn(1);
		when(pageMock.getTotalElements()).thenReturn(1L);

		final var params = CustomerEngagementParameters.create();
		params.setPage(2);
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
