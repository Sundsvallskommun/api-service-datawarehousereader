package se.sundsvall.datawarehousereader.api.model.installation;

import java.time.LocalDate;
import java.util.Objects;

import se.sundsvall.datawarehousereader.api.model.Category;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation.InstallationEntity;
import se.sundsvall.dept44.models.api.paging.AbstractParameterPagingAndSortingBase;
import se.sundsvall.dept44.models.api.paging.validation.ValidSortByProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Installations request parameters model")
@ValidSortByProperty(InstallationEntity.class)
public class InstallationParameters extends AbstractParameterPagingAndSortingBase {

	@Schema(description = "Is the installation installed", example = "true")
	private Boolean installed;

	@Schema(description = "Earliest date when item was last modified", example = "2022-12-31")
	private LocalDate lastModifiedDateFrom;

	@Schema(description = "Latest date when item was last modified", example = "2022-12-31")
	private LocalDate lastModifiedDateTo;

	@Schema(description = "Category", example = "ELECTRICITY", implementation = Category.class)
	private Category category;

	@Schema(description = "Facility id", example = "735999109151401011")
	private String facilityId;

	public static InstallationParameters create() {
		return new InstallationParameters();
	}

	public Boolean getInstalled() {
		return installed;
	}

	public void setInstalled(final Boolean installed) {
		this.installed = installed;
	}

	public InstallationParameters withInstalled(final Boolean installed) {
		this.installed = installed;
		return this;
	}

	public LocalDate getLastModifiedDateFrom() {
		return lastModifiedDateFrom;
	}

	public void setLastModifiedDateFrom(final LocalDate lastModifiedDateFrom) {
		this.lastModifiedDateFrom = lastModifiedDateFrom;
	}

	public InstallationParameters withLastModifiedDateFrom(final LocalDate lastModifiedDateFrom) {
		this.lastModifiedDateFrom = lastModifiedDateFrom;
		return this;
	}

	public LocalDate getLastModifiedDateTo() {
		return lastModifiedDateTo;
	}

	public void setLastModifiedDateTo(final LocalDate lastModifiedDateTo) {
		this.lastModifiedDateTo = lastModifiedDateTo;
	}

	public InstallationParameters withLastModifiedDateTo(final LocalDate lastModifiedDateTo) {
		this.lastModifiedDateTo = lastModifiedDateTo;
		return this;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(final Category category) {
		this.category = category;
	}

	public InstallationParameters withCategory(final Category category) {
		this.category = category;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(final String facilityId) {
		this.facilityId = facilityId;
	}

	public InstallationParameters withFacilityId(final String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		InstallationParameters that = (InstallationParameters) o;
		return Objects.equals(installed, that.installed) && Objects.equals(lastModifiedDateFrom, that.lastModifiedDateFrom) && Objects.equals(lastModifiedDateTo, that.lastModifiedDateTo) && category == that.category && Objects.equals(facilityId,
			that.facilityId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), installed, lastModifiedDateFrom, lastModifiedDateTo, category, facilityId);
	}

	@Override
	public String toString() {
		return "InstallationParameters{" +
			"installed=" + installed +
			", lastModifiedDateFrom=" + lastModifiedDateFrom +
			", lastModifiedDateTo=" + lastModifiedDateTo +
			", category=" + category +
			", facilityId='" + facilityId + '\'' +
			", sortBy=" + sortBy +
			", sortDirection=" + sortDirection +
			", page=" + page +
			", limit=" + limit +
			'}';
	}
}
