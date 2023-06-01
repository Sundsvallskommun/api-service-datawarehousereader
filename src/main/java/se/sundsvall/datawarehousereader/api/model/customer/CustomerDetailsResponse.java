package se.sundsvall.datawarehousereader.api.model.customer;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import se.sundsvall.datawarehousereader.api.model.MetaData;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer details response model")
public class CustomerDetailsResponse {

	@JsonProperty("_meta")
	@Schema(implementation = MetaData.class, accessMode = READ_ONLY)
	private MetaData metaData;

	@ArraySchema(schema = @Schema(implementation = CustomerDetails.class, accessMode = READ_ONLY))
	private List<CustomerDetails> customerDetails;

	public static CustomerDetailsResponse create() {
		return new CustomerDetailsResponse();
	}

	public CustomerDetailsResponse withMetadata(MetaData metaData) {
		this.metaData = metaData;
		return this;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(final MetaData metaData) {
		this.metaData = metaData;
	}

	public CustomerDetailsResponse withCustomerDetails(List<CustomerDetails> customerDetails) {
		this.customerDetails = customerDetails;
		return this;
	}

	public List<CustomerDetails> getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(final List<CustomerDetails> customerDetails) {
		this.customerDetails = customerDetails;
	}

	@Override
	public String toString() {
		return "CustomerDetailsResponse{" +
			"metaData=" + metaData +
			", customerDetails=" + customerDetails +
			'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CustomerDetailsResponse that = (CustomerDetailsResponse) o;
		return Objects.equals(metaData, that.metaData) && Objects.equals(customerDetails, that.customerDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(metaData, customerDetails);
	}

}
