package openklaster.cassandra.service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import openklaster.cassandra.model.WeatherConditions;

import java.util.Date;

public class WeatherConditionsHandler extends CassandraHandler {
    private final Mapper<WeatherConditions> mapper;

    public WeatherConditionsHandler(CassandraClient cassandraClient) {
        super(cassandraClient, "/weatherconditions", "weatherconditions", "installationId");
        this.mapper = mappingManager.mapper(WeatherConditions.class);
    }

    public Handler<RoutingContext> postHandler() {
        return routingContext -> {
            String receiverIdString = routingContext.request().getParam(idType);
            String source = routingContext.request().getParam("source");
            String type = routingContext.request().getParam("type");
            String description = routingContext.request().getParam("description");

            int installationId;
            try {
                installationId = Integer.parseInt(receiverIdString);
            } catch(NumberFormatException | NullPointerException e) {
                routingContext.response()
                        .setStatusCode(400)
                        .end();
                return;
            }

            WeatherConditions weatherConditions = new WeatherConditions(new Date(), installationId, source, type, description);
            mapper.save(weatherConditions, handler(routingContext));
        };
    }
}
