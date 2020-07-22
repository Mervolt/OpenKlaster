package com.openklaster.cassandra.service;

import com.openklaster.common.model.SourceMeasurement;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.Date;

public class SourceMeasurementHandler extends CassandraHandler<SourceMeasurement> {
    public SourceMeasurementHandler(CassandraClient cassandraClient, JsonObject configObject) {
        super(cassandraClient, configObject, SourceMeasurement.class);
    }

    @Override
    public void createPostHandler(Message<JsonObject> message) {
        try {
            SourceMeasurement sourceMeasurement = parseToModel(message.body());
            if (sourceMeasurement.getTimestamp() == null)
                sourceMeasurement.setTimestamp(new Date());
            mapper.save(sourceMeasurement, addToDatabaseResultHandler(message, JsonObject.mapFrom(sourceMeasurement)));
        } catch (Exception e) {
            handleFailure(message, e.getMessage());
        }
    }
}
