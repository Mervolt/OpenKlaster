package service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
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
    public Handler<RoutingContext> createPostHandler() {
        return routingContext -> {
            int id = parseInt(routingContext, idType);
            String source = routingContext.request().getParam("source");
            String type = routingContext.request().getParam("type");
            String description = routingContext.request().getParam("description");

            WeatherConditions weatherConditions = new WeatherConditions(new Date(), id, source, type, description);
            mapper.save(weatherConditions, handler(routingContext, weatherConditions.toString()));
        };
    }
}
