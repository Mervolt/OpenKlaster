package com.openklaster.cassandra.service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import com.openklaster.common.model.EnergyPredictions;

import java.util.Date;

public class EnergyPredictionsHandler extends CassandraHandler {
    private final Mapper<EnergyPredictions> mapper;

    public EnergyPredictionsHandler(CassandraClient cassandraClient, JsonObject configObject) {
        super(cassandraClient, configObject);
        this.logger = LoggerFactory.getLogger(EnergyPredictionsHandler.class);
        this.mapper = mappingManager.mapper(EnergyPredictions.class);
    }

    @Override
    public void createPostHandler(Message<JsonObject> message) {
        try {
            String id = message.body().getString(idType);
            String source = message.body().getString("source");
            String type = message.body().getString("type");
            String description = message.body().getString("description");

            EnergyPredictions energyPredictions = new EnergyPredictions(new Date(), id, source, type, description);
            mapper.save(energyPredictions, handler(message, energyPredictions.toString()));
        } catch (Exception e) {
            parsingArgumentsError(message);
        }
    }
}
