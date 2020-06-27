package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EnergySourceCalculator {

    @JsonProperty("_id")
    private String sourceName;
    private Double energyValue;
}
