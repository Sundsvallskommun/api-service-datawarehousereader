package se.sundsvall.datawarehousereader.service;

import static se.sundsvall.datawarehousereader.service.mapper.InstalledBaseMapper.toInstalledBaseItems;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseItem;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseParameters;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InstalledBaseRepository;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

@Service
public class InstalledBaseService {

	@Autowired
	private InstalledBaseRepository repository;

	public InstalledBaseResponse getInstalledBase(InstalledBaseParameters parameters) {
		final var matches = repository.findAllByParameters(parameters, PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));

		// If page larger than last page is requested, a empty list is returned otherwise the current page
		final List<InstalledBaseItem> installedBase = matches.getTotalPages() < parameters.getPage() ? Collections.emptyList() : toInstalledBaseItems(matches.getContent());

		return InstalledBaseResponse.create()
			.withMetaData(PagingAndSortingMetaData.create().withPageData(matches))
			.withInstalledBase(installedBase);
	}
}
