package com.openklaster.cassandra.service;

import com.openklaster.common.model.EnergyPredictions;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.Date;

public class EnergyPredictionsHandler extends CassandraHandler<EnergyPredictions> {
    public EnergyPredictionsHandler(CassandraClient cassandraClient, JsonObject configObject) {
        super(cassandraClient, configObject, EnergyPredictions.class);
    }

    @Override
    public void createPostHandler(Message<JsonObject> message) {
        try {
            EnergyPredictions energyPredictions = parseToModel(message.body());
            if (energyPredictions.getTimestamp() == null)
                energyPredictions.setTimestamp(new Date());
            mapper.save(energyPredictions, addToDatabaseResultHandler(message, JsonObject.mapFrom(energyPredictions)));
        } catch (Exception e) {
            handleFailure(message, e.getMessage());
        }
    }
}
