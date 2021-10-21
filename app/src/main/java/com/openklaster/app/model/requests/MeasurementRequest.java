package com.openklaster.app.model.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.util.Date;

@Value
public class MeasurementRequest {
    String installationId;
    double value;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
    Date timestamp;
}
