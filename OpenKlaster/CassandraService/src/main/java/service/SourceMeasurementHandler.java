package service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import model.SourceMeasurement;

import java.util.Date;

public class SourceMeasurementHandler extends CassandraHandler {
    private final Mapper<SourceMeasurement> mapper;

    public SourceMeasurementHandler(CassandraClient cassandraClient, JsonObject configObject) {
        super(cassandraClient, configObject);
        logger = LoggerFactory.getLogger(SourceMeasurementHandler.class);
        this.mapper = mappingManager.mapper(SourceMeasurement.class);
    }

    @Override
    public Handler<RoutingContext> createPostHandler() {
        return routingContext -> {
            int id = parseInt(routingContext, idType);
            float value = parseFloat(routingContext, "value");
            String unit = parseUnit(routingContext);

            SourceMeasurement sourceMeasurement = new SourceMeasurement(id, new Date(), unit, value);
            mapper.save(sourceMeasurement, handler(routingContext, sourceMeasurement.toString()));
        };
    }
}
