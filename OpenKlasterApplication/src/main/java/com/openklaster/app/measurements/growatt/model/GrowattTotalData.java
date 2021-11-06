package com.openklaster.app.measurements.growatt.model;

import lombok.Data;

@Data
public class GrowattTotalData {
    private String currentPowerSum;
    private String CO2Sum;
    private String isHaveStorage;
    private String eTotalMoneyText;
    private String todayEnergySum;
    private String totalEnergySum;
}
