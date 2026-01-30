package se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@Subselect("SELECT NULL AS uuid, NULL AS customerorgid, NULL AS facilityId, NULL AS feedType, NULL AS unit, NULL AS usage, NULL AS DateAndTime, NULL AS isInterpolted WHERE 1=0")
@IdClass(MeasurementDistrictHeatingKey.class)
public class MeasurementDistrictHeatingEntity implements DefaultMeasurementAttributesInterface, Serializable {

	private static final long serialVersionUID = -1348815619727439020L;

	@Column(name = "uuid", insertable = false, updatable = false, columnDefinition = "uniqueidentifier")
	private String uuid;

	@Id
	@Column(name = "customerorgid", nullable = false, insertable = false, updatable = false, columnDefinition = "varchar(8000)")
	private String customerOrgId;

	@Id
	@Column(name = "facilityId", insertable = false, updatable = false, columnDefinition = "varchar(255)")
	private String facilityId;

	@Id
	@Column(name = "feedType", nullable = false, insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String feedType;

	@Column(name = "unit", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String unit;

	@Column(name = "usage", insertable = false, updatable = false, columnDefinition = "decimal(28,10)")
	private BigDecimal usage;

	@Id
	@Column(name = "DateAndTime", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDateTime measurementTimestamp;

	@Column(name = "isInterpolted", nullable = false, insertable = false, updatable = false, columnDefinition = "bit")
	private Integer interpolation;

	public static MeasurementDistrictHeatingEntity create() {
		return new MeasurementDistrictHeatingEntity();
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}

	public MeasurementDistrictHeatingEntity withUuid(final String uuid) {
		this.uuid = uuid;
		return this;
	}

	@Override
	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public void setCustomerOrgId(final String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public MeasurementDistrictHeatingEntity withCustomerOrgId(final String customerOrgId) {
		this.customerOrgId = customerOrgId;
		return this;
	}

	@Override
	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(final String facilityId) {
		this.facilityId = facilityId;
	}

	public MeasurementDistrictHeatingEntity withFacilityId(final String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	@Override
	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(final String feedType) {
		this.feedType = feedType;
	}

	public MeasurementDistrictHeatingEntity withFeedType(final String feedType) {
		this.feedType = feedType;
		return this;
	}

	@Override
	public String getUnit() {
		return unit;
	}

	public void setUnit(final String unit) {
		this.unit = unit;
	}

	public MeasurementDistrictHeatingEntity withUnit(final String unit) {
		this.unit = unit;
		return this;
	}

	@Override
	public BigDecimal getUsage() {
		return usage;
	}

	public void setUsage(final BigDecimal usage) {
		this.usage = usage;
	}

	public MeasurementDistrictHeatingEntity withUsage(final BigDecimal usage) {
		this.usage = usage;
		return this;
	}

	@Override
	public LocalDateTime getMeasurementTimestamp() {
		return measurementTimestamp;
	}

	public void setMeasurementTimestamp(final LocalDateTime measurementTimestamp) {
		this.measurementTimestamp = measurementTimestamp;
	}

	public MeasurementDistrictHeatingEntity withMeasurementTimestamp(final LocalDateTime measurementTimestamp) {
		this.measurementTimestamp = measurementTimestamp;
		return this;
	}

	@Override
	public Integer getInterpolation() {
		return interpolation;
	}

	public void setInterpolation(final Integer interpolation) {
		this.interpolation = interpolation;
	}

	public MeasurementDistrictHeatingEntity withInterpolation(final Integer interpolation) {
		this.interpolation = interpolation;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final MeasurementDistrictHeatingEntity that = (MeasurementDistrictHeatingEntity) o;
		return Objects.equals(uuid, that.uuid) && Objects.equals(customerOrgId, that.customerOrgId) && Objects.equals(facilityId, that.facilityId) && Objects.equals(feedType, that.feedType) && Objects.equals(
			unit, that.unit) && Objects.equals(usage, that.usage) && Objects.equals(measurementTimestamp, that.measurementTimestamp) && Objects.equals(interpolation, that.interpolation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, customerOrgId, facilityId, feedType, unit, usage, measurementTimestamp, interpolation);
	}

	@Override
	public String toString() {
		return "MeasurementDistrictHeatingEntity{" +
			"uuid='" + uuid + '\'' +
			", customerOrgId='" + customerOrgId + '\'' +
			", facilityId='" + facilityId + '\'' +
			", feedType='" + feedType + '\'' +
			", unit='" + unit + '\'' +
			", usage=" + usage +
			", measurementTimestamp=" + measurementTimestamp +
			", interpolation=" + interpolation +
			'}';
	}
}
