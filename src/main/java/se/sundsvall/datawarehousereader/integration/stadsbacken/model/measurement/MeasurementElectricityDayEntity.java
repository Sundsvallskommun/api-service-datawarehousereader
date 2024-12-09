package se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(schema = "kundinfo", name = "vMeasurementElectricityDay")
@IdClass(MeasurementElectricityKey.class)
public class MeasurementElectricityDayEntity implements DefaultMeasurementAttributesInterface {

	@Id
	@Column(name = "customerorgid", nullable = false, insertable = false, updatable = false, columnDefinition = "varchar(8000)")
	private String customerOrgId;

	@Column(name = "uuid", insertable = false, updatable = false, columnDefinition = "uniqueidentifier")
	private String uuid;

	@Id
	@Column(name = "facilityId", insertable = false, updatable = false, columnDefinition = "varchar(255)")
	private String facilityId;

	@Id
	@Column(name = "feedType", insertable = false, updatable = false, columnDefinition = "varchar(6)")
	private String feedType;

	@Id
	@Column(name = "isInterpolted", nullable = false, insertable = false, updatable = false, columnDefinition = "tinyint")
	private Integer interpolation;

	@Id
	@Column(name = "DateAndTime", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDateTime measurementTimestamp;

	@Id
	@Column(name = "unit", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String unit;

	@Id
	@Column(name = "usage", insertable = false, updatable = false, columnDefinition = "decimal(28,10)")
	private BigDecimal usage;

	@Column(name = "READING_DAY_SEQ", nullable = false, insertable = false, updatable = false)
	private Integer readingSequence;

	public static MeasurementElectricityDayEntity create() {
		return new MeasurementElectricityDayEntity();
	}

	@Override
	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public void setCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public MeasurementElectricityDayEntity withCustomerOrgId(String customerOrgId) {
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

	public MeasurementElectricityDayEntity withUuid(String uuid) {
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

	public MeasurementElectricityDayEntity withFacilityId(String facilityId) {
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

	public MeasurementElectricityDayEntity withFeedType(String feedType) {
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

	public MeasurementElectricityDayEntity withInterpolation(Integer interpolation) {
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

	public MeasurementElectricityDayEntity withMeasurementTimestamp(LocalDateTime measurementTimestamp) {
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

	public MeasurementElectricityDayEntity withUnit(String unit) {
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

	public MeasurementElectricityDayEntity withUsage(BigDecimal usage) {
		this.usage = usage;
		return this;
	}

	public Integer getReadingSequence() {
		return readingSequence;
	}

	public void setReadingSequence(Integer readingSequence) {
		this.readingSequence = readingSequence;
	}

	public MeasurementElectricityDayEntity withReadingSequence(Integer readingSequence) {
		this.readingSequence = readingSequence;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerOrgId, facilityId, feedType, interpolation, measurementTimestamp, unit, usage, uuid, readingSequence);
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
		final MeasurementElectricityDayEntity other = (MeasurementElectricityDayEntity) obj;
		return Objects.equals(customerOrgId, other.customerOrgId) && Objects.equals(facilityId, other.facilityId)
			&& Objects.equals(feedType, other.feedType) && Objects.equals(interpolation, other.interpolation)
			&& Objects.equals(measurementTimestamp, other.measurementTimestamp) && Objects.equals(unit, other.unit)
			&& Objects.equals(usage, other.usage) && Objects.equals(uuid, other.uuid) && Objects.equals(readingSequence, other.readingSequence);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MeasurementElectricityDayEntity [customerOrgId=").append(customerOrgId).append(", uuid=")
			.append(uuid).append(", facilityId=").append(facilityId).append(", feedType=").append(feedType)
			.append(", interpolation=").append(interpolation).append(", measurementTimestamp=")
			.append(measurementTimestamp).append(", unit=").append(unit).append(", usage=").append(usage)
			.append(", readingSequence=").append(readingSequence).append("]");
		return builder.toString();
	}
}
