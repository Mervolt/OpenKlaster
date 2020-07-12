package com.openklaster.api.model;

import lombok.Data;

@Data
public class EnergySourceCalculator extends Model {
    private String sourceName;
    private Double energyValue;
}
