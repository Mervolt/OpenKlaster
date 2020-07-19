package com.openklaster.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnergySourceCalculator {

    @JsonProperty("_id")
    private String sourceName;
    private Double energyValue;
}
