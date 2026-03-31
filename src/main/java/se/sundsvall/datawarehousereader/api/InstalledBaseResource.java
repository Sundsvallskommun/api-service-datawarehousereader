package se.sundsvall.datawarehousereader.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseParameters;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBasePartyParameters;
import se.sundsvall.datawarehousereader.api.model.installedbase.InstalledBaseResponse;
import se.sundsvall.datawarehousereader.service.InstalledBaseService;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.dept44.problem.Problem;
import se.sundsvall.dept44.problem.violations.ConstraintViolationProblem;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Validated
@RequestMapping("/{municipalityId}/installedbase")
@Tag(name = "Installed base", description = "Installed base operations")
class InstalledBaseResource {

	private final InstalledBaseService installedBaseService;

	InstalledBaseResource(final InstalledBaseService installedBaseService) {
		this.installedBaseService = installedBaseService;
	}

	/**
	 * @deprecated Use {@link #getInstalledBaseByPartyId(String, String, InstalledBasePartyParameters)} instead. This method
	 *             will be removed in future releases.
	 */
	@Deprecated(forRemoval = true)
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Get installed base information", deprecated = true, description = "Resource returns all installed bases matching provided search parameters", responses = {
		@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
			Problem.class, ConstraintViolationProblem.class
		}))),
		@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	ResponseEntity<InstalledBaseResponse> getInstalledBase(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Valid InstalledBaseParameters searchParams) {

		return ok(installedBaseService.getInstalledBase(searchParams));
	}

	@GetMapping(path = "/{partyId}", produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Get installed base information by party id", description = "Resource returns installed bases for the given party id", responses = {
		@ApiResponse(responseCode = "200", description = "Successful operation", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
			Problem.class, ConstraintViolationProblem.class
		}))),
		@ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	})
	ResponseEntity<InstalledBaseResponse> getInstalledBaseByPartyId(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "partyId", description = "Party id (UUID)", example = "898B3634-A2F9-483C-8808-37F3F25CF24E") @ValidUuid @PathVariable final String partyId,
		@Valid final InstalledBasePartyParameters parameters) {

		return ok(installedBaseService.getInstalledBase(parameters.getPage(), parameters.getLimit(), parameters.getOrganizationIds(), parameters.getDate(), partyId, parameters.getSortBy()));
	}
}
