package se.sundsvall.datawarehousereader.service;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementResponse;
import se.sundsvall.datawarehousereader.service.logic.DistrictHeatingMeasurementProvider;
import se.sundsvall.datawarehousereader.service.logic.ElectricityMeasurementProvider;
import se.sundsvall.datawarehousereader.service.logic.PartyProvider;

@Service
public class MeasurementService {

	private static final String CATEGORY_NOT_IMPLEMENTED = "category '%s'";

	private final DistrictHeatingMeasurementProvider districtHeatingMeasurementProvider;

	private final ElectricityMeasurementProvider electricityMeasurementProvider;

	private final PartyProvider partyProvider;

	MeasurementService(final DistrictHeatingMeasurementProvider districtHeatingMeasurementProvider,
		final ElectricityMeasurementProvider electricityMeasurementProvider,
		final PartyProvider partyProvider) {
		this.districtHeatingMeasurementProvider = districtHeatingMeasurementProvider;
		this.electricityMeasurementProvider = electricityMeasurementProvider;
		this.partyProvider = partyProvider;
	}

	public MeasurementResponse getMeasurements(String municipalityId, Category category, Aggregation aggregateOn, MeasurementParameters parameters) {
		final var legalId = Optional.ofNullable(parameters.getPartyId()).map(partyId -> partyProvider.translateToLegalId(municipalityId, partyId)).orElse(null);
		final var fromDateTime = Optional.ofNullable(parameters.getFromDateTime()).map(OffsetDateTime::toLocalDateTime).orElse(null);
		final var toDateTime = Optional.ofNullable(parameters.getToDateTime()).map(OffsetDateTime::toLocalDateTime).orElse(null);

		return switch (category) {
			case DISTRICT_HEATING -> districtHeatingMeasurementProvider.getMeasurements(legalId, aggregateOn, fromDateTime, toDateTime, parameters);
			case ELECTRICITY -> electricityMeasurementProvider.getMeasurements(legalId, aggregateOn, fromDateTime, toDateTime, parameters);
			default -> throw Problem.valueOf(Status.NOT_IMPLEMENTED, String.format(CATEGORY_NOT_IMPLEMENTED, category));
		};
	}
}
