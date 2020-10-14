package com.openklaster.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openklaster.api.validation.ModelValidationErrorMessages;
import com.openklaster.common.model.Credentials;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Inverter {
    @NotBlank(message = ModelValidationErrorMessages.DESCRIPTION)
    private String description;
    @NotBlank(message = ModelValidationErrorMessages.MANUFACTURER)
    private String manufacturer;
    @NotNull(message = ModelValidationErrorMessages.CREDENTRAILS)
    private Credentials credentials;
    @NotBlank(message = ModelValidationErrorMessages.MODEL_TYPE)
    private String modelType;
}
