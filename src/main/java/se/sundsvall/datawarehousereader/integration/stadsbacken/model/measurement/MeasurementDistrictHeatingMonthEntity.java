package se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(schema = "kundinfo", name = "vMeasurementDistrictHeatingMonth")
@IdClass(MeasurementDistrictHeatingKey.class)
public class MeasurementDistrictHeatingMonthEntity implements DefaultMeasurementAttributesInterface {

	@Id
	@Column(name = "customerorgid", insertable = false, updatable = false, columnDefinition = "varchar(8000)")
	private String customerOrgId;

	@Column(name = "uuid", insertable = false, updatable = false, columnDefinition = "uniqueidentifier")
	private String uuid;

	@Id
	@Column(name = "facilityId", insertable = false, updatable = false, columnDefinition = "varchar(255)")
	private String facilityId;

	@Column(name = "feedType", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String feedType;

	@Column(name = "isInterpolated", nullable = false, insertable = false, updatable = false, columnDefinition = "smallint")
	private Integer interpolation;

	@Column(name = "DateAndTime", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDateTime measurementTimestamp;

	@Column(name = "unit", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String unit;

	@Column(name = "usage", insertable = false, updatable = false, columnDefinition = "decimal(33,10)")
	private BigDecimal usage;

	@Id
	@Column(name = "READING_SEQ", nullable = false, insertable = false, updatable = false)
	private Integer readingSequence;

	public static MeasurementDistrictHeatingMonthEntity create() {
		return new MeasurementDistrictHeatingMonthEntity();
	}

	@Override
	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public void setCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public MeasurementDistrictHeatingMonthEntity withCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
		return this;
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public MeasurementDistrictHeatingMonthEntity withUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	@Override
	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public MeasurementDistrictHeatingMonthEntity withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	@Override
	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

	public MeasurementDistrictHeatingMonthEntity withFeedType(String feedType) {
		this.feedType = feedType;
		return this;
	}

	@Override
	public Integer getInterpolation() {
		return interpolation;
	}

	public void setInterpolation(Integer interpolation) {
		this.interpolation = interpolation;
	}

	public MeasurementDistrictHeatingMonthEntity withInterpolation(Integer interpolation) {
		this.interpolation = interpolation;
		return this;
	}

	@Override
	public LocalDateTime getMeasurementTimestamp() {
		return measurementTimestamp;
	}

	public void setMeasurementTimestamp(LocalDateTime measurementTimestamp) {
		this.measurementTimestamp = measurementTimestamp;
	}

	public MeasurementDistrictHeatingMonthEntity withMeasurementTimestamp(LocalDateTime measurementTimestamp) {
		this.measurementTimestamp = measurementTimestamp;
		return this;
	}

	@Override
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public MeasurementDistrictHeatingMonthEntity withUnit(String unit) {
		this.unit = unit;
		return this;
	}

	@Override
	public BigDecimal getUsage() {
		return usage;
	}

	public void setUsage(BigDecimal usage) {
		this.usage = usage;
	}

	public MeasurementDistrictHeatingMonthEntity withUsage(BigDecimal usage) {
		this.usage = usage;
		return this;
	}

	public Integer getReadingSequence() {
		return readingSequence;
	}

	public void setReadingSequence(Integer readingSequence) {
		this.readingSequence = readingSequence;
	}

	public MeasurementDistrictHeatingMonthEntity withReadingSequence(Integer readingSequence) {
		this.readingSequence = readingSequence;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerOrgId, facilityId, feedType, interpolation, measurementTimestamp,
			readingSequence, unit, usage, uuid);
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
		final MeasurementDistrictHeatingMonthEntity other = (MeasurementDistrictHeatingMonthEntity) obj;
		return Objects.equals(customerOrgId, other.customerOrgId) && Objects.equals(facilityId, other.facilityId)
			&& Objects.equals(feedType, other.feedType) && Objects.equals(interpolation, other.interpolation)
			&& Objects.equals(measurementTimestamp, other.measurementTimestamp)
			&& Objects.equals(readingSequence, other.readingSequence) && Objects.equals(unit, other.unit)
			&& Objects.equals(usage, other.usage) && Objects.equals(uuid, other.uuid);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MeasurementDistrictHeatingMonthEntity [customerOrgId=").append(customerOrgId).append(", uuid=")
			.append(uuid).append(", facilityId=").append(facilityId).append(", feedType=").append(feedType)
			.append(", interpolation=").append(interpolation).append(", measurementTimestamp=").append(measurementTimestamp)
			.append(", unit=").append(unit).append(", usage=").append(usage).append(", readingSequence=")
			.append(readingSequence).append("]");
		return builder.toString();
	}
}
