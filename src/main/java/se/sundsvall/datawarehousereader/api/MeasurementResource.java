package se.sundsvall.datawarehousereader.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.api.model.measurement.Aggregation;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementParameters;
import se.sundsvall.datawarehousereader.api.model.measurement.MeasurementResponse;
import se.sundsvall.datawarehousereader.service.MeasurementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Validated
@RequestMapping("/measurements")
@Tag(name = "Measurement", description = "Measurement operations")
class MeasurementResource {

	private final MeasurementService measurementService;

	MeasurementResource(final MeasurementService measurementService) {
		this.measurementService = measurementService;
	}

	@GetMapping(path = "/{category}/{aggregateOn}", produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
	@Operation(summary = "Get measurement information", description = "Resource returns measurement data matching provided search parameters")
	@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {Problem.class, ConstraintViolationProblem.class})))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<MeasurementResponse> getMeasurements(
		@Parameter(name = "category", schema = @Schema(implementation = Category.class), required = true) @PathVariable(name = "category") Category category,
		@Parameter(name = "aggregateOn", schema = @Schema(implementation = Aggregation.class), required = true) @PathVariable(name = "aggregateOn") Aggregation aggregateOn,
		@Valid MeasurementParameters searchParams) {

		return ok(measurementService.getMeasurements(category, aggregateOn, searchParams));
	}
}
