package service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import model.SourceMeasurement;

import java.util.Date;

public class SourceMeasurementHandler extends CassandraHandler {
    private final Mapper<SourceMeasurement> mapper;

    public SourceMeasurementHandler(CassandraClient cassandraClient) {
        super(cassandraClient, "/sourcemeasurement", "sourcemeasurement", "inverterId");
        this.mapper = mappingManager.mapper(SourceMeasurement.class);
    }

    public Handler<RoutingContext> postHandler() {
        return routingContext -> {
            String receiverIdString = routingContext.request().getParam(idType);
            String valueString = routingContext.request().getParam("value");
            String cumulativelyString = routingContext.request().getParam("cumulatively");
            int inverterId;
            double value;
            try {
                inverterId = Integer.parseInt(receiverIdString);
                value = Double.parseDouble(valueString);
            } catch(NumberFormatException | NullPointerException e) {
                routingContext.response()
                        .setStatusCode(400)
                        .end();
                return;
            }
            String unit = cumulativelyString != null && cumulativelyString.equals("yes") ? "kWH" : "kW";

            SourceMeasurement sourceMeasurement = new SourceMeasurement(inverterId, new Date(), unit, value);
            mapper.save(sourceMeasurement, handler(routingContext));
        };
    }
}
