package se.sundsvall.datawarehousereader.service.mapper;

import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerMapper.toCustomerEngagements;
import static se.sundsvall.datawarehousereader.service.mapper.CustomerMapper.toPartyType;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import generated.se.sundsvall.party.PartyType;
import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerEngagement;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerEntity;

class CustomerMapperTest {

	@Test
	void toCustomersWithNull() {
		assertThat(toCustomerEngagements(null)).isEmpty();
	}

	@Test
	void toCustomersWithEmptyList() {
		assertThat(toCustomerEngagements(Collections.emptyList())).isEmpty();
	}

	@ParameterizedTest
	@MethodSource("toPartyTypeTestArguments")
	void toPartyTypeFromCustomerType(CustomerType customerType, PartyType partyType) {
		assertThat(toPartyType(customerType)).isEqualTo(partyType);
	}

	@Test
	void toCustomers() {
		final var customerId = 123;
		final var customerOrgId = "customerOrgId";
		final var customerType = CustomerType.ENTERPRISE.toString();
		final var organizationId = "organizationId";
		final var organizationName = "organizationName";
		final var active = true;
		final var moveInDate = LocalDateTime.now();

		final var entity = CustomerEntity.create()
			.withCustomerId(customerId)
			.withCustomerOrgId(customerOrgId)
			.withCustomerType(customerType)
			.withOrganizationId(organizationId)
			.withOrganizationName(organizationName)
			.withActive(active)
			.withMoveInDate(moveInDate);

		final var result = toCustomerEngagements(List.of(entity));

		assertThat(result)
			.hasSize(1)
			.extracting(
				CustomerEngagement::getCustomerNumber,
				CustomerEngagement::getCustomerOrgNumber,
				CustomerEngagement::getCustomerType,
				CustomerEngagement::getOrganizationNumber,
				CustomerEngagement::getOrganizationName,
				CustomerEngagement::getPartyId,
				CustomerEngagement::isActive,
				CustomerEngagement::getMoveInDate)
			.containsExactly(tuple(
				valueOf(customerId),
				customerOrgId,
				CustomerType.ENTERPRISE,
				organizationId,
				organizationName,
				null,
				active,
				moveInDate.toLocalDate()));
	}

	private static Stream<Arguments> toPartyTypeTestArguments() {
		return Stream.of(
			Arguments.of(CustomerType.PRIVATE, PartyType.PRIVATE),
			Arguments.of(CustomerType.ENTERPRISE, PartyType.ENTERPRISE));
	}
}
