package com.openklaster.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openklaster.api.validation.ModelValidationErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Load {
    @NotBlank(message = ModelValidationErrorMessages.NAME)
    private String name;
    @NotBlank(message = ModelValidationErrorMessages.DESCRIPTION)
    private String description;
}
