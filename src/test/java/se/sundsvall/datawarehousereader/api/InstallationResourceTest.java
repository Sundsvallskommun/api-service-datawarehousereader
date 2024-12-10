package se.sundsvall.datawarehousereader.api;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.installation.InstallationDetailsResponse;
import se.sundsvall.datawarehousereader.api.model.installation.InstallationParameters;
import se.sundsvall.datawarehousereader.service.InstallationService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class InstallationResourceTest {

	private static final String PATH = "/2281/installations";

	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_LIMIT = 100;
	private static final int PAGE = 6;
	private static final int LIMIT = 35;

	private static final Boolean INSTALLED = false;
	private static final LocalDate DATE_FROM = LocalDate.parse("2020-01-01");
	private static final LocalDate DATE_TO = LocalDate.parse("2023-01-01");
	private static final String CATEGORY = "ELECTRICITY";
	private static final String FACILITY_ID = "facilityId";

	@MockitoBean
	private InstallationService serviceMock;

	@Captor
	private ArgumentCaptor<InstallationParameters> parametersCaptor;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getInstallationDetailsAllParameters() {
		when(serviceMock.getInstallations(any())).thenReturn(InstallationDetailsResponse.create());

		webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParams(createParameterMap(PAGE, LIMIT, CATEGORY, INSTALLED, FACILITY_ID, DATE_FROM, DATE_TO))
			.build())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(InstallationDetailsResponse.class)
			.isEqualTo(InstallationDetailsResponse.create());

		verify(serviceMock).getInstallations(parametersCaptor.capture());
		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getLimit()).isEqualTo(LIMIT);
		assertThat(parameters.getPage()).isEqualTo(PAGE);
		assertThat(parameters.getFacilityId()).isEqualTo(FACILITY_ID);
		assertThat(parameters.getCategory()).isEqualTo(Category.ELECTRICITY);
		assertThat(parameters.getInstalled()).isEqualTo(INSTALLED);
		assertThat(parameters.getLastModifiedDateFrom()).isEqualTo(DATE_FROM);
		assertThat(parameters.getLastModifiedDateTo()).isEqualTo(DATE_TO);
	}

	@Test
	void getInstalledBaseDefaultValues() {
		when(serviceMock.getInstallations(any())).thenReturn(InstallationDetailsResponse.create());

		webTestClient.get().uri(uriBuilder -> uriBuilder.path(PATH)
			.queryParams(createParameterMap(null, null, CATEGORY, INSTALLED, FACILITY_ID, DATE_FROM, DATE_TO))
			.build())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(InstallationDetailsResponse.class)
			.isEqualTo(InstallationDetailsResponse.create());

		verify(serviceMock).getInstallations(parametersCaptor.capture());
		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getLimit()).isEqualTo(DEFAULT_LIMIT);
		assertThat(parameters.getPage()).isEqualTo(DEFAULT_PAGE);
		assertThat(parameters.getFacilityId()).isEqualTo(FACILITY_ID);
		assertThat(parameters.getCategory()).isEqualTo(Category.ELECTRICITY);
		assertThat(parameters.getInstalled()).isEqualTo(INSTALLED);
		assertThat(parameters.getLastModifiedDateFrom()).isEqualTo(DATE_FROM);
		assertThat(parameters.getLastModifiedDateTo()).isEqualTo(DATE_TO);
	}

	@Test
	void getInstalledBaseNoParams() {
		when(serviceMock.getInstallations(any())).thenReturn(InstallationDetailsResponse.create());

		webTestClient.get().uri(PATH)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(InstallationDetailsResponse.class)
			.isEqualTo(InstallationDetailsResponse.create());

		verify(serviceMock).getInstallations(parametersCaptor.capture());
		final var parameters = parametersCaptor.getValue();
		assertThat(parameters.getPage()).isEqualTo(DEFAULT_PAGE);
		assertThat(parameters.getLimit()).isEqualTo(DEFAULT_LIMIT);
		assertThat(parameters.getFacilityId()).isNull();
		assertThat(parameters.getCategory()).isNull();
		assertThat(parameters.getInstalled()).isNull();
		assertThat(parameters.getLastModifiedDateTo()).isNull();
		assertThat(parameters.getLastModifiedDateFrom()).isNull();
	}

	private MultiValueMap<String, String> createParameterMap(final Integer page, final Integer limit,
		final String category, final Boolean installed, final String facilityId, final LocalDate dateFrom, final LocalDate dateTo) {
		final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

		ofNullable(page).ifPresent(p -> parameters.add("page", p.toString()));
		ofNullable(limit).ifPresent(p -> parameters.add("limit", p.toString()));
		ofNullable(category).ifPresent(p -> parameters.add("category", p));
		ofNullable(installed).ifPresent(p -> parameters.add("installed", String.valueOf(p)));
		ofNullable(facilityId).ifPresent(p -> parameters.add("facilityId", p));
		ofNullable(dateFrom).ifPresent(p -> parameters.add("lastModifiedDateFrom", String.valueOf(p)));
		ofNullable(dateTo).ifPresent(p -> parameters.add("lastModifiedDateTo", String.valueOf(p)));

		return parameters;
	}
}
