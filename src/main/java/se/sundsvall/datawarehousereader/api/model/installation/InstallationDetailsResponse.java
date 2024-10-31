package se.sundsvall.datawarehousereader.api.model.installation;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class InstallationDetailsResponse {

	@JsonProperty("_meta")
	@Schema(implementation = PagingAndSortingMetaData.class, accessMode = READ_ONLY)
	private PagingAndSortingMetaData metaData;

	@ArraySchema(schema = @Schema(implementation = InstallationDetails.class, accessMode = READ_ONLY))
	private List<InstallationDetails> installationDetails;

	public static InstallationDetailsResponse create() {
		return new InstallationDetailsResponse();
	}

	public PagingAndSortingMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(final PagingAndSortingMetaData metaData) {
		this.metaData = metaData;
	}

	public InstallationDetailsResponse withMetaData(final PagingAndSortingMetaData metaData) {
		this.metaData = metaData;
		return this;
	}

	public List<InstallationDetails> getInstallationDetails() {
		return installationDetails;
	}

	public void setInstallationDetails(final List<InstallationDetails> installationDetails) {
		this.installationDetails = installationDetails;
	}

	public InstallationDetailsResponse withInstallationDetails(final List<InstallationDetails> installationDetails) {
		this.installationDetails = installationDetails;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		InstallationDetailsResponse that = (InstallationDetailsResponse) o;
		return Objects.equals(metaData, that.metaData) && Objects.equals(installationDetails, that.installationDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), metaData, installationDetails);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("InstallationDetailsResponse [metaData=").append(metaData).append(", installationDetails=")
			.append(installationDetails).append("]");
		return builder.toString();
	}
}
