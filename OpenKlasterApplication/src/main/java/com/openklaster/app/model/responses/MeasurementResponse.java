package com.openklaster.app.model.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class MeasurementResponse {
    String installationId;
    double value;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
    Date timestamp;
    String unit;
}
