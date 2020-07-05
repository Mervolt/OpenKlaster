package openklaster.common.model;

import lombok.Data;

@Data
public class EnergySourceCalculator extends Model{
    private String sourceName;
    private Double energyValue;
}
