package com.openklaster.api.model;

import lombok.Data;

import java.util.Date;

@Data
public class MeasurementRequest extends Model {
    private Integer receiverId;
    private String startDate;
    private String endDate;
}

