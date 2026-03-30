package se.sundsvall.datawarehousereader.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseItem;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseParameters;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InstalledBaseJdbcRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.InstalledBaseRepository;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import static se.sundsvall.datawarehousereader.service.mapper.InstalledBaseMapper.toInstalledBaseItems;

@Service
public class InstalledBaseService {

	private final InstalledBaseRepository repository;
	private final InstalledBaseJdbcRepository installedBaseJdbcRepository;

	InstalledBaseService(final InstalledBaseRepository repository, final InstalledBaseJdbcRepository installedBaseJdbcRepository) {
		this.repository = repository;
		this.installedBaseJdbcRepository = installedBaseJdbcRepository;
	}

	public InstalledBaseResponse getInstalledBase(final InstalledBaseParameters parameters) {
		final var matches = repository.findAllByParameters(parameters, PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));

		// If a page larger than the last page is requested, an empty list is returned otherwise the current page
		final List<InstalledBaseItem> installedBase = matches.getTotalPages() < parameters.getPage() ? Collections.emptyList() : toInstalledBaseItems(matches.getContent());

		return InstalledBaseResponse.create()
			.withMetaData(PagingAndSortingMetaData.create().withPageData(matches))
			.withInstalledBase(installedBase);
	}

	public InstalledBaseResponse getInstalledBase(final Integer pageNumber, final Integer pageSize,
		final String organizationIds, final LocalDate date, final String uuid, final String sortBy) {

		return installedBaseJdbcRepository.getInstalledBases(pageNumber, pageSize, organizationIds, date, uuid, sortBy);
	}
}
