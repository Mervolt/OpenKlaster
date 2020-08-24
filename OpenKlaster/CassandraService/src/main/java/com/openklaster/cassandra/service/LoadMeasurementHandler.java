package com.openklaster.cassandra.service;

import com.openklaster.common.model.LoadMeasurement;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.Date;

public class LoadMeasurementHandler extends CassandraHandler<LoadMeasurement> {
    public LoadMeasurementHandler(CassandraClient cassandraClient, JsonObject configObject) {
        super(cassandraClient, configObject, LoadMeasurement.class);
    }

    @Override
    public void createPostHandler(Message<JsonObject> message) {
        try {
            LoadMeasurement loadMeasurement = parseToModel(message.body());
            if (loadMeasurement.getTimestamp() == null)
                loadMeasurement.setTimestamp(new Date());
            mapper.save(loadMeasurement, addToDatabaseResultHandler(message, JsonObject.mapFrom(loadMeasurement)));
        } catch (Exception e) {
            handleFailure(message, e.getMessage());
        }
    }
}
