package se.sundsvall.datawarehousereader.integration.party.configuration;

import java.util.List;

import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import se.sundsvall.dept44.configuration.feign.FeignConfiguration;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;
import se.sundsvall.dept44.configuration.feign.decoder.ProblemErrorDecoder;

@Import(FeignConfiguration.class)
public class PartyConfiguration {

	public static final String CLIENT_REGISTRATION_ID = "party";

	@Bean
	FeignBuilderCustomizer feignBuilderCustomizer(ClientRegistrationRepository clientRepository, PartyProperties partyProperties) {
		return FeignMultiCustomizer.create()
			.withErrorDecoder(new ProblemErrorDecoder(CLIENT_REGISTRATION_ID, List.of(HttpStatus.NOT_FOUND.value())))
			.withRequestTimeoutsInSeconds(partyProperties.connectTimeout(), partyProperties.readTimeout())
			.withRetryableOAuth2InterceptorForClientRegistration(clientRepository.findByRegistrationId(CLIENT_REGISTRATION_ID))
			.composeCustomizersToOne();
	}
}
