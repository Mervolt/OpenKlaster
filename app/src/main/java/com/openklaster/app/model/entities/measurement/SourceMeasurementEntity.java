package com.openklaster.app.model.entities.measurement;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Value
@Builder
@Table(value = "sourcemeasurement")
public class SourceMeasurementEntity {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    String installationId;
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    Date timestamp;
    @Column
    double value;
    @Column
    String unit;
}
