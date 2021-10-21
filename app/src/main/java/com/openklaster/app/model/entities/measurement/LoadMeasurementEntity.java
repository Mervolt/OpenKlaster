package com.openklaster.app.model.entities.measurement;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Value
@Builder
@Table(value = "loadmeasurement")
public class LoadMeasurementEntity {
    @PrimaryKeyColumn
    String installationId;
    @Column
    double value;
    @PrimaryKeyColumn
    Date timestamp;
    @PrimaryKeyColumn
    String unit;
}
