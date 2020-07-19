package com.openklaster.common.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;

@Table(keyspace = "openklaster", name = "loadmeasurement")
public class LoadMeasurement {
    @PartitionKey
    private Date timestamp;
    private int installationId;
    private String unit;
    private double value;

    public LoadMeasurement(int installationId, Date timestamp, String unit, double value) {
        this.installationId = installationId;
        this.timestamp = timestamp;
        this.unit = unit;
        this.value = value;
    }

    @Override
    public String toString() {
        return "LoadMeasurement{" +
                "timestamp=" + timestamp +
                ", receiverId=" + installationId +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}

