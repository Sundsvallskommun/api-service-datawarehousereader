package se.sundsvall.datawarehousereader.integration.stadsbacken.model.measurement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(schema = "kundinfo", name = "vMeasurementElectricityHour")
@IdClass(MeasurementElectricityHourKey.class)
@NamedStoredProcedureQuery(name = "measurement.electricityHour", procedureName = "kundinfo.spMeasurementElectricityHour", 
resultClasses = MeasurementElectricityHourEntity.class,
parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "customerorgid", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "anlaggningsID", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "datum_start", type = LocalDateTime.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "datum_stop", type = LocalDateTime.class)
})
public class MeasurementElectricityHourEntity implements DefaultMeasurementAttributesInterface {

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

	// 0 means that the data is based on real values, i.e. that no interpolation has been done (this
	// information is missing from SP result but defined in the DefaultMeasurementAttributesInterface
	// and needs to be returned to fulfill the interface)
	@Transient
	private Integer interpolation = 0;

	@Id
	@Column(name = "DateAndTime", insertable = false, updatable = false, columnDefinition = "datetime")
	private LocalDateTime measurementTimestamp;

	@Id
	@Column(name = "unit", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String unit;

	@Id
	@Column(name = "usage", insertable = false, updatable = false, columnDefinition="decimal(28,10)")
	private BigDecimal usage;

	public static MeasurementElectricityHourEntity create() {
		return new MeasurementElectricityHourEntity();
	}
	
	public String getCustomerOrgId() {
		return customerOrgId;
	}

	public void setCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
	}

	public MeasurementElectricityHourEntity withCustomerOrgId(String customerOrgId) {
		this.customerOrgId = customerOrgId;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public MeasurementElectricityHourEntity withUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public MeasurementElectricityHourEntity withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

	public MeasurementElectricityHourEntity withFeedType(String feedType) {
		this.feedType = feedType;
		return this;
	}

	public Integer getInterpolation() {
		return interpolation;
	}

	public void setInterpolation(Integer interpolation) {
		this.interpolation = interpolation;
	}

	public MeasurementElectricityHourEntity withInterpolation(Integer interpolation) {
		this.interpolation = interpolation;
		return this;
	}

	public LocalDateTime getMeasurementTimestamp() {
		return measurementTimestamp;
	}

	public void setMeasurementTimestamp(LocalDateTime measurementTimestamp) {
		this.measurementTimestamp = measurementTimestamp;
	}

	public MeasurementElectricityHourEntity withMeasurementTimestamp(LocalDateTime measurementTimestamp) {
		this.measurementTimestamp = measurementTimestamp;
		return this;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public MeasurementElectricityHourEntity withUnit(String unit) {
		this.unit = unit;
		return this;
	}

	public BigDecimal getUsage() {
		return usage;
	}

	public void setUsage(BigDecimal usage) {
		this.usage = usage;
	}

	public MeasurementElectricityHourEntity withUsage(BigDecimal usage) {
		this.usage = usage;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerOrgId, facilityId, feedType, interpolation, measurementTimestamp, unit, usage, uuid);
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
		MeasurementElectricityHourEntity other = (MeasurementElectricityHourEntity) obj;
		return Objects.equals(customerOrgId, other.customerOrgId) && Objects.equals(facilityId, other.facilityId) && Objects.equals(feedType, other.feedType) && Objects.equals(interpolation, other.interpolation) && Objects.equals(
			measurementTimestamp, other.measurementTimestamp) && Objects.equals(unit, other.unit) && Objects.equals(usage, other.usage) && Objects.equals(uuid, other.uuid);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MeasurementElectricityHourEntity [customerOrgId=").append(customerOrgId).append(", uuid=").append(uuid).append(", facilityId=").append(facilityId).append(", feedType=").append(feedType).append(", interpolation=").append(
			interpolation).append(", measurementTimestamp=").append(measurementTimestamp).append(", unit=").append(unit).append(", usage=").append(usage).append("]");
		return builder.toString();
	}
}
