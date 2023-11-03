package se.sundsvall.datawarehousereader.api.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsParameters;
import se.sundsvall.datawarehousereader.api.validation.ValidCustomerDetailsParameters;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.ObjectUtils.isEmpty;

public class ValidCustomerDetailsParametersConstraintValidator implements ConstraintValidator<ValidCustomerDetailsParameters, CustomerDetailsParameters> {

	@Override
	public boolean isValid(final CustomerDetailsParameters value, final ConstraintValidatorContext context) {
		return !(isEmpty(value.getPartyId()) && isBlank(value.getCustomerEngagementOrgId()));
	}
}
