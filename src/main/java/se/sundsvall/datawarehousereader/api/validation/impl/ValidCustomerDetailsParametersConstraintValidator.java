package se.sundsvall.datawarehousereader.api.validation.impl;

import static org.apache.commons.lang3.StringUtils.isBlank;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import se.sundsvall.datawarehousereader.api.model.customer.CustomerDetailsParameters;
import se.sundsvall.datawarehousereader.api.validation.ValidCustomerDetailsParameters;

public class ValidCustomerDetailsParametersConstraintValidator implements ConstraintValidator<ValidCustomerDetailsParameters, CustomerDetailsParameters> {

	@Override
	public boolean isValid(final CustomerDetailsParameters value, final ConstraintValidatorContext context) {
		return !(isBlank(value.getCustomerEngagementOrgId()));
	}
}
