package se.sundsvall.datawarehousereader.service.logic;

import org.springframework.beans.factory.annotation.Autowired;
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
import se.sundsvall.datawarehousereader.service.mapper.MeasurementMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.data.domain.PageRequest.of;
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

	public MeasurementResponse getMeasurements(String legalId, Aggregation aggregateOn, LocalDateTime fromDateTime, LocalDateTime toDateTime, MeasurementParameters parameters) {

		final var matches =  switch (aggregateOn) {
			case HOUR -> electricityHourRepository.findAllMatching(legalId, parameters.getFacilityId(), fromDateTime, toDateTime,
				of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));
			case DAY -> electricityDayRepositoryRepository.findAllMatching(legalId, parameters.getFacilityId(), fromDateTime, toDateTime,
				of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));
			case MONTH -> electricityMonthRepository.findAllMatching(legalId, parameters.getFacilityId(), fromDateTime, toDateTime,
				of(parameters.getPage() - 1, parameters.getLimit(), parameters.sort()));
			default -> throw Problem.valueOf(Status.NOT_IMPLEMENTED, String.format(AGGREGATION_NOT_IMPLEMENTED, aggregateOn, ELECTRICITY));
		};

		// If page larger than last page is requested, an empty list is returned otherwise the current page
		List<Measurement> measurements = matches.getTotalPages() < parameters.getPage() ? Collections.emptyList() : MeasurementMapper.toMeasurements(matches.getContent(), parameters, aggregateOn, ELECTRICITY);
		return toMeasurementResponse(parameters, matches.getTotalPages(), matches.getTotalElements(), measurements);
	}
}
