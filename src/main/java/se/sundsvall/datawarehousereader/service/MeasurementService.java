package se.sundsvall.datawarehousereader.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.Measurement;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement.MeasurementRepository;
import se.sundsvall.datawarehousereader.service.logic.PartyProvider;

@Service
public class MeasurementService {

	private static final String CATEGORY_NOT_IMPLEMENTED = "category '%s'";

	private final PartyProvider partyProvider;

	private final MeasurementRepository measurementRepository;

	MeasurementService(final PartyProvider partyProvider, MeasurementRepository measurementRepository) {
		this.partyProvider = partyProvider;
		this.measurementRepository = measurementRepository;
	}

	public List<Measurement> getMeasurements(final String municipalityId, final Category category, final Aggregation aggregateOn, final MeasurementParameters parameters) {
		final var legalId = Optional.ofNullable(parameters.getPartyId()).map(partyId -> partyProvider.translateToLegalId(municipalityId, partyId)).orElse(null);
		final var fromDateTime = Optional.ofNullable(parameters.getFromDateTime()).map(OffsetDateTime::toLocalDateTime).orElse(null);
		final var toDateTime = Optional.ofNullable(parameters.getToDateTime()).map(OffsetDateTime::toLocalDateTime).orElse(null);

		return switch (category) {
			case DISTRICT_HEATING -> List.of(); // work in progress
			case ELECTRICITY -> measurementRepository.getElectricityMeasurements(legalId, parameters.getFacilityId(), aggregateOn, fromDateTime, toDateTime);
			default -> throw Problem.valueOf(Status.NOT_IMPLEMENTED, String.format(CATEGORY_NOT_IMPLEMENTED, category));
		};
	}
}
