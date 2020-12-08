package com.openklaster.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openklaster.api.validation.ModelValidationErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Source {
    @NotNull(message = ModelValidationErrorMessages.AZIMUTH)
    private Integer azimuth;
    @NotNull(message = ModelValidationErrorMessages.TILT)
    private Integer tilt;
    @NotNull(message = ModelValidationErrorMessages.CAPACITY)
    private Integer capacity;
    private String description;
}
