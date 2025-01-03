package se.sundsvall.datawarehousereader.api.model.measurement;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "Measurement meta data model")
public class MeasurementMetaData {

	@Schema(description = "Key connected to the meta data", example = "READING_SEQ", accessMode = READ_ONLY)
	private String key;

	@Schema(description = "Value for the meta data", example = "5733010", accessMode = READ_ONLY)
	private String value;

	public static MeasurementMetaData create() {
		return new MeasurementMetaData();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public MeasurementMetaData withKey(String key) {
		this.key = key;
		return this;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MeasurementMetaData withValue(String value) {
		this.value = value;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value);
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
		final MeasurementMetaData other = (MeasurementMetaData) obj;
		return Objects.equals(key, other.key) && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MeasurementMetaData [key=").append(key).append(", value=").append(value).append("]");
		return builder.toString();
	}
}
