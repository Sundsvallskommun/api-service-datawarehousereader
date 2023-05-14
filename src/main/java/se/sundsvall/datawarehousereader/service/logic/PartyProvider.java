package se.sundsvall.datawarehousereader.service.logic;

import generated.se.sundsvall.party.PartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.datawarehousereader.integration.party.PartyClient;

import java.util.Objects;
import java.util.Optional;

import static generated.se.sundsvall.party.PartyType.ENTERPRISE;
import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static java.lang.String.format;
import static org.zalando.problem.Problem.valueOf;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static org.zalando.problem.Status.NOT_FOUND;

@Component
public class PartyProvider {

	@Autowired
	private PartyClient partyClient;

	public String translateToLegalId(String partyId) {
		return getLegalId(PRIVATE, partyId)
			.or(() -> getLegalId(ENTERPRISE, partyId))
			.orElseThrow(() -> valueOf(NOT_FOUND, format("PartyId '%s' could not be found as a private customer or an enterprise customer", partyId)));
	}
	
	public String translateToPartyId(PartyType partyType, String legalId) {
		return getPartyId(partyType, legalId)
			.orElseThrow(() -> valueOf(INTERNAL_SERVER_ERROR, "Could not determine partyId for customer connected to returned data"));
	}

	private Optional<String> getLegalId(PartyType partyType, String partyId) {
		try {
			return partyClient.getLegalId(partyType, partyId);
		} catch (ThrowableProblem e) {
			if (e.getStatus() == NOT_FOUND) {
				return Optional.empty();
			}
			throw e;
		}
	}

	private Optional<String> getPartyId(PartyType partyType, String legalId) {
		try {
			return partyClient.getPartyId(partyType, legalId);
		} catch (ThrowableProblem e) {
			if (Objects.equals(e.getStatus(), NOT_FOUND)) {
				return Optional.empty();
			}
			throw e;
		}
	}
}
