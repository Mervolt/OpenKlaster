package service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import model.EnergyPredictions;

import java.util.Date;

public class EnergyPredictionsHandler extends CassandraHandler {
    private final Mapper<EnergyPredictions> mapper;

    public EnergyPredictionsHandler(CassandraClient cassandraClient) {
        super(cassandraClient, "/energypredictions/", "energypredictions", "installationId");
        this.mapper = mappingManager.mapper(EnergyPredictions.class);
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

            EnergyPredictions energyPredictions = new EnergyPredictions(new Date(), installationId, source, type, description);
            mapper.save(energyPredictions, handler(routingContext));
        };
    }
}
