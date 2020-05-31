package service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import model.EnergyPredictions;

import java.util.Date;

public class EnergyPredictionsHandler extends CassandraHandler {
    private final Mapper<EnergyPredictions> mapper;

    public EnergyPredictionsHandler(CassandraClient cassandraClient, JsonObject configObject) {
        super(cassandraClient, configObject);
        logger = LoggerFactory.getLogger(EnergyPredictionsHandler.class);
        this.mapper = mappingManager.mapper(EnergyPredictions.class);
    }

    @Override
    public Handler<RoutingContext> createPostHandler() {
        return routingContext -> {
            int id = parseInt(routingContext, idType);
            String source = routingContext.request().getParam("source");
            String type = routingContext.request().getParam("type");
            String description = routingContext.request().getParam("description");

            EnergyPredictions energyPredictions = new EnergyPredictions(new Date(), id, source, type, description);
            mapper.save(energyPredictions, handler(routingContext, energyPredictions.toString()));
        };
    }
}
