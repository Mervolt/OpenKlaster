package com.openklaster.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openklaster.api.validation.ModelValidationErrorMessages;
import com.openklaster.api.validation.TokenNotRequired;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@TokenNotRequired
@JsonIgnoreProperties(ignoreUnknown = true)
public class Login extends Model{
    @NotBlank(message = ModelValidationErrorMessages.USERNAME)
    private String username;
    @NotBlank(message = ModelValidationErrorMessages.PASSWORD)
    private String password;
}
