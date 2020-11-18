package com.openklaster.api.validation;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.validation.annotation.TokenNotRequired;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationExecutor {
    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public static <T> void validate(final T objectToValidate, Map<String, String> tokens) throws ValidationException {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> validationMessages = validator.validate(objectToValidate);
        List<String> messages = validationMessages.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        validateToken(objectToValidate, tokens, messages);
        if (!messages.isEmpty()) {
            throw new ValidationException(messages.toString());
        }
    }

    private static void validateToken(Object object, Map<String, String> tokens, List<String> messages) {
        Class<?> clazz = object.getClass();
        if (shouldValidateToken(clazz, tokens)) {
            messages.add(ModelValidationErrorMessages.TOKEN_REQUIRED);
        }
    }

    private static boolean shouldValidateToken(Class<?> clazz, Map<String, String> tokens) {
        return !clazz.isAnnotationPresent(TokenNotRequired.class) && isThereAtLeastOneToken(tokens);
    }

    private static boolean isThereAtLeastOneToken(Map<String, String> tokens) {
        return (!isTokenPresent(tokens, HandlerProperties.apiToken)) &&
                (!isTokenPresent(tokens, HandlerProperties.sessionToken)) &&
                (!isTokenPresent(tokens, HandlerProperties.technicalToken));
    }

    private static boolean isTokenPresent(Map<String, String> tokens, String tokenType) {
        return tokens.containsKey(tokenType) && StringUtils.isNotBlank(tokens.get(tokenType));
    }
}
