package se.sundsvall.datawarehousereader.service.logic;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementResponse;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementElectricityDayRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementElectricityHourRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementElectricityMonthRepository;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementElectricityHourEntity;
import se.sundsvall.datawarehousereader.service.mapper.MeasurementMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;
import static se.sundsvall.datawarehousereader.api.model.Category.ELECTRICITY;
import static se.sundsvall.datawarehousereader.service.mapper.MeasurementMapper.toMeasurementResponse;

@Component
public class ElectricityMeasurementProvider {

	@Autowired
	private MeasurementElectricityHourRepository electricityHourRepository;

	@Autowired
	private MeasurementElectricityDayRepository electricityDayRepositoryRepository;

	@Autowired
	private MeasurementElectricityMonthRepository electricityMonthRepository;

	private static final String AGGREGATION_NOT_IMPLEMENTED = "aggregation '%s' and category '%s'";
	private static final String MAXIMUM_HOURLY_MEASUREMENT_RANGE_VIOLATION = "Date range exceeds maximum range. Range can max be one year when asking for hourly electricity measurements.";

	@Transactional
	public MeasurementResponse getMeasurements(String legalId, Aggregation aggregateOn, LocalDateTime fromDateTime, LocalDateTime toDateTime, MeasurementParameters parameters) {

		final var pagedMatches = switch (aggregateOn) {
			case HOUR -> handleHourMeasurementRequest(legalId, fromDateTime, toDateTime, parameters);
			case DAY -> electricityDayRepositoryRepository.findAllMatching(legalId, parameters.getFacilityId(), fromDateTime, toDateTime, toPageRequest(parameters));
			case MONTH -> electricityMonthRepository.findAllMatching(legalId, parameters.getFacilityId(), fromDateTime, toDateTime, toPageRequest(parameters));
			default -> throw Problem.valueOf(Status.NOT_IMPLEMENTED, String.format(AGGREGATION_NOT_IMPLEMENTED, aggregateOn, ELECTRICITY));
		};

		// If requested page is larger than last page, an empty list is returned otherwise the requested page
		List<Measurement> measurements = pagedMatches.getTotalPages() < parameters.getPage() ? Collections.emptyList() :
			MeasurementMapper.toMeasurements(pagedMatches.getContent(), parameters, aggregateOn, ELECTRICITY);

		return toMeasurementResponse(parameters, pagedMatches.getTotalPages(), pagedMatches.getTotalElements(), measurements);
	}

	private Page<MeasurementElectricityHourEntity> handleHourMeasurementRequest(String legalId, LocalDateTime fromDateTime, LocalDateTime toDateTime, MeasurementParameters parameters) {
		// Throw exception if requested period is larger than a year
		if (ofNullable(fromDateTime).orElse(LocalDateTime.MIN).plusYears(1).isBefore(ofNullable(toDateTime).orElse(LocalDateTime.MAX))) {
			throw Problem.valueOf(Status.BAD_REQUEST, MAXIMUM_HOURLY_MEASUREMENT_RANGE_VIOLATION);
		}

		return toPage(parameters, electricityHourRepository.findAllMatching(legalId, parameters.getFacilityId(), fromDateTime, toDateTime));
	}

	/**
	 * Method for converting result list into a Page object with sub list for requested page. Convertion must be done
	 * explicitly as stored procedures can not produce a return object of type Page and cant sort result list.
	 * 
	 * @param parameters object containing input for calculating the current requested sub page for the result list
	 * @param matches    with result to be converted to a paged list
	 * @return a Page object representing the sublist for the requested page of the list
	 */
	private Page<MeasurementElectricityHourEntity> toPage(MeasurementParameters parameters, List<MeasurementElectricityHourEntity> matches) {

		// Sort result list based on incoming sorting parameters
		matches.sort(MeasurementElectricityHourEntityComparator.create(parameters.getSortBy(), parameters.getSortDirection()));

		// Convert list into a list of pages
		PagedListHolder<MeasurementElectricityHourEntity> page = toPagedListHolder(parameters, matches);

		if (page.getPageCount() < parameters.getPage()) {
			return new PageImpl<>(Collections.emptyList(), toPageRequest(parameters), page.getNrOfElements());
		}
		return new PageImpl<>(page.getPageList(), PageRequest.of(page.getPage(), page.getPageSize(), parameters.sort()), page.getNrOfElements());
	}

	private PagedListHolder<MeasurementElectricityHourEntity> toPagedListHolder(MeasurementParameters parameters, List<MeasurementElectricityHourEntity> matches) {
		PagedListHolder<MeasurementElectricityHourEntity> page = new PagedListHolder<>(matches);
		page.setPage(parameters.getPage() - 1);
		page.setPageSize(parameters.getLimit());
		return page;
	}

	private PageRequest toPageRequest(MeasurementParameters parameters) {
		return PageRequest.of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort());
	}
}
