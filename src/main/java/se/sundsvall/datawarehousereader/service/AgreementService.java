package se.sundsvall.datawarehousereader.service;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static se.sundsvall.datawarehousereader.service.mapper.AgreementMapper.toAgreements;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se.sundsvall.datawarehousereader.api.model.agreement.Agreement;
import se.sundsvall.datawarehousereader.api.model.agreement.AgreementParameters;
import se.sundsvall.datawarehousereader.api.model.agreement.AgreementResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.AgreementRepository;
import se.sundsvall.datawarehousereader.service.logic.PartyProvider;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

@Service
public class AgreementService {

	private final AgreementRepository repository;

	private final PartyProvider partyProvider;

	AgreementService(final AgreementRepository repository, final PartyProvider partyProvider) {
		this.repository = repository;
		this.partyProvider = partyProvider;
	}

	public AgreementResponse getAgreements(String municipalityId, AgreementParameters parameters) {
		final var matches = repository.findAllByParameters(parameters, getCustomerOrgId(municipalityId, parameters.getPartyId()), PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));

		// If page larger than last page is requested, an empty list is returned otherwise the current page
		final List<Agreement> agreements = matches.getTotalPages() < parameters.getPage() ? emptyList() : toAgreements(matches.getContent());

		return AgreementResponse.create()
			.withAgreements(agreements)
			.withMetaData(PagingAndSortingMetaData.create().withPageData(matches));
	}

	private String getCustomerOrgId(String municipalityId, String partyId) {
		return ofNullable(partyId)
			.map(p -> partyProvider.translateToLegalId(municipalityId, p))
			.orElse(null);
	}
}
