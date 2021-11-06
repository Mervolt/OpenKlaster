package com.openklaster.app.measurements.growatt.model;

import lombok.Data;

@Data
public class GrowattLoginBack {
    int userId;
    int userLevel;
    boolean success;
}
