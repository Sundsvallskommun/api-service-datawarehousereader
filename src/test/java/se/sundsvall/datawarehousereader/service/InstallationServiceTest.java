package se.sundsvall.datawarehousereader.service;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import se.sundsvall.datawarehousereader.api.model.installation.InstallationParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InstallationRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstallationServiceTest {

	@Mock
	private InstallationRepository repositoryMock;

	@InjectMocks
	private InstallationService service;

	@Test
	void getInstallations() {
		final var parameters = InstallationParameters.create();
		parameters.setPage(1);
		parameters.setLimit(10);
		parameters.setSortBy(List.of("id"));
		final var pageable = PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort());
		final var installation = InstallationEntity.create();
		final var installationPage = new PageImpl<>(List.of(installation), pageable, 1L);

		when(repositoryMock.findAllByParameters(parameters, pageable)).thenReturn(installationPage);

		final var result = service.getInstallations(parameters);

		assertThat(result).isNotNull();
		assertThat(result.getMetaData()).satisfies(metaData -> {
			assertThat(metaData.getPage()).isEqualTo(parameters.getPage());
			assertThat(metaData.getSortBy()).isEqualTo(parameters.getSortBy());
			assertThat(metaData.getLimit()).isEqualTo(parameters.getLimit());
		});
		assertThat(result.getInstallationDetails().getFirst()).satisfies(i -> {
			assertThat(i.getCity()).isEqualTo(installation.getCity());
			assertThat(i.getFacilityId()).isEqualTo(installation.getFacilityId());
			assertThat(i.getCompany()).isEqualTo(installation.getCompany());
			assertThat(i.getType()).isEqualTo(installation.getType());
			assertThat(i.getStreet()).isEqualTo(installation.getStreet());
			assertThat(i.getDateFrom()).isEqualTo(installation.getDateFrom());
			assertThat(i.getDateTo()).isEqualTo(installation.getDateTo());
			assertThat(i.getDateLastModified()).isEqualTo(installation.getLastChangedDate());
			assertThat(i.getPostCode()).isEqualTo(installation.getPostCode());
		});

		verify(repositoryMock).findAllByParameters(parameters, pageable);
		verifyNoMoreInteractions(repositoryMock);
	}

	@Test
	void getInstallationsWhenEmpty() {
		when(repositoryMock.findAllByParameters(any(), any())).thenReturn(Page.empty());

		final var result = service.getInstallations(InstallationParameters.create());

		assertThat(result).isNotNull();
		assertThat(result.getMetaData()).satisfies(metaData -> {
			assertThat(metaData.getPage()).isEqualTo(1);
			assertThat(metaData.getSortBy()).isNull();
			assertThat(metaData.getLimit()).isZero();
		});
		assertThat(result.getInstallationDetails()).isEmpty();
		verify(repositoryMock).findAllByParameters(any(), any());
		verifyNoMoreInteractions(repositoryMock);
	}
}
