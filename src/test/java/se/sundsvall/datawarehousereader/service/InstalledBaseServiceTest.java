package se.sundsvall.datawarehousereader.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.unsorted;
import static se.sundsvall.datawarehousereader.service.mapper.InstalledBaseMapper.toInstalledBaseItems;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InstalledBaseRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installedbase.InstalledBaseItemEntity;

@ExtendWith(MockitoExtension.class)
class InstalledBaseServiceTest {

	@Mock
	private InstalledBaseRepository repositoryMock;

	@Mock
	private Page<InstalledBaseItemEntity> pageMock;

	@Mock
	private InstalledBaseItemEntity entityMock;

	@InjectMocks
	private InstalledBaseService service;

	@Captor
	private ArgumentCaptor<InstalledBaseParameters> parametersCaptor;

	@Captor
	private ArgumentCaptor<Pageable> pageableCaptor;

	@Test
	void testWithEmptyParameters() {
		final var params = InstalledBaseParameters.create();

		when(repositoryMock.findAllByParameters(ArgumentMatchers.<InstalledBaseParameters>any(), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getContent()).thenReturn(List.of(entityMock));
		when(pageMock.getTotalPages()).thenReturn(1);
		when(pageMock.getTotalElements()).thenReturn(1L);
		when(pageMock.getNumber()).thenReturn(params.getPage() - 1);
		when(pageMock.getNumberOfElements()).thenReturn(1);
		when(pageMock.getSize()).thenReturn(params.getLimit());
		when(pageMock.getSort()).thenReturn(params.sort());

		final var response = service.getInstalledBase(params);

		verify(repositoryMock).findAllByParameters(parametersCaptor.capture(), pageableCaptor.capture());

		assertThat(parametersCaptor.getValue())
			.hasAllNullFieldsOrPropertiesExcept("page", "limit", "sortBy", "sortDirection")
			.extracting(InstalledBaseParameters::getPage, InstalledBaseParameters::getLimit)
			.isEqualTo(List.of(1, 100));
		assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(100);
		assertThat(pageableCaptor.getValue().getSort()).isEqualTo(unsorted());
		assertThat(response.getMetaData().getCount()).isEqualTo(1L);
		assertThat(response.getMetaData().getLimit()).isEqualTo(100);
		assertThat(response.getMetaData().getPage()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(1);
		assertThat(response.getInstalledBase()).isEqualTo(toInstalledBaseItems(List.of(entityMock)));
	}

	@Test
	void testWithAllParametersSet() {
		final var careOf = "careOf";
		final var city = "city";
		final var company = "company";
		final var customerNumber = "1337";
		final var limit = 1;
		final var facilityId = "facilityId";
		final var page = 2;
		final var postCode = "postCode";
		final var street = "street";
		final var type = "type";
		final var params = InstalledBaseParameters.create();
		params.setCareOf(careOf);
		params.setCity(city);
		params.setCompany(company);
		params.setCustomerNumber(customerNumber);
		params.setLimit(limit);
		params.setFacilityId(facilityId);
		params.setPage(page);
		params.setPostCode(postCode);
		params.setStreet(street);
		params.setType(type);

		when(repositoryMock.findAllByParameters(ArgumentMatchers.<InstalledBaseParameters>any(), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getContent()).thenReturn(List.of(entityMock));
		when(pageMock.getTotalPages()).thenReturn(2);
		when(pageMock.getTotalElements()).thenReturn(2L);
		when(pageMock.getNumber()).thenReturn(params.getPage() - 1);
		when(pageMock.getNumberOfElements()).thenReturn(1);
		when(pageMock.getSize()).thenReturn(params.getLimit());
		when(pageMock.getSort()).thenReturn(params.sort());

		final var response = service.getInstalledBase(params);
		verify(repositoryMock).findAllByParameters(parametersCaptor.capture(), pageableCaptor.capture());

		assertThat(parametersCaptor.getValue()).usingRecursiveComparison().isEqualTo(params);
		assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(page - 1);
		assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(limit);
		assertThat(pageableCaptor.getValue().getSort()).isEqualTo(unsorted());
		assertThat(response.getMetaData().getCount()).isEqualTo(1);
		assertThat(response.getMetaData().getLimit()).isEqualTo(1);
		assertThat(response.getMetaData().getPage()).isEqualTo(page);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(2);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(2);
		assertThat(response.getInstalledBase()).isEqualTo(toInstalledBaseItems(List.of(entityMock)));
	}

	@Test
	void testForPageLargerThanResultsMaxPage() {
		final var params = InstalledBaseParameters.create();
		params.setPage(2);

		when(repositoryMock.findAllByParameters(ArgumentMatchers.<InstalledBaseParameters>any(), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getTotalPages()).thenReturn(1);
		when(pageMock.getTotalElements()).thenReturn(1L);
		when(pageMock.getNumber()).thenReturn(params.getPage() - 1);
		when(pageMock.getNumberOfElements()).thenReturn(0);
		when(pageMock.getSize()).thenReturn(params.getLimit());
		when(pageMock.getSort()).thenReturn(params.sort());

		final var response = service.getInstalledBase(params);

		verify(repositoryMock).findAllByParameters(ArgumentMatchers.<InstalledBaseParameters>any(), any(Pageable.class));

		assertThat(response.getMetaData().getCount()).isZero();
		assertThat(response.getMetaData().getLimit()).isEqualTo(100);
		assertThat(response.getMetaData().getPage()).isEqualTo(2);
		assertThat(response.getMetaData().getTotalPages()).isEqualTo(1);
		assertThat(response.getMetaData().getTotalRecords()).isEqualTo(1);
		assertThat(response.getInstalledBase()).isEmpty();
	}
}
