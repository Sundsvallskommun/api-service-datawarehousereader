package se.sundsvall.datawarehousereader.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import se.sundsvall.datawarehousereader.api.validation.impl.ValidCustomerDetailsParametersConstraintValidator;

@Documented
@Target({
	ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidCustomerDetailsParametersConstraintValidator.class)
public @interface ValidCustomerDetailsParameters {

	String message() default "'customerEngagementOrgId' must be provided";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
