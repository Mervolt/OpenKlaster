package com.openklaster.api.model;

import com.openklaster.api.validation.ModelValidationErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostInstallation extends Model {

    @NotBlank(message = ModelValidationErrorMessages.USERNAME)
    private String username;
    @NotNull(message = ModelValidationErrorMessages.INVERTER)
    private Inverter inverter;
    @NotNull(message = ModelValidationErrorMessages.LOAD)
    private Load load;
    @NotNull(message = ModelValidationErrorMessages.SOURCE)
    private Source source;
    @NotBlank(message = ModelValidationErrorMessages.INSTALLATION_TYPE)
    private String installationType;
    @NotNull(message = ModelValidationErrorMessages.LONGITUDE)
    private Double longitude;
    @NotNull(message = ModelValidationErrorMessages.LATITUDE)
    private Double latitude;
    @NotBlank(message = ModelValidationErrorMessages.DESCRIPTION)
    private String description;
}
