package se.sundsvall.datawarehousereader.api.model.measurement;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Schema(description = "Measurement model")
public class Measurement {

	@Schema(description = "Unique identifier for the measurement", examples = "550e8400-e29b-41d4-a716-446655440000", accessMode = READ_ONLY)
	private String uuid;

	@Schema(description = "Customer organization identifier", examples = "5534567890", accessMode = READ_ONLY)
	private String customerOrgId;

	@Schema(description = "Facility identifier", examples = "735999109151401011", accessMode = READ_ONLY)
	private String facilityId;

	@Schema(description = "Type of feed", examples = "Energy", accessMode = READ_ONLY)
	private String feedType;

	@Schema(description = "Measurement unit", examples = "kWh", accessMode = READ_ONLY)
	private String unit;

	@Schema(description = "Usage value", examples = "1500", accessMode = READ_ONLY)
	private BigDecimal usage;

	@Schema(description = "Interpolation value indicating data quality", examples = "0", accessMode = READ_ONLY)
	private Integer interpolation;

	@Schema(description = "Date and time of the measurement", examples = "2024-01-15T10:30:00", accessMode = READ_ONLY)
	private LocalDateTime dateAndTime;

	public static Measurement create() {
		return new Measurement();
	}

	public String getUuid() {
		return uuid;
	}

	public Measurement withUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public Measurement withCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
		return this;
	}

	public void setCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public Measurement withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public String getFeedType() {
		return feedType;
	}

	public Measurement withFeedType(String feedType) {
		this.feedType = feedType;
		return this;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

	public String getUnit() {
		return unit;
	}

	public Measurement withUnit(String unit) {
		this.unit = unit;
		return this;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getUsage() {
		return usage;
	}

	public Measurement withUsage(BigDecimal usage) {
		this.usage = usage;
		return this;
	}

	public void setUsage(BigDecimal usage) {
		this.usage = usage;
	}

	public Integer getInterpolation() {
		return interpolation;
	}

	public Measurement withInterpolation(Integer interpolation) {
		this.interpolation = interpolation;
		return this;
	}

	public void setInterpolation(Integer interpolation) {
		this.interpolation = interpolation;
	}

	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public Measurement withDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
		return this;
	}

	public void setDateAndTime(LocalDateTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	@Override
	public String toString() {
		return "Measurement{" +
			"uuid='" + uuid + '\'' +
			", customerOrgId='" + customerOrgId + '\'' +
			", facilityId='" + facilityId + '\'' +
			", feedType='" + feedType + '\'' +
			", unit='" + unit + '\'' +
			", usage=" + usage +
			", interpolation=" + interpolation +
			", dateAndTime=" + dateAndTime +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		Measurement that = (Measurement) o;
		return Objects.equals(uuid, that.uuid) && Objects.equals(customerOrgId, that.customerOrgId) && Objects.equals(facilityId, that.facilityId) && Objects.equals(feedType, that.feedType) && Objects.equals(
			unit, that.unit) && Objects.equals(usage, that.usage) && Objects.equals(interpolation, that.interpolation) && Objects.equals(dateAndTime, that.dateAndTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, customerOrgId, facilityId, feedType, unit, usage, interpolation, dateAndTime);
	}
}
