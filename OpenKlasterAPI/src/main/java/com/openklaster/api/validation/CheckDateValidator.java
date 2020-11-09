package com.openklaster.api.validation;

import com.openklaster.api.validation.annotation.CheckDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckDateValidator implements ConstraintValidator<CheckDateFormat, String> {

    private static final String pattern = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";

    @Override
    public void initialize(CheckDateFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if ( object == null ) {
            return true;
        }
        Pattern datePattern = Pattern.compile(pattern);
        Matcher matcher = datePattern.matcher(object);
        if (matcher.matches()) {
            return true;
        }
        else {
            return false;
        }
    }
}
