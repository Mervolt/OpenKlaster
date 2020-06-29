package model;

import lombok.Data;

@Data
public class EnergySourceCalculator implements Model{
    private String sourceName;
    private Double energyValue;
}
