package com.openklaster.cassandra.service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import com.openklaster.common.model.WeatherConditions;

import java.util.Date;

public class WeatherConditionsHandler extends CassandraHandler {
    private final Mapper<WeatherConditions> mapper;

    public WeatherConditionsHandler(CassandraClient cassandraClient, JsonObject configObject) {
        super(cassandraClient, configObject);
        this.logger = LoggerFactory.getLogger(WeatherConditionsHandler.class);
        this.mapper = mappingManager.mapper(WeatherConditions.class);
    }

    @Override
    public void createPostHandler(Message<JsonObject> message) {
        try {
            String id = message.body().getString(idType);
            String source = message.body().getString("source");
            String type = message.body().getString("type");
            String description = message.body().getString("description");

            WeatherConditions weatherConditions = new WeatherConditions(new Date(), id, source, type, description);
            mapper.save(weatherConditions, handler(message, weatherConditions.toString()));
        } catch (Exception e) {
            parsingArgumentsError(message);
        }
    }
}
