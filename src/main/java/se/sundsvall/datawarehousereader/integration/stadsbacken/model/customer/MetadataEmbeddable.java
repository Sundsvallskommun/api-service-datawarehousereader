package se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MetadataEmbeddable {

	@Column(name = "TotalRecords", columnDefinition = "int", insertable = false, updatable = false)
	private int totalRecords;

	@Column(name = "TotalPages", columnDefinition = "int", insertable = false, updatable = false)
	private int totalPages;

	@Column(name = "Count", columnDefinition = "int", insertable = false, updatable = false)
	private int count;

	public static MetadataEmbeddable create() {
		return new MetadataEmbeddable();
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public MetadataEmbeddable withTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
		return this;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public MetadataEmbeddable withTotalPages(int totalPages) {
		this.totalPages = totalPages;
		return this;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public MetadataEmbeddable withCount(int count) {
		this.count = count;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(count, totalPages, totalRecords);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof final MetadataEmbeddable other)) { return false; }
		return Float.floatToIntBits(count) == Float.floatToIntBits(other.count) && Float.floatToIntBits(totalPages) == Float.floatToIntBits(other.totalPages) && totalRecords == other.totalRecords;
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		builder.append("MetadataEmbeddable [totalRecords=").append(totalRecords).append(", totalPages=").append(totalPages).append(", count=").append(count).append("]");
		return builder.toString();
	}
}
