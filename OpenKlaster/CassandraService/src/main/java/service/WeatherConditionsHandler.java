package service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import model.WeatherConditions;

import java.util.Date;

public class WeatherConditionsHandler extends CassandraHandler {
    private final Mapper<WeatherConditions> mapper;

    public WeatherConditionsHandler(CassandraClient cassandraClient, JsonObject configObject) {
        super(cassandraClient, configObject);
        logger = LoggerFactory.getLogger(WeatherConditionsHandler.class);
        this.mapper = mappingManager.mapper(WeatherConditions.class);
    }

    @Override
    public void createPostHandler(Message<JsonObject> message) {
        try {
            int id = message.body().getInteger(idType);
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
