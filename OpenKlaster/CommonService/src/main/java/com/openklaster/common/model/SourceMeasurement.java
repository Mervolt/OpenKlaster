package com.openklaster.common.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(keyspace = "openklaster", name = "sourcemeasurement")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SourceMeasurement {
    @PartitionKey
    private Date timestamp;
    private String installationId;
    private String unit;
    private double value;
}
