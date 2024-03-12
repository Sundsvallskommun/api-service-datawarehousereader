package se.sundsvall.datawarehousereader.api.model.installation;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

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

	@Schema(description = "Last changed date. Format is YYYY-MM-DD.", example = "2022-01-01")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dateFrom;

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

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(final LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public InstallationParameters withDateFrom(final LocalDate dateFrom) {
		this.dateFrom = dateFrom;
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		InstallationParameters that = (InstallationParameters) o;
		return Objects.equals(installed, that.installed) && Objects.equals(dateFrom, that.dateFrom) && Objects.equals(category, that.category) && Objects.equals(facilityId, that.facilityId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), installed, dateFrom, category, facilityId);
	}

	@Override
	public String toString() {
		return "InstallationParameters{" +
			"installed=" + installed +
			", dateFrom=" + dateFrom +
			", category='" + category + '\'' +
			", facilityId='" + facilityId + '\'' +
			", sortBy=" + sortBy +
			", sortDirection=" + sortDirection +
			", page=" + page +
			", limit=" + limit +
			'}';
	}
}
