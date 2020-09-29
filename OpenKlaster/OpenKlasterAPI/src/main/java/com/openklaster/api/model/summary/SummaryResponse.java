package com.openklaster.api.model.summary;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
public class SummaryResponse {
    private Double totalEnergy;
    private Map<String, Double> power;
    private EnvironmentalBenefits environmentalBenefits;
}
