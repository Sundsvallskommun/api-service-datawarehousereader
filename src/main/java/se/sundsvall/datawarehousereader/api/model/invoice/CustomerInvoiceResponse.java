package se.sundsvall.datawarehousereader.api.model.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Schema(description = "Customer invoice response model")
public class CustomerInvoiceResponse {

	@JsonProperty("_meta")
	@Schema(implementation = PagingAndSortingMetaData.class, accessMode = READ_ONLY)
	private PagingAndSortingMetaData metaData;

	@ArraySchema(schema = @Schema(implementation = CustomerInvoice.class, accessMode = READ_ONLY))
	private List<CustomerInvoice> invoices;

	public static CustomerInvoiceResponse create() {
		return new CustomerInvoiceResponse();
	}

	public List<CustomerInvoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(final List<CustomerInvoice> invoices) {
		this.invoices = invoices;
	}

	public CustomerInvoiceResponse withInvoices(final List<CustomerInvoice> invoices) {
		this.invoices = invoices;
		return this;
	}

	public PagingAndSortingMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(final PagingAndSortingMetaData metaData) {
		this.metaData = metaData;
	}

	public CustomerInvoiceResponse withMetaData(final PagingAndSortingMetaData metaData) {
		this.metaData = metaData;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(invoices, metaData);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final CustomerInvoiceResponse other = (CustomerInvoiceResponse) obj;
		return Objects.equals(invoices, other.invoices) && Objects.equals(metaData, other.metaData);
	}

	@Override
	public String toString() {
		return "CustomerInvoiceResponse [metaData=" + metaData + ", invoices=" + invoices + "]";
	}
}
