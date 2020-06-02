package service;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import model.LoadMeasurement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadMeasurementHandler extends CassandraHandler {
    private final Mapper<LoadMeasurement> mapper;

    public LoadMeasurementHandler(CassandraClient cassandraClient, JsonObject configObject) {
        super(cassandraClient, configObject);
        logger = LoggerFactory.getLogger(LoadMeasurementHandler.class);
        this.mapper = mappingManager.mapper(LoadMeasurement.class);
    }

    @Override
    public Handler<RoutingContext> createPostHandler() {
        return routingContext -> {
            int id = parseInt(routingContext, idType);
            float value = parseFloat(routingContext, "value");
            String unit = parseUnit(routingContext);
            Date timestamp = parseTimestamp(routingContext);

            LoadMeasurement loadMeasurement = new LoadMeasurement(id, timestamp, unit, value);
            mapper.save(loadMeasurement, handler(routingContext, loadMeasurement.toString()));
        };
    }
}
