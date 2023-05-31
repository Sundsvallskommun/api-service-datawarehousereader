package se.sundsvall.datawarehousereader.service.mapper;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import se.sundsvall.datawarehousereader.api.model.CustomerType;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetails;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailsEntity;
import se.sundsvall.datawarehousereader.service.util.ServiceUtil;

public class CustomerDetailsMapper {

	private static final String PERSONAL_NUMBER_REGEX = "(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])-\\d{4}";

	private CustomerDetailsMapper() {}

	public static List<CustomerDetails> toCustomerDetails(List<CustomerDetailsEntity> entities) {
		return ofNullable(entities).orElse(emptyList()).stream()
			.map(CustomerDetailsMapper::toCustomerDetails)
			.toList();
	}

	private static CustomerDetails toCustomerDetails(final CustomerDetailsEntity entity) {
		return CustomerDetails.create()
			.withCustomerOrgNumber(entity.getCustomerOrgId())
			.withCustomerNumber(ServiceUtil.toString(entity.getCustomerId()))
			.withCustomerName(entity.getName())
			.withCareOf(entity.getCo())
			.withStreet(entity.getAddress())
			.withPostalCode(entity.getZipcode())
			.withCity(entity.getCity())
			.withPhoneNumbers(extractPhoneNumbers(entity))
			.withEmailAddresses(extractEmails(entity))
			.withCustomerCategoryID(entity.getCustomerCategoryID())
			.withCustomerCategoryDescription(entity.getCustomerCategoryDescription())
			.withCustomerChangedFlg(entity.isCustomerChangedFlg())
			.withInstalledChangedFlg(entity.isInstalledChangedFlg())
			.withCustomerType(extractCustomerType(entity.getCustomerOrgId()));
	}

	private static List<String> extractPhoneNumbers(final CustomerDetailsEntity entity) {
		return Stream.of(entity.getPhone1(), entity.getPhone2(), entity.getPhone3())
			.filter(Objects::nonNull)
			.toList();

	}

	private static List<String> extractEmails(final CustomerDetailsEntity entity) {
		return Stream.of(entity.getEmail1(), entity.getEmail2())
			.filter(Objects::nonNull)
			.toList();
	}

	private static CustomerType extractCustomerType(String customerOrgId) {
		return ofNullable(customerOrgId)
			.filter(StringUtils::isNotBlank)
			.map(string -> string.matches(PERSONAL_NUMBER_REGEX) ? CustomerType.PRIVATE : CustomerType.ENTERPRISE)
			.orElse(null);

	}

}