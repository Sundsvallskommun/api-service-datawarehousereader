package se.sundsvall.datawarehousereader.integration.stadsbacken.model.installation;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class InstallationMetaDataEmbeddable implements Serializable {

	private static final long serialVersionUID = 7581230689748313918L;

	@Column(name = "company", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String company;

	@Column(name = "`key`", nullable = false, insertable = false, updatable = false)
	private String key;

	@Column(name = "`value`", insertable = false, updatable = false, columnDefinition = "nvarchar(255)")
	private String value;

	@Column(name = "`type`", nullable = false, insertable = false, updatable = false, columnDefinition = "varchar(8)")
	private String type;

	@Column(name = "displayName", nullable = false, insertable = false, updatable = false, columnDefinition = "varchar(26)")
	private String displayName;

	public static InstallationMetaDataEmbeddable create() {
		return new InstallationMetaDataEmbeddable();
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(final String company) {
		this.company = company;
	}

	public InstallationMetaDataEmbeddable withCompany(final String company) {
		this.company = company;
		return this;
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public InstallationMetaDataEmbeddable withKey(final String key) {
		this.key = key;
		return this;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public InstallationMetaDataEmbeddable withValue(final String value) {
		this.value = value;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public InstallationMetaDataEmbeddable withType(final String type) {
		this.type = type;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	public InstallationMetaDataEmbeddable withDisplayName(final String displayName) {
		this.displayName = displayName;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InstallationMetaDataEmbeddable that = (InstallationMetaDataEmbeddable) o;
		return Objects.equals(company, that.company) && Objects.equals(key, that.key) && Objects.equals(value, that.value) && Objects.equals(type, that.type) && Objects.equals(displayName, that.displayName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(company, key, value, type, displayName);
	}

	@Override
	public String toString() {
		return "InstallationMetaDataEmbeddable{" +
			"company='" + company + '\'' +
			", key='" + key + '\'' +
			", value='" + value + '\'' +
			", type='" + type + '\'' +
			", displayName='" + displayName + '\'' +
			'}';
	}
}
