package com.openklaster.app.model.responses;

import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.Map;

@Value
@Builder
public class InstallationSummaryResponse {
    double totalEnergy;
    double currentPower;
    double energyProducedToday;
    Map<Date, Double> power;
}

@Value
@Builder
class EnvBenefits {
    double co2Reduced;
    int treesSaved;
}