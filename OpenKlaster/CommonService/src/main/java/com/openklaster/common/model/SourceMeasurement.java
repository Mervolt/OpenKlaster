package com.openklaster.common.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(keyspace = "openklaster", name = "sourcemeasurement")
@NoArgsConstructor
@AllArgsConstructor
public class SourceMeasurement {
    @PartitionKey
    private Date timestamp;
    private int installationId;
    private String unit;
    private double value;

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

