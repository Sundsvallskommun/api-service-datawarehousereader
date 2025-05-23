package se.sundsvall.datawarehousereader.integration.party;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static se.sundsvall.datawarehousereader.integration.party.configuration.PartyConfiguration.CLIENT_ID;

import generated.se.sundsvall.party.PartyType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.sundsvall.datawarehousereader.integration.party.configuration.PartyConfiguration;

@FeignClient(name = CLIENT_ID, url = "${integration.party.url}", configuration = PartyConfiguration.class)
@CircuitBreaker(name = CLIENT_ID)
public interface PartyClient {

	/**
	 * Get legal-ID by partyId (personId or organizationId).
	 *
	 * @param  partyType                            the type of party.
	 * @param  municipalityId                       the municipalityId of the party.
	 * @param  partyId                              the ID of the party. I.e. the personId or organizationId.
	 * @return                                      an optional string containing the legalId that corresponds to the
	 *                                              provided partyType and partyId if found.
	 * @throws org.zalando.problem.ThrowableProblem
	 */
	@GetMapping(path = "/{municipalityId}/{type}/{partyId}/legalId", produces = TEXT_PLAIN_VALUE)
	Optional<String> getLegalId(
		@PathVariable("type") PartyType partyType,
		@PathVariable("municipalityId") String municipalityId,
		@PathVariable("partyId") String partyId);

	/**
	 * Get party-ID by legalId (personalNumber or organizationNumber).
	 *
	 * @param  partyType                            the type of party.
	 * @param  municipalityId                       the municipalityId of the party.
	 * @param  legalId                              the legal-ID of the party. I.e. the personalNumber or
	 *                                              organizationNumber.
	 * @return                                      an optional string containing the partyId that corresponds to the
	 *                                              provided partyType and legalId if found.
	 * @throws org.zalando.problem.ThrowableProblem
	 */
	@GetMapping(path = "/{municipalityId}/{type}/{legalId}/partyId", produces = TEXT_PLAIN_VALUE)
	Optional<String> getPartyId(
		@PathVariable("type") PartyType partyType,
		@PathVariable("municipalityId") String municipalityId,
		@PathVariable("legalId") String legalId);
}
