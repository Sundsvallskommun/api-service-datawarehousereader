package se.sundsvall.datawarehousereader.integration.party;

import generated.se.sundsvall.party.PartyType;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.annotation.Cacheable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartyIntegrationTest {

	@Mock
	private PartyClient partyClientMock;

	@InjectMocks
	private PartyIntegration integration;

	@AfterEach
	void verifyNoMoreMockInterations() {
		verifyNoMoreInteractions(partyClientMock);
	}

	@Test
	void testCacheableAnnotationExistsOnLegalIdMethod() throws Exception {
		assertThat(PartyIntegration.class.getMethod("getLegalId", PartyType.class, String.class, String.class).getAnnotation(Cacheable.class).value()).containsExactly("legalIds");
	}

	@Test
	void testLegalIdWhenFound() {
		// Arrange
		final var municipalityId = "municipalityId";
		final var partyId = "partyId";
		final var legalId = "legalId";

		when(partyClientMock.getLegalId(any(), any(), any())).thenReturn(Optional.of(legalId));

		// Act
		final var result = integration.getLegalId(PartyType.ENTERPRISE, municipalityId, partyId);

		// Assert and verify
		assertThat(result).isPresent().hasValue(legalId);
		verify(partyClientMock).getLegalId(PartyType.ENTERPRISE, municipalityId, partyId);
	}

	@Test
	void testLegalIdWhenNotFound() {
		// Arrange
		final var municipalityId = "municipalityId";
		final var partyId = "partyId";

		// Act
		final var result = integration.getLegalId(PartyType.PRIVATE, municipalityId, partyId);

		// Assert and verify
		assertThat(result).isEmpty();
		verify(partyClientMock).getLegalId(PartyType.PRIVATE, municipalityId, partyId);
	}

	@Test
	void testCacheableAnnotationExistsOnPartyIdMethod() throws Exception {
		assertThat(PartyIntegration.class.getMethod("getPartyId", PartyType.class, String.class, String.class).getAnnotation(Cacheable.class).value()).containsExactly("partyIds");
	}

	@Test
	void testPartyIdWhenFound() {
		// Arrange
		final var municipalityId = "municipalityId";
		final var legalId = "legalId";
		final var partyId = "partyId";

		when(partyClientMock.getPartyId(any(), any(), any())).thenReturn(Optional.of(partyId));

		// Act
		final var result = integration.getPartyId(PartyType.PRIVATE, municipalityId, legalId);

		// Assert and verify
		assertThat(result).isPresent().hasValue(partyId);
		verify(partyClientMock).getPartyId(PartyType.PRIVATE, municipalityId, legalId);
	}

	@Test
	void testPartyIdWhenNotFound() {
		// Arrange
		final var municipalityId = "municipalityId";
		final var legalId = "legalId";

		// Act
		final var result = integration.getPartyId(PartyType.ENTERPRISE, municipalityId, legalId);

		// Assert and verify
		assertThat(result).isEmpty();
		verify(partyClientMock).getPartyId(PartyType.ENTERPRISE, municipalityId, legalId);
	}
}
