package com.openklaster.api.model;

import com.openklaster.api.validation.ModelValidationErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Source {
    @NotNull(message = ModelValidationErrorMessages.AZIMUTH)
    private Integer azimuth;
    @NotNull(message = ModelValidationErrorMessages.TILT)
    private Integer tilt;
    @NotNull(message = ModelValidationErrorMessages.CAPACITY)
    private Integer capacity;
    @NotBlank(message = ModelValidationErrorMessages.DESCRIPTION)
    private String description;

}
