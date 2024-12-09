package se.sundsvall.datawarehousereader.api.model.installation;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "Installation metadata model")
public class InstallationMetaData {

	@Schema(description = "Key", example = "netarea", accessMode = READ_ONLY)
	private String key;

	@Schema(description = "Value", example = "Sundsvall tätort", accessMode = READ_ONLY)
	private String value;

	@Schema(description = "Type", example = "location", accessMode = READ_ONLY)
	private String type;

	@Schema(description = "Displayname", example = "Nätområde", accessMode = READ_ONLY)
	private String displayName;

	public static InstallationMetaData create() {
		return new InstallationMetaData();
	}

	public String getKey() {
		return key;
	}

	public InstallationMetaData withKey(String key) {
		this.key = key;
		return this;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public InstallationMetaData withValue(String value) {
		this.value = value;
		return this;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public InstallationMetaData withType(String type) {
		this.type = type;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public InstallationMetaData withDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		InstallationMetaData that = (InstallationMetaData) o;
		return Objects.equals(key, that.key) && Objects.equals(value, that.value) && Objects.equals(type, that.type) && Objects.equals(displayName, that.displayName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value, type, displayName);
	}

	@Override
	public String toString() {
		return "InstallationMetaData{" +
			"key='" + key + '\'' +
			", value='" + value + '\'' +
			", type='" + type + '\'' +
			", displayName='" + displayName + '\'' +
			'}';
	}
}
