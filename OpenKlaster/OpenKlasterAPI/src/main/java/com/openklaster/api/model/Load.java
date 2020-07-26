package com.openklaster.api.model;

import com.openklaster.api.validation.ModelValidationErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Load {
    @NotBlank(message = ModelValidationErrorMessages.NAME)
    private String name;
    @NotBlank(message = ModelValidationErrorMessages.DESCRIPTION)
    private String description;
}
