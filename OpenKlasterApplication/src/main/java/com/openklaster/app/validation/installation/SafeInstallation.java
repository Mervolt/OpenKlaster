package com.openklaster.app.validation.installation;

import org.springframework.validation.annotation.Validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SafeInstallationValidator.class)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Validated
public @interface SafeInstallation {
    String message() default "{Installation not found}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
