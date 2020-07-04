package openklaster.cassandra.model;

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

    public LoadMeasurement() {
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
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

