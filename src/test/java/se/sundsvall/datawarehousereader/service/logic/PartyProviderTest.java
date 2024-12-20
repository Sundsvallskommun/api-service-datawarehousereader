package se.sundsvall.datawarehousereader.service.logic;

import static generated.se.sundsvall.party.PartyType.ENTERPRISE;
import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static org.zalando.problem.Status.NOT_FOUND;

import generated.se.sundsvall.party.PartyType;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.datawarehousereader.integration.party.PartyClient;

@ExtendWith(MockitoExtension.class)
class PartyProviderTest {

	private static final String LEGAL_ID = "legalId";
	private static final String MUNICIPALITY_ID = "municipalityId";
	private static final String UUID = randomUUID().toString();

	@Mock
	private PartyClient partyClientMock;

	@InjectMocks
	private PartyProvider provider;

	@ParameterizedTest
	@EnumSource(PartyType.class)
	@MockitoSettings(strictness = LENIENT)
	void translateToLegalId(PartyType partyType) {
		when(partyClientMock.getLegalId(partyType, MUNICIPALITY_ID, UUID)).thenReturn(Optional.of(LEGAL_ID));

		assertThat(provider.translateToLegalId(MUNICIPALITY_ID, UUID)).isEqualTo(LEGAL_ID);

		switch (partyType) {
			case PRIVATE -> {
				verify(partyClientMock).getLegalId(PRIVATE, MUNICIPALITY_ID, UUID);
				verify(partyClientMock, never()).getLegalId(ENTERPRISE, MUNICIPALITY_ID, UUID);
			}
			case ENTERPRISE -> {
				verify(partyClientMock).getLegalId(PRIVATE, MUNICIPALITY_ID, UUID);
				verify(partyClientMock).getLegalId(ENTERPRISE, MUNICIPALITY_ID, UUID);
			}
		}
	}

	@Test
	void translateToLegalIdWhenPartyIdNotFound() {
		when(partyClientMock.getLegalId(PRIVATE, MUNICIPALITY_ID, UUID)).thenThrow(Problem.valueOf(NOT_FOUND, "Not Found"));
		when(partyClientMock.getLegalId(ENTERPRISE, MUNICIPALITY_ID, UUID)).thenThrow(Problem.valueOf(NOT_FOUND, "Not Found"));

		final var exception = assertThrows(ThrowableProblem.class, () -> provider.translateToLegalId(MUNICIPALITY_ID, UUID));

		assertThat(exception.getStatus()).isEqualTo(NOT_FOUND);
		assertThat(exception.getMessage()).isEqualTo(String.format("Not Found: PartyId '%s' could not be found as a private customer or an enterprise customer", UUID));

		verify(partyClientMock).getLegalId(PRIVATE, MUNICIPALITY_ID, UUID);
		verify(partyClientMock).getLegalId(ENTERPRISE, MUNICIPALITY_ID, UUID);
	}

	@Test
	void translateToLegalIdWhenPartyIdNotFoundForPrivate() {
		when(partyClientMock.getLegalId(PRIVATE, MUNICIPALITY_ID, UUID)).thenThrow(Problem.valueOf(NOT_FOUND, "Not Found"));
		when(partyClientMock.getLegalId(ENTERPRISE, MUNICIPALITY_ID, UUID)).thenReturn(Optional.of(LEGAL_ID));

		assertThat(provider.translateToLegalId(MUNICIPALITY_ID, UUID)).isEqualTo(LEGAL_ID);

		verify(partyClientMock).getLegalId(PRIVATE, MUNICIPALITY_ID, UUID);
		verify(partyClientMock).getLegalId(ENTERPRISE, MUNICIPALITY_ID, UUID);
	}

	@ParameterizedTest
	@EnumSource(PartyType.class)
	void translateToPartyId(PartyType partyType) {
		when(partyClientMock.getPartyId(partyType, MUNICIPALITY_ID, LEGAL_ID)).thenReturn(Optional.of(UUID));

		assertThat(provider.translateToPartyId(partyType, MUNICIPALITY_ID, LEGAL_ID)).isEqualTo(UUID);

		verify(partyClientMock).getPartyId(partyType, MUNICIPALITY_ID, LEGAL_ID);
	}

	@ParameterizedTest
	@EnumSource(PartyType.class)
	void translateToPartyIdWhenLegalIdNotFound(PartyType partyType) {
		final var exception = assertThrows(ThrowableProblem.class, () -> provider.translateToPartyId(partyType, MUNICIPALITY_ID, LEGAL_ID));

		assertThat(exception.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
		assertThat(exception.getMessage()).isEqualTo("Internal Server Error: Could not determine partyId for customer connected to returned data");

		verify(partyClientMock).getPartyId(partyType, MUNICIPALITY_ID, LEGAL_ID);
	}

	@ParameterizedTest
	@EnumSource(PartyType.class)
	void translateToPartyIdWhen404FromParty(PartyType partyType) {
		when(partyClientMock.getPartyId(partyType, MUNICIPALITY_ID, LEGAL_ID)).thenThrow(Problem.valueOf(NOT_FOUND, "Not Found"));

		final var exception = assertThrows(ThrowableProblem.class, () -> provider.translateToPartyId(partyType, MUNICIPALITY_ID, LEGAL_ID));

		assertThat(exception.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
		assertThat(exception.getMessage()).isEqualTo("Internal Server Error: Could not determine partyId for customer connected to returned data");
		verify(partyClientMock).getPartyId(partyType, MUNICIPALITY_ID, LEGAL_ID);
	}
}
