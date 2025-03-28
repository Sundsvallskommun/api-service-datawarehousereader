package se.sundsvall.datawarehousereader.api.model.customer;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

@Schema(description = "Customer engagement response model")
public class CustomerEngagementResponse {

	@JsonProperty("_meta")
	@Schema(implementation = PagingAndSortingMetaData.class, accessMode = READ_ONLY)
	private PagingAndSortingMetaData metaData;

	@ArraySchema(schema = @Schema(implementation = CustomerEngagement.class, accessMode = READ_ONLY))
	private List<CustomerEngagement> customerEngagements;

	public static CustomerEngagementResponse create() {
		return new CustomerEngagementResponse();
	}

	public PagingAndSortingMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(PagingAndSortingMetaData metaData) {
		this.metaData = metaData;
	}

	public CustomerEngagementResponse withMetaData(PagingAndSortingMetaData metaData) {
		this.metaData = metaData;
		return this;
	}

	public List<CustomerEngagement> getCustomerEngagements() {
		return customerEngagements;
	}

	public void setCustomerEngagements(List<CustomerEngagement> customerEngagements) {
		this.customerEngagements = customerEngagements;
	}

	public CustomerEngagementResponse withCustomerEngagements(List<CustomerEngagement> customerEngagements) {
		this.customerEngagements = customerEngagements;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerEngagements, metaData);
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
		final CustomerEngagementResponse other = (CustomerEngagementResponse) obj;
		return Objects.equals(customerEngagements, other.customerEngagements) && Objects.equals(metaData, other.metaData);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CustomerEngagementResponse [metaData=").append(metaData).append(", customerEngagements=").append(customerEngagements)
			.append("]");
		return builder.toString();
	}

}
