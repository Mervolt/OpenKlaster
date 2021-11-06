package com.openklaster.app.measurements;

public interface Inverter <Credentials>{
    Measurement retrieveMeasurement(Credentials credentials);
}
