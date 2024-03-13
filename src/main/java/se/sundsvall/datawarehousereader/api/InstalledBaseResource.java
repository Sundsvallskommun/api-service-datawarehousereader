package se.sundsvall.datawarehousereader.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseParameters;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseResponse;
import se.sundsvall.datawarehousereader.service.InstalledBaseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Validated
@RequestMapping("/installedbase")
@Tag(name = "Installed base", description = "Installed base operations")
class InstalledBaseResource {

	private final InstalledBaseService installedBaseService;

	InstalledBaseResource(final InstalledBaseService installedBaseService) {
		this.installedBaseService = installedBaseService;
	}

	@GetMapping(produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
	@Operation(summary = "Get installed base information", description = "Resource returns all installed bases matching provided search parameters")
	@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {Problem.class, ConstraintViolationProblem.class})))
	@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<InstalledBaseResponse> getInstalledBase(@Valid InstalledBaseParameters searchParams) {

		return ok(installedBaseService.getInstalledBase(searchParams));
	}
}
