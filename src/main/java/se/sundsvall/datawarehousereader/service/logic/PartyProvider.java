package se.sundsvall.datawarehousereader.service.logic;

import generated.se.sundsvall.party.PartyType;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.datawarehousereader.integration.party.PartyIntegration;

import static generated.se.sundsvall.party.PartyType.ENTERPRISE;
import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static java.lang.String.format;
import static java.util.Optional.empty;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static org.zalando.problem.Status.NOT_FOUND;

@Component
public class PartyProvider {

	private final PartyIntegration partyIntegration;

	PartyProvider(PartyIntegration partyIntegration) {
		this.partyIntegration = partyIntegration;
	}

	public String translateToLegalId(String municipalityId, String partyId) {
		return getLegalId(PRIVATE, municipalityId, partyId)
			.or(() -> getLegalId(ENTERPRISE, municipalityId, partyId))
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND, format("PartyId '%s' could not be found as a private customer or an enterprise customer", partyId)));
	}

	public String translateToPartyId(PartyType partyType, String municipalityId, String legalId) {
		return getPartyId(partyType, municipalityId, legalId)
			.orElseThrow(() -> Problem.valueOf(INTERNAL_SERVER_ERROR, "Could not determine partyId for customer connected to returned data"));
	}

	private Optional<String> getLegalId(PartyType partyType, String municipalityId, String partyId) {
		try {
			return partyIntegration.getLegalId(partyType, municipalityId, partyId);
		} catch (final ThrowableProblem e) {
			if (e.getStatus() == NOT_FOUND) {
				return empty();
			}
			throw e;
		}
	}

	private Optional<String> getPartyId(PartyType partyType, String municipalityId, String legalId) {
		try {
			return partyIntegration.getPartyId(partyType, municipalityId, legalId);
		} catch (final ThrowableProblem e) {
			if (e.getStatus() == NOT_FOUND) {
				return empty();
			}
			throw e;
		}
	}
}
