package com.openklaster.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openklaster.api.validation.ModelValidationErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasurementEnergy extends Model {
    private String timestamp;
    @NotBlank(message = ModelValidationErrorMessages.INSTALLATION_ID)
    private String installationId;
    private final Unit unit = Unit.kWh;
    @NotNull(message = ModelValidationErrorMessages.VALUE)
    private double value;
}

