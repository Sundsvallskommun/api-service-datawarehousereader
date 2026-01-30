package se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class MeasurementDistrictHeatingKey implements Serializable {

	private static final long serialVersionUID = -1348815619727439020L;

	private String customerOrgId;

	private String facilityId;

	private String feedType;

	private LocalDateTime measurementTimestamp;

	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public void setCustomerOrgId(final String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(final String facilityId) {
		this.facilityId = facilityId;
	}

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(final String feedType) {
		this.feedType = feedType;
	}

	public LocalDateTime getMeasurementTimestamp() {
		return measurementTimestamp;
	}

	public void setMeasurementTimestamp(final LocalDateTime measurementTimestamp) {
		this.measurementTimestamp = measurementTimestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerOrgId, facilityId, feedType, measurementTimestamp);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MeasurementDistrictHeatingKey other = (MeasurementDistrictHeatingKey) obj;
		return (Objects.equals(customerOrgId, other.customerOrgId) &&
			Objects.equals(facilityId, other.facilityId) &&
			Objects.equals(feedType, other.feedType) &&
			Objects.equals(measurementTimestamp, other.measurementTimestamp));
	}

	@Override
	public String toString() {
		return "MeasurementDistrictHeatingKey [customerOrgId="
			+ customerOrgId
			+ ", facilityId="
			+ facilityId
			+ ", feedType="
			+ feedType
			+ ", measurementTimestamp="
			+ measurementTimestamp
			+ "]";
	}
}
