package se.sundsvall.datawarehousereader.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import se.sundsvall.datawarehousereader.api.validation.impl.ValidSortByPropertyConstraintValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidSortByPropertyConstraintValidator.class)
public @interface ValidSortByProperty {
	String message() default "one or more of properties in list are not present in entity.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
