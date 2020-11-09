package com.openklaster.api.validation.annotation;

import com.openklaster.api.validation.CheckDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckDateValidator.class)
@Documented
public @interface CheckDateFormat {

    String message() default "Date must be in the format yyyy-MM-dd";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
