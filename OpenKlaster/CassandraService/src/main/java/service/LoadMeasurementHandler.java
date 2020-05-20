package service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import model.LoadMeasurement;

import java.util.Date;

public class LoadMeasurementHandler extends CassandraHandler {
    private final Mapper<LoadMeasurement> mapper;

    public LoadMeasurementHandler(CassandraClient cassandraClient) {
        super(cassandraClient, "/loadmeasurement", "loadmeasurement", "receiverId");
        this.mapper = mappingManager.mapper(LoadMeasurement.class);
    }

    public Handler<RoutingContext> postHandler() {
        return routingContext -> {
            String receiverIdString = routingContext.request().getParam(idType);
            String valueString = routingContext.request().getParam("value");
            String cumulativelyString = routingContext.request().getParam("cumulatively");

            int receiverId;
            double value;
            try {
                receiverId = Integer.parseInt(receiverIdString);
                value = Double.parseDouble(valueString);
            } catch(NumberFormatException | NullPointerException e) {
                routingContext.response()
                        .setStatusCode(400)
                        .end();
                return;
            }
            String unit = cumulativelyString != null && cumulativelyString.equals("yes") ? "kWH" : "kW";

            LoadMeasurement loadMeasurement = new LoadMeasurement(receiverId, new Date(), unit, value);
            mapper.save(loadMeasurement, handler(routingContext));
        };
    }
}
