package se.sundsvall.datawarehousereader.api.model.installedbase;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import se.sundsvall.dept44.models.api.paging.AbstractParameterPagingBase;

@Schema(description = "Installed base party request parameters model")
@ParameterObject
public class InstalledBasePartyParameters extends AbstractParameterPagingBase {

	@Schema(description = "Comma-separated list of organization ids", examples = "123456789,123456987")
	private String organizationIds;

	@Schema(description = "Filter date", examples = "2025-06-01")
	private LocalDate date;

	@Schema(description = "Column to sort by", examples = "Company")
	private String sortBy;

	public static InstalledBasePartyParameters create() {
		return new InstalledBasePartyParameters();
	}

	public InstalledBasePartyParameters withOrganizationIds(final String organizationIds) {
		this.organizationIds = organizationIds;
		return this;
	}

	public String getOrganizationIds() {
		return organizationIds;
	}

	public void setOrganizationIds(final String organizationIds) {
		this.organizationIds = organizationIds;
	}

	public InstalledBasePartyParameters withDate(final LocalDate date) {
		this.date = date;
		return this;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}

	public InstalledBasePartyParameters withSortBy(final String sortBy) {
		this.sortBy = sortBy;
		return this;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(final String sortBy) {
		this.sortBy = sortBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(date, organizationIds, sortBy);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof InstalledBasePartyParameters other)) {
			return false;
		}
		return Objects.equals(date, other.date) && Objects.equals(organizationIds, other.organizationIds) && Objects.equals(sortBy, other.sortBy);
	}

	@Override
	public String toString() {
		return "InstalledBasePartyParameters [organizationIds=" + organizationIds + ", date=" + date + ", sortBy=" + sortBy + ", page=" + page + ", limit=" + limit + "]";
	}
}
