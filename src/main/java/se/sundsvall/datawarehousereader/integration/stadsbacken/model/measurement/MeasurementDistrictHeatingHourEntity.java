package se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(schema = "kundinfo", name = "vMeasurementDistrictHeatingHour")
public class MeasurementDistrictHeatingHourEntity implements DefaultMeasurementAttributesInterface, Serializable {

	private static final long serialVersionUID = -1348815619727439020L;

	@Id
	@Column(name = "ReadingDetailID", nullable = false, insertable = false, updatable = false, columnDefinition = "bigint")
	private Long readingDetailId;

	@Column(name = "customerorgid", nullable = false, insertable = false, updatable = false, columnDefinition = "varchar(8000)")
	private String customerOrgId;
	 
	@Column(name = "uuid", insertable = false, updatable = false, columnDefinition = "uniqueidentifier")
	private String uuid;

	@Column(name = "facilityId", insertable = false, updatable = false, columnDefinition = "varchar(255)")
	private String facilityId;

	@Column(name = "feedType", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String feedType;
	
	@Column(name = "isInterpolted", nullable = false, insertable = false, updatable = false, columnDefinition = "bit")
	private Integer interpolation;

	@Column(name = "DateAndTime", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDateTime measurementTimestamp;

	@Column(name = "unit", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String unit;

	@Column(name = "usage", insertable = false, updatable = false, columnDefinition="decimal(28,10)")
	private BigDecimal usage;

	@Column(name = "READING_DAY_SEQ", nullable = false, insertable = false, updatable = false)
	private Integer readingSequence;

	public static MeasurementDistrictHeatingHourEntity create() {
		return new MeasurementDistrictHeatingHourEntity();
	}

	public Long getReadingDetailId() {
		return readingDetailId;
	}

	public void setReadingDetailId(Long readingDetailId) {
		this.readingDetailId = readingDetailId;
	}

	public MeasurementDistrictHeatingHourEntity withReadingDetailId(Long readingDetailId) {
		this.readingDetailId = readingDetailId;
		return this;
	}

	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public void setCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public MeasurementDistrictHeatingHourEntity withCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public MeasurementDistrictHeatingHourEntity withUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public MeasurementDistrictHeatingHourEntity withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

	public MeasurementDistrictHeatingHourEntity withFeedType(String feedType) {
		this.feedType = feedType;
		return this;
	}

	public Integer getInterpolation() {
		return interpolation;
	}

	public void setInterpolation(Integer interpolation) {
		this.interpolation = interpolation;
	}

	public MeasurementDistrictHeatingHourEntity withInterpolation(Integer interpolation) {
		this.interpolation = interpolation;
		return this;
	}

	public LocalDateTime getMeasurementTimestamp() {
		return measurementTimestamp;
	}

	public void setMeasurementTimestamp(LocalDateTime measurementTimestamp) {
		this.measurementTimestamp = measurementTimestamp;
	}

	public MeasurementDistrictHeatingHourEntity withMeasurementTimestamp(LocalDateTime measurementTimestamp) {
		this.measurementTimestamp = measurementTimestamp;
		return this;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public MeasurementDistrictHeatingHourEntity withUnit(String unit) {
		this.unit = unit;
		return this;
	}

	public BigDecimal getUsage() {
		return usage;
	}

	public void setUsage(BigDecimal usage) {
		this.usage = usage;
	}

	public MeasurementDistrictHeatingHourEntity withUsage(BigDecimal usage) {
		this.usage = usage;
		return this;
	}

	public Integer getReadingSequence() {
		return readingSequence;
	}

	public void setReadingSequence(Integer readingSequence) {
		this.readingSequence = readingSequence;
	}

	public MeasurementDistrictHeatingHourEntity withReadingSequence(Integer readingSequence) {
		this.readingSequence = readingSequence;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(readingDetailId, customerOrgId, facilityId, feedType, interpolation, measurementTimestamp,
				readingSequence, unit, usage, uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasurementDistrictHeatingHourEntity other = (MeasurementDistrictHeatingHourEntity) obj;
		return Objects.equals(readingDetailId, other.readingDetailId) && Objects.equals(customerOrgId, other.customerOrgId)
				&& Objects.equals(facilityId, other.facilityId) && Objects.equals(feedType, other.feedType)
				&& Objects.equals(interpolation, other.interpolation) && Objects.equals(measurementTimestamp, other.measurementTimestamp)
				&& Objects.equals(readingSequence, other.readingSequence) && Objects.equals(unit, other.unit)
				&& Objects.equals(usage, other.usage) && Objects.equals(uuid, other.uuid);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MeasurementDistrictHeatingHourEntity [readingDetailId=").append(readingDetailId).append(", customerOrgId").append(customerOrgId).append(", uuid=")
				.append(uuid).append(", facilityId=").append(facilityId).append(", feedType=").append(feedType)
				.append(", interpolation=").append(interpolation).append(", measurementTimestamp=")
				.append(measurementTimestamp).append(", unit=").append(unit).append(", usage=").append(usage)
				.append(", readingSequence=").append(readingSequence).append("]");
		return builder.toString();
	}
}
