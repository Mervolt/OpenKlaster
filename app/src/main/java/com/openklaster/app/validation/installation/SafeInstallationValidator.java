package com.openklaster.app.validation.installation;

import com.openklaster.app.persistence.mongo.installation.SafeInstallationAccessor;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class SafeInstallationValidator implements ConstraintValidator<SafeInstallation, String> {
    private final SafeInstallationAccessor safeInstallationAccessor;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            safeInstallationAccessor.getCurrentUserInstallation(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
