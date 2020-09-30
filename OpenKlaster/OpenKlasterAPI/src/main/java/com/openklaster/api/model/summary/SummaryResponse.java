package com.openklaster.api.model.summary;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class SummaryResponse {
    private Double totalEnergy;
    private Double currentPower;
    private Double energyProducedToday;
    private Map<String, Double> power;
    private EnvironmentalBenefits environmentalBenefits;
}
