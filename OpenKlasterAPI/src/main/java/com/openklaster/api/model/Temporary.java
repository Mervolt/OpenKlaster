package com.openklaster.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openklaster.api.validation.TokenNotRequired;
import lombok.Data;

@Data
@TokenNotRequired
@JsonIgnoreProperties(ignoreUnknown = true)
public class Temporary extends Model {
    // Todo fields and validation for chart
}
