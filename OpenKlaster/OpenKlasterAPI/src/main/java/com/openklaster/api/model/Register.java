package com.openklaster.api.model;

import com.openklaster.api.validation.ModelValidationErrorMessages;
import com.openklaster.api.validation.TokenNotRequired;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@TokenNotRequired
public class Register extends Model {
    @NotBlank(message = ModelValidationErrorMessages.USERNAME)
    private String username;
    @NotBlank(message = ModelValidationErrorMessages.PASSWORD)
    private String password;
    @NotBlank(message = ModelValidationErrorMessages.EMAIL)
    private String email;
}
