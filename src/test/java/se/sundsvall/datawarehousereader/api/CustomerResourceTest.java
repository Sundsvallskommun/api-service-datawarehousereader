package se.sundsvall.datawarehousereader.api;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import se.sundsvall.datawarehousereader.Application;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsParameters;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsResponse;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagementParameters;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagementResponse;
import se.sundsvall.datawarehousereader.service.CustomerService;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class CustomerResourceTest {

	private static final OffsetDateTime START_DATE_TIME = OffsetDateTime.now();
	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_LIMIT = 100;
	private static final int PAGE = 12;
	private static final int LIMIT = 50;
	private static final String CUSTOMER_NUMBER = "123";
	private static final String PARTY_ID = UUID.randomUUID().toString();
	private static final String ORGANIZATION_NUMBER = "organizationNumber";
	private static final String ORGANIZATION_NAME = "organizationName";

	@MockBean
	private CustomerService serviceMock;

	@Captor
	private ArgumentCaptor<CustomerEngagementParameters> parametersCaptor;

	@Captor
	private ArgumentCaptor<CustomerDetailsParameters> customerDetailsParametersArgumentCaptor;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getCustomerEngagementsAllParams() {
		when(serviceMock.getCustomerEngagements(any())).thenReturn(CustomerEngagementResponse.create());

		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/customer/engagements")
			.queryParams(createParameterMap(PAGE, LIMIT, CUSTOMER_NUMBER, PARTY_ID, ORGANIZATION_NUMBER, ORGANIZATION_NAME))
			.build())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(CustomerEngagementResponse.class)
			.isEqualTo(CustomerEngagementResponse.create());

		verify(serviceMock).getCustomerEngagements(parametersCaptor.capture());
		CustomerEngagementParameters parameters = parametersCaptor.getValue();
		assertThat(parameters.getCustomerNumber()).isEqualTo(CUSTOMER_NUMBER);
		assertThat(parameters.getPartyId()).isEqualTo(List.of(PARTY_ID));
		assertThat(parameters.getLimit()).isEqualTo(LIMIT);
		assertThat(parameters.getOrganizationNumber()).isEqualTo(ORGANIZATION_NUMBER);
		assertThat(parameters.getOrganizationName()).isEqualTo(ORGANIZATION_NAME);
		assertThat(parameters.getPage()).isEqualTo(PAGE);
	}

	@Test
	void getCustomerEngagementsDefaultValues() {
		when(serviceMock.getCustomerEngagements(any())).thenReturn(CustomerEngagementResponse.create());

		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/customer/engagements")
			.queryParams(createParameterMap(null, null, CUSTOMER_NUMBER, PARTY_ID, ORGANIZATION_NUMBER, ORGANIZATION_NAME))
			.build())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(CustomerEngagementResponse.class)
			.isEqualTo(CustomerEngagementResponse.create());

		verify(serviceMock).getCustomerEngagements(parametersCaptor.capture());
		CustomerEngagementParameters parameters = parametersCaptor.getValue();
		assertThat(parameters.getCustomerNumber()).isEqualTo(CUSTOMER_NUMBER);
		assertThat(parameters.getPartyId()).isEqualTo(List.of(PARTY_ID));
		assertThat(parameters.getLimit()).isEqualTo(DEFAULT_LIMIT);
		assertThat(parameters.getOrganizationNumber()).isEqualTo(ORGANIZATION_NUMBER);
		assertThat(parameters.getOrganizationName()).isEqualTo(ORGANIZATION_NAME);
		assertThat(parameters.getPage()).isEqualTo(DEFAULT_PAGE);
	}

	@Test
	void getCustomerEngagementsNoParams() {
		when(serviceMock.getCustomerEngagements(any())).thenReturn(CustomerEngagementResponse.create());

		webTestClient.get()
			.uri("/customer/engagements")
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(CustomerEngagementResponse.class)
			.isEqualTo(CustomerEngagementResponse.create());

		verify(serviceMock).getCustomerEngagements(parametersCaptor.capture());
		CustomerEngagementParameters parameters = parametersCaptor.getValue();
		assertThat(parameters.getCustomerNumber()).isNull();
		assertThat(parameters.getPartyId()).isNull();
		assertThat(parameters.getLimit()).isEqualTo(DEFAULT_LIMIT);
		assertThat(parameters.getOrganizationNumber()).isNull();
		assertThat(parameters.getOrganizationName()).isNull();
		assertThat(parameters.getPage()).isEqualTo(DEFAULT_PAGE);
	}

	private MultiValueMap<String, String> createParameterMap(Integer page, Integer limit, String customerNumber, String customerOrgId, String organizationId, String organizationName) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

		ofNullable(page).ifPresent(p -> parameters.add("page", p.toString()));
		ofNullable(limit).ifPresent(p -> parameters.add("limit", p.toString()));
		ofNullable(customerNumber).ifPresent(p -> parameters.add("customerNumber", p));
		ofNullable(customerOrgId).ifPresent(p -> parameters.add("partyId", p));
		ofNullable(organizationId).ifPresent(p -> parameters.add("organizationNumber", p));
		ofNullable(organizationName).ifPresent(p -> parameters.add("organizationName", p));

		return parameters;
	}

	@Test
	void getCustomerDetailsAllParams(){
		when(serviceMock.getCustomerDetails(any())).thenReturn(CustomerDetailsResponse.create());
		MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap<>();
		parameterMap.add("fromDateTime", START_DATE_TIME.format(DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())));
		parameterMap.add("page", String.valueOf(PAGE));
		parameterMap.add("limit", String.valueOf(LIMIT));
		parameterMap.add("partyId", PARTY_ID);

		webTestClient.get().uri(uriBuilder -> uriBuilder.path("/customer/details")
				.queryParams(parameterMap)
				.build())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(CustomerDetailsResponse.class)
			.isEqualTo(CustomerDetailsResponse.create());

		verify(serviceMock).getCustomerDetails(customerDetailsParametersArgumentCaptor.capture());
		CustomerDetailsParameters parameters = customerDetailsParametersArgumentCaptor.getValue();
		assertThat(parameters.getFromDateTime()).isEqualTo(START_DATE_TIME);
		assertThat(parameters.getPartyId()).isEqualTo(List.of(PARTY_ID));
		assertThat(parameters.getLimit()).isEqualTo(LIMIT);
		assertThat(parameters.getPage()).isEqualTo(PAGE);
	}

	@Test
	void getCustomerDetailsNoParams() {
		when(serviceMock.getCustomerDetails(any())).thenReturn(CustomerDetailsResponse.create());

		webTestClient.get()
			.uri("/customer/details")
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(CustomerDetailsResponse.class)
			.isEqualTo(CustomerDetailsResponse.create());

		verify(serviceMock).getCustomerDetails(customerDetailsParametersArgumentCaptor.capture());
		CustomerDetailsParameters parameters = customerDetailsParametersArgumentCaptor.getValue();
		assertThat(parameters.getPartyId()).isNull();
		assertThat(parameters.getFromDateTime()).isNull();
		assertThat(parameters.getLimit()).isEqualTo(DEFAULT_LIMIT);
		assertThat(parameters.getPage()).isEqualTo(DEFAULT_PAGE);
	}
}
