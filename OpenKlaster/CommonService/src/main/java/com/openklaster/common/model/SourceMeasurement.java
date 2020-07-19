package com.openklaster.common.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;

@Table(keyspace = "openklaster", name = "sourcemeasurement")
public class SourceMeasurement {
    @PartitionKey
    private Date timestamp;
    private int installationId;
    private String unit;
    private double value;

    public SourceMeasurement(int installationId, Date timestamp, String unit, double value) {
        this.installationId = installationId;
        this.timestamp = timestamp;
        this.unit = unit;
        this.value = value;
    }

    @Override
    public String toString() {
        return "SourceMeasurement{" +
                "timestamp=" + timestamp +
                ", inverterId=" + installationId +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}

