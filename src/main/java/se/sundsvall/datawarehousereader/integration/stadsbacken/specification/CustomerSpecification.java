package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerEntity;

import java.util.List;

public interface CustomerSpecification {

	SpecificationBuilder<CustomerEntity> BUILDER = new SpecificationBuilder<>();
	static Specification<CustomerEntity> withCustomerOrgIds(List<String> customerOrgIds) {
		return BUILDER.buildInFilterForString("customerOrgId", customerOrgIds);
	}

	static Specification<CustomerEntity> withOrganizationId(String organizationId) {
		return BUILDER.buildEqualFilter("organizationId", organizationId);
	}

	static Specification<CustomerEntity> withOrganizationName(String organizationName) {
		return BUILDER.buildEqualFilter("organizationName", organizationName);
	}

	static Specification<CustomerEntity> withCustomerId(Integer customerId) {
		return BUILDER.buildEqualFilter("customerId", customerId);
	}
}
