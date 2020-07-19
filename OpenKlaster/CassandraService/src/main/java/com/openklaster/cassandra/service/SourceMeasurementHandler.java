package com.openklaster.cassandra.service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import com.openklaster.common.model.SourceMeasurement;

import java.util.Date;

public class SourceMeasurementHandler extends CassandraHandler {
    private final Mapper<SourceMeasurement> mapper;

    public SourceMeasurementHandler(CassandraClient cassandraClient, JsonObject configObject) {
        super(cassandraClient, configObject);
        this.logger = LoggerFactory.getLogger(SourceMeasurementHandler.class);
        this.mapper = mappingManager.mapper(SourceMeasurement.class);
    }

    @Override
    public void createPostHandler(Message<JsonObject> message) {
        try {
            String id = message.body().getString(idType);
            float value = message.body().getFloat("value");
            // Todo It will be done, but first we must determine how to do it. I leave "kW" for now
            String unit = "kW";
            Date timestamp = parseTimestamp(message);

            SourceMeasurement sourceMeasurement = new SourceMeasurement(timestamp, id, unit, value);
            mapper.save(sourceMeasurement, handler(message, sourceMeasurement.toString()));
        } catch (Exception e) {
            parsingArgumentsError(message);
        }
    }
}
