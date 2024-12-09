package se.sundsvall.datawarehousereader.integration.stadsbacken.specification;

import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerEntity_.CUSTOMER_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerEntity_.CUSTOMER_ORG_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerEntity_.ORGANIZATION_ID;
import static se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerEntity_.ORGANIZATION_NAME;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerEntity;

public interface CustomerSpecification {

	SpecificationBuilder<CustomerEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<CustomerEntity> withCustomerOrgIds(List<String> customerOrgIds) {
		return BUILDER.buildInFilterForString(CUSTOMER_ORG_ID, customerOrgIds);
	}

	static Specification<CustomerEntity> withOrganizationId(String organizationId) {
		return BUILDER.buildEqualFilter(ORGANIZATION_ID, organizationId);
	}

	static Specification<CustomerEntity> withOrganizationName(String organizationName) {
		return BUILDER.buildEqualFilter(ORGANIZATION_NAME, organizationName);
	}

	static Specification<CustomerEntity> withCustomerId(Integer customerId) {
		return BUILDER.buildEqualFilter(CUSTOMER_ID, customerId);
	}
}
