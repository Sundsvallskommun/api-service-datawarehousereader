package se.sundsvall.datawarehousereader.integration.party;

import generated.se.sundsvall.party.PartyType;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Facade to the client class as cachable annotations only should be applied to concrete classes
 */
@Component
public class PartyIntegration {

	private final PartyClient partyClient;

	PartyIntegration(PartyClient partyClient) {
		this.partyClient = partyClient;
	}

	@Cacheable("legalIds")
	public Optional<String> getLegalId(PartyType partyType, String municipalityId, String partyId) {
		return partyClient.getLegalId(partyType, municipalityId, partyId);
	}

	@Cacheable("partyIds")
	public Optional<String> getPartyId(PartyType partyType, String municipalityId, String legalId) {
		return partyClient.getPartyId(partyType, municipalityId, legalId);
	}
}
