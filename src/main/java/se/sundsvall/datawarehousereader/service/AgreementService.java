package se.sundsvall.datawarehousereader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se.sundsvall.datawarehousereader.api.model.agreement.Agreement;
import se.sundsvall.datawarehousereader.api.model.agreement.AgreementParameters;
import se.sundsvall.datawarehousereader.api.model.agreement.AgreementResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.AgreementRepository;
import se.sundsvall.datawarehousereader.service.logic.PartyProvider;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static se.sundsvall.datawarehousereader.service.mapper.AgreementMapper.toAgreements;

@Service
public class AgreementService {

	@Autowired
	private AgreementRepository repository;

	@Autowired
	private PartyProvider partyProvider;

	public AgreementResponse getAgreements(AgreementParameters parameters) {
		final var matches = repository.findAllByParameters(parameters, getCustomerOrgId(parameters.getPartyId()), PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));

		// If page larger than last page is requested, a empty list is returned otherwise the current page
		final List<Agreement> agreements = matches.getTotalPages() < parameters.getPage() ? emptyList() : toAgreements(matches.getContent());


		return AgreementResponse.create()
			.withAgreements(agreements)
			.withMetaData(PagingAndSortingMetaData.create().withPageData(matches));
	}

	private String getCustomerOrgId(String partyId) {
		return ofNullable(partyId)
			.map(partyProvider::translateToLegalId)
			.orElse(null);
	}
}
