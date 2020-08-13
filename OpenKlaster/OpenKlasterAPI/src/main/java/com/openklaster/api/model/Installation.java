package com.openklaster.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openklaster.api.validation.ModelValidationErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Installation extends Model {
    @NotBlank(message = ModelValidationErrorMessages.INSTALLATION_ID)
    private String installationId;
    @NotBlank(message = ModelValidationErrorMessages.USERNAME)
    private String username;
    @Valid
    @NotNull(message = ModelValidationErrorMessages.INVERTER)
    private Inverter inverter;
    @Valid
    @NotNull(message = ModelValidationErrorMessages.LOAD)
    private Load load;
    @Valid
    @NotNull(message = ModelValidationErrorMessages.SOURCE)
    private Source source;
    @NotBlank(message = ModelValidationErrorMessages.INSTALLATION_TYPE)
    private String installationType;
    @NotNull(message = ModelValidationErrorMessages.LONGITUDE)
    private Double longitude;
    @NotNull(message = ModelValidationErrorMessages.LATITUDE)
    private Double latitude;
    @NotNull(message = ModelValidationErrorMessages.DESCRIPTION)
    private String description;
}
