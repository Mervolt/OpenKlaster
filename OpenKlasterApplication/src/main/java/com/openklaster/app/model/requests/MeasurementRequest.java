package com.openklaster.app.model.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openklaster.app.validation.installation.SafeInstallation;
import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Value
@Builder
public class MeasurementRequest {
    @SafeInstallation String installationId;
    double value;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
    Date timestamp;
}
