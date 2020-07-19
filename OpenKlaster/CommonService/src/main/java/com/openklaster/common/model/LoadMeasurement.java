package com.openklaster.common.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(keyspace = "openklaster", name = "loadmeasurement")
@NoArgsConstructor
@AllArgsConstructor
public class LoadMeasurement {
    @PartitionKey
    private Date timestamp;
    private int installationId;
    private String unit;
    private double value;

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

