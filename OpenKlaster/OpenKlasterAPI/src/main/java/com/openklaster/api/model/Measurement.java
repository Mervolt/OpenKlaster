package com.openklaster.api.model;

import lombok.Data;


@Data
public class Measurement extends Model {
    private String timestamp;
    private int receiverId;
    private String unit;
    private double value;
}

