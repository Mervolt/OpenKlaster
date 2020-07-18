package com.openklaster.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Measurement extends Model {
    private String timestamp;
    private int installationId;
    private String unit;
    private double value;
}

