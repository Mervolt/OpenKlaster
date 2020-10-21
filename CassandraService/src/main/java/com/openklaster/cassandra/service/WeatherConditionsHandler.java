package com.openklaster.cassandra.service;

import com.openklaster.common.model.WeatherConditions;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.Date;

public class WeatherConditionsHandler extends CassandraHandler<WeatherConditions> {
    public WeatherConditionsHandler(CassandraClient cassandraClient, String address, String table) {
        super(cassandraClient, WeatherConditions.class, address, table);
    }

    @Override
    public void createPostHandler(Message<JsonObject> message) {
        try {
            WeatherConditions weatherConditions = parseToModel(message.body());
            if (weatherConditions.getTimestamp() == null)
                weatherConditions.setTimestamp(new Date());
            mapper.save(weatherConditions, addToDatabaseResultHandler(message, JsonObject.mapFrom(weatherConditions)));
        } catch (Exception e) {
            handleFailure(message, e.getMessage());
        }
    }
}
