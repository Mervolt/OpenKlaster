package com.openklaster.app.measurements.growatt.model;

import lombok.Data;

@Data
public class GrowattData {
    private String plantMoneyText;
    private String plantName;
    private String plantId;
    private String isHaveStorage;
    private String todayEnergy;
    private String totalEnergy;
    private String currentPower;
}
