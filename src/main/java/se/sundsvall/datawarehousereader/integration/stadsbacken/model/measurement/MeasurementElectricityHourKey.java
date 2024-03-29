package se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class MeasurementElectricityHourKey implements Serializable {

	private static final long serialVersionUID = -1348815619727439020L;

	private String customerOrgId;

	private String facilityId;

	private String feedType;

	private String unit;

	private BigDecimal usage;

	private LocalDateTime measurementTimestamp;

	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public void setCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

	public LocalDateTime getMeasurementTimestamp() {
		return measurementTimestamp;
	}

	public void setMeasurementTimestamp(LocalDateTime measurementTimestamp) {
		this.measurementTimestamp = measurementTimestamp;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getUsage() {
		return usage;
	}

	public void setUsage(BigDecimal usage) {
		this.usage = usage;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerOrgId, facilityId, feedType, measurementTimestamp, unit, usage);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MeasurementElectricityHourKey other = (MeasurementElectricityHourKey) obj;
		return Objects.equals(customerOrgId, other.customerOrgId) && Objects.equals(facilityId, other.facilityId) && Objects.equals(feedType, other.feedType) && Objects.equals(measurementTimestamp, other.measurementTimestamp) && Objects.equals(unit,
			other.unit) && Objects.equals(usage, other.usage);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MeasurementElectricityHourKey [customerOrgId=").append(customerOrgId).append(", facilityId=").append(facilityId).append(", feedType=").append(feedType).append(", unit=").append(unit).append(", usage=").append(usage).append(
			", measurementTimestamp=").append(measurementTimestamp).append("]");
		return builder.toString();
	}
}
