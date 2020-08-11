package com.openklaster.api.validation;

import com.openklaster.api.handler.properties.HandlerProperties;

import javax.validation.*;
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
        if (!clazz.isAnnotationPresent(TokenNotRequired.class) &&
           (!isTokenPresent(tokens, HandlerProperties.apiToken)) &&
           (!isTokenPresent(tokens, HandlerProperties.sessionToken))) {
                messages.add(ModelValidationErrorMessages.TOKEN_REQUIRED);
        }
    }

    private static boolean isTokenPresent(Map<String, String> tokens, String tokenType) {
        return tokens.containsKey(tokenType)
                && tokens.get(tokenType) != null
                && !tokens.get(tokenType).equals("");
    }

}
