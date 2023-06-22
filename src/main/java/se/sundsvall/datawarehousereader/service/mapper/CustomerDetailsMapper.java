package se.sundsvall.datawarehousereader.service.mapper;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetails;
import se.sundsvall.datawarehousereader.integration.stadsbacken.model.customer.CustomerDetailsEntity;
import se.sundsvall.datawarehousereader.service.util.ServiceUtil;

public class CustomerDetailsMapper {

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
			.withInstalledChangedFlg(entity.isInstalledChangedFlg());
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


}
