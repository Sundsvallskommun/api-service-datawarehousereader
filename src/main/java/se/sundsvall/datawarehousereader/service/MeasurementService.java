package se.sundsvall.datawarehousereader.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.MeasurementRepository;
import se.sundsvall.datawarehousereader.service.logic.PartyProvider;
import se.sundsvall.dept44.problem.Problem;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@Service
public class MeasurementService {

	private static final String CATEGORY_NOT_IMPLEMENTED = "category '%s'";

	private final PartyProvider partyProvider;

	private final MeasurementRepository measurementRepository;

	MeasurementService(final PartyProvider partyProvider,
		final MeasurementRepository measurementRepository) {
		this.partyProvider = partyProvider;
		this.measurementRepository = measurementRepository;
	}

	public List<Measurement> getMeasurements(final String municipalityId, final Category category, final Aggregation aggregateOn, final MeasurementParameters parameters) {
		final var legalId = Optional.ofNullable(parameters.getPartyId()).map(partyId -> partyProvider.translateToLegalId(municipalityId, partyId)).orElse(null);
		final var fromDateTime = Optional.ofNullable(parameters.getFromDateTime()).orElse(null);
		final var toDateTime = Optional.ofNullable(parameters.getToDateTime()).orElse(null);
		final var facilityIds = Optional.ofNullable(parameters.getFacilityIds()).map(ids -> String.join(",", ids)).orElse(null);
		final var display = Optional.ofNullable(parameters.getDisplay()).map(d -> d.name().toLowerCase()).orElse(null);

		return switch (category) {
			case DISTRICT_COOLING -> measurementRepository.getDistrictCoolingMeasurements(legalId, facilityIds, aggregateOn, fromDateTime, toDateTime, display);
			case DISTRICT_HEATING -> measurementRepository.getDistrictHeatingMeasurements(legalId, facilityIds, aggregateOn, fromDateTime, toDateTime, display);
			case ELECTRICITY -> measurementRepository.getElectricityMeasurements(legalId, facilityIds, aggregateOn, fromDateTime, toDateTime, display);
			default -> throw Problem.valueOf(NOT_IMPLEMENTED, String.format(CATEGORY_NOT_IMPLEMENTED, category));
		};
	}
}
