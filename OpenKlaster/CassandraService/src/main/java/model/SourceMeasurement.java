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

    public SourceMeasurement() {
    }

    public int getInverterId() {
        return inverterId;
    }

    public void setInverterId(int inverterId) {
        this.inverterId = inverterId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

