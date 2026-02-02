package se.sundsvall.datawarehousereader.api.model.measurement;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;

@Schema(description = "Measurement request parameters model")
@ParameterObject
public class MeasurementParameters {

	@Schema(description = "PartyId (e.g. a personId or an organizationId)", examples = "81471222-5798-11e9-ae24-57fa13b361e1")
	@ValidUuid(nullable = true)
	private String partyId;

	@Schema(description = "Facility id", examples = "735999109151401011")
	private String facilityId;

	@Schema(description = "Date and time for oldest measurement point to return. Format is yyyy-MM-dd'T'HH:mm:ss.SSSXXX, for example '2000-10-31T01:30:00.000-05:00'")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime fromDateTime;

	@Schema(description = "Date and time for youngest measurement point to return. Format is yyyy-MM-dd'T'HH:mm:ss.SSSXXX, for example '2000-10-31T01:30:00.000-05:00'")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime toDateTime;

	public MeasurementParameters() {}

	public static MeasurementParameters create() {
		return new MeasurementParameters();
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public MeasurementParameters withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public MeasurementParameters withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public OffsetDateTime getFromDateTime() {
		return fromDateTime;
	}

	public void setFromDateTime(OffsetDateTime fromDateTime) {
		this.fromDateTime = fromDateTime;
	}

	public MeasurementParameters withFromDateTime(OffsetDateTime fromDateTime) {
		this.fromDateTime = fromDateTime;
		return this;
	}

	public OffsetDateTime getToDateTime() {
		return toDateTime;
	}

	public void setToDateTime(OffsetDateTime toDateTime) {
		this.toDateTime = toDateTime;
	}

	public MeasurementParameters withToDateTime(OffsetDateTime toDateTime) {
		this.toDateTime = toDateTime;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		MeasurementParameters that = (MeasurementParameters) o;
		return Objects.equals(partyId, that.partyId) && Objects.equals(facilityId, that.facilityId) && Objects.equals(fromDateTime, that.fromDateTime) && Objects.equals(toDateTime, that.toDateTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(partyId, facilityId, fromDateTime, toDateTime);
	}

	@Override
	public String toString() {
		return "MeasurementParameters{" +
			"partyId='" + partyId + '\'' +
			", facilityId='" + facilityId + '\'' +
			", fromDateTime=" + fromDateTime +
			", toDateTime=" + toDateTime +
			'}';
	}
}
