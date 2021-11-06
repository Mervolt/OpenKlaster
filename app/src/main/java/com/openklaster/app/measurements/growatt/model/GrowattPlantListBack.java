package com.openklaster.app.measurements.growatt.model;

import lombok.Data;

import java.util.List;

@Data
public class GrowattPlantListBack {
    private List<GrowattData> data;
    private GrowattTotalData totalData;
    private boolean success;
}
