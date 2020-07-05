package com.openklaster.cassandra.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;

@Table(keyspace = "openklaster", name = "loadmeasurement")
public class LoadMeasurement {
    @PartitionKey
    private Date timestamp;
    private int receiverId;
    private String unit;
    private double value;

    public LoadMeasurement(int receiverId, Date timestamp, String unit, double value) {
        this.receiverId = receiverId;
        this.timestamp = timestamp;
        this.unit = unit;
        this.value = value;
    }

    @Override
    public String toString() {
        return "LoadMeasurement{" +
                "timestamp=" + timestamp +
                ", receiverId=" + receiverId +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}

