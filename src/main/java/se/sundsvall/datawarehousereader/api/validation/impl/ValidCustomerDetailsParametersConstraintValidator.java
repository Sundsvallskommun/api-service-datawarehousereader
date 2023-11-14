package se.sundsvall.datawarehousereader.api.validation.impl;

import static org.apache.commons.lang3.StringUtils.isBlank;

import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsParameters;
import se.sundsvall.datawarehousereader.api.validation.ValidCustomerDetailsParameters;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCustomerDetailsParametersConstraintValidator implements ConstraintValidator<ValidCustomerDetailsParameters, CustomerDetailsParameters> {

	@Override
	public boolean isValid(final CustomerDetailsParameters value, final ConstraintValidatorContext context) {
		return !(isBlank(value.getCustomerEngagementOrgId()));
	}
}
