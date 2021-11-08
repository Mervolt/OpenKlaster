package com.openklaster.app.model.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openklaster.app.validation.installation.SafeInstallation;
import lombok.Value;

import java.util.Date;

@Value
public class MeasurementRequest {
    @SafeInstallation String installationId;
    double value;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
    Date timestamp;
}