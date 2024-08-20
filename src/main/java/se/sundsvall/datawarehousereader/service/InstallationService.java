package se.sundsvall.datawarehousereader.service;

import static java.util.Collections.emptyList;
import static se.sundsvall.datawarehousereader.service.mapper.InstallationMapper.toInstallationDetailsList;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import se.sundsvall.datawarehousereader.api.model.installation.InstallationDetails;
import se.sundsvall.datawarehousereader.api.model.installation.InstallationDetailsResponse;
import se.sundsvall.datawarehousereader.api.model.installation.InstallationParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InstallationRepository;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

@Service
public class InstallationService {

	private final InstallationRepository repository;

	InstallationService(final InstallationRepository repository) {
		this.repository = repository;
	}

	public InstallationDetailsResponse getInstallations(final InstallationParameters parameters) {
		final var matches = repository.findAllByParameters(parameters, PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));

		final List<InstallationDetails> installations = matches.getTotalPages() < parameters.getPage() ? emptyList() : toInstallationDetailsList(matches.getContent());

		return InstallationDetailsResponse.create()
			.withMetaData(PagingAndSortingMetaData.create().withPageData(matches))
			.withInstallationDetails(installations);
	}
}
