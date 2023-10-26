package se.sundsvall.datawarehousereader.api.model.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import java.util.List;
import java.util.Objects;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Schema(description = "Invoice response model")
public class InvoiceResponse {

	@JsonProperty("_meta")
	@Schema(implementation = PagingAndSortingMetaData.class, accessMode = READ_ONLY)
	private PagingAndSortingMetaData metaData;

	@ArraySchema(schema = @Schema(implementation = Invoice.class, accessMode = READ_ONLY))
	private List<Invoice> invoices;

	public static InvoiceResponse create() {
		return new InvoiceResponse();
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public InvoiceResponse withInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
		return this;
	}

	public PagingAndSortingMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(PagingAndSortingMetaData metaData) {
		this.metaData = metaData;
	}

	public InvoiceResponse withMetaData(PagingAndSortingMetaData metaData) {
		this.metaData = metaData;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(invoices, metaData);
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
		final InvoiceResponse other = (InvoiceResponse) obj;
		return Objects.equals(invoices, other.invoices) && Objects.equals(metaData, other.metaData);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("InvoiceResponse [metaData=").append(metaData).append(", invoices=").append(invoices)
			.append("]");
		return builder.toString();
	}
}
