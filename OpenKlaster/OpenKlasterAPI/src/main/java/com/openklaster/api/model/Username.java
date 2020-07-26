package com.openklaster.api.model;

import com.openklaster.api.validation.ModelValidationErrorMessages;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Username extends Model {
    @NotBlank(message = ModelValidationErrorMessages.USERNAME)
    private String username;
}
