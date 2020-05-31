package model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;

@Table(keyspace = "openklaster", name = "sourcemeasurement")
public class SourceMeasurement {
    @PartitionKey
    private Date timestamp;
    private int inverterId;
    private String unit;
    private double value;

    public SourceMeasurement(int inverterId, Date timestamp, String unit, double value) {
        this.inverterId = inverterId;
        this.timestamp = timestamp;
        this.unit = unit;
        this.value = value;
    }

    @Override
    public String toString() {
        return "SourceMeasurement{" +
                "timestamp=" + timestamp +
                ", inverterId=" + inverterId +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}

