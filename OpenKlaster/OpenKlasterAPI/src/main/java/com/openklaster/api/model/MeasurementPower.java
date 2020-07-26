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
public class MeasurementPower extends Model {
    private String timestamp;
    @NotBlank(message = ModelValidationErrorMessages.INSTALLATION_ID)
    private String installationId;
    private String unit = "kW";
    @NotNull(message = ModelValidationErrorMessages.VALUE)
    private double value;
}

