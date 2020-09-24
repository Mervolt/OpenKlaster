package com.openklaster.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class Measurement extends Model {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
    protected Date timestamp;
    protected String installationId;
    protected Unit unit;
    protected double value;
}
