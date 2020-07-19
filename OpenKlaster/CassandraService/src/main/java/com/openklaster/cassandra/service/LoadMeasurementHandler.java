package com.openklaster.cassandra.service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import com.openklaster.common.model.LoadMeasurement;

import java.util.Date;

public class LoadMeasurementHandler extends CassandraHandler {
    private final Mapper<LoadMeasurement> mapper;

    public LoadMeasurementHandler(CassandraClient cassandraClient, JsonObject configObject) {
        super(cassandraClient, configObject);
        this.logger = LoggerFactory.getLogger(LoadMeasurementHandler.class);
        this.mapper = mappingManager.mapper(LoadMeasurement.class);
    }


    @Override
    public void createPostHandler(Message<JsonObject> message) {
        try {
            int id = message.body().getInteger(idType);
            float value = message.body().getFloat("value");
            // Todo It will be done, but first we must determine how to do it. I leave "kW" for now
            String unit = "kW";
            Date timestamp = parseTimestamp(message);

            LoadMeasurement loadMeasurement = new LoadMeasurement(timestamp, id, unit, value);
            mapper.save(loadMeasurement, handler(message, loadMeasurement.toString()));
        } catch (Exception e) {
            parsingArgumentsError(message);
        }
    }
}
