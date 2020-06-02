package service;

import com.datastax.driver.core.Row;
import config.NestedConfigAccessor;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.MappingManager;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.ext.web.RoutingContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public abstract class CassandraHandler {
    protected static Logger logger;
    protected final CassandraClient cassandraClient;
    protected final MappingManager mappingManager;
    protected final NestedConfigAccessor config;
    protected final String route;
    protected final String table;
    protected final String idType;
    private static final String select = "SELECT JSON * FROM %s WHERE %s = %d AND timestamp >= '%s' AND timestamp <= '%s' ALLOW FILTERING";

    public CassandraHandler(CassandraClient cassandraClient, JsonObject configObject) {
        this.cassandraClient = cassandraClient;
        this.mappingManager = MappingManager.create(cassandraClient);
        this.config = new NestedConfigAccessor(configObject);
        this.route = config.getString("route");
        this.table = config.getString("table");
        this.idType = config.getString("idtype");
    }

    public String getRoute() {
        return this.route;
    }

    public abstract Handler<RoutingContext> createPostHandler();

    public Handler<RoutingContext> createGetHandler() {
        return routingContext -> {
            int id = parseInt(routingContext, idType);
            String startDate = parseDate(routingContext, "startDate");
            String endDate = parseDate(routingContext, "endDate");
            String query = String.format(select, table, idType.toLowerCase(), id, startDate, endDate);
            System.out.println(query);
            cassandraClient.executeWithFullFetch(query, resultHandler(routingContext));
        };
    }


    protected Handler<AsyncResult<Void>> handler(RoutingContext routingContext, String object) {
        return voidAsyncResult -> {
            if (voidAsyncResult.succeeded()) {
                logger.info("New entry in the database" + object);
                routingContext.response()
                        .setStatusCode(200)
                        .end();
            } else {
                logger.info("Unable to add entry to the database" + object);
                routingContext.response()
                        .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                        .end();
            }
        };
    }

    protected Handler<AsyncResult<List<Row>>> resultHandler(RoutingContext routingContext) {
        return listAsyncResult -> {
            if (listAsyncResult.succeeded()) {
                List<Row> rows = listAsyncResult.result();
                String response = getJsonResponse(rows);
                logger.info("Status code for GET request " + HttpResponseStatus.OK.code());
                routingContext.response()
                        .putHeader("content-type", config.getString("content-type"))
                        .end(response);
            } else {
                logger.info("Status code for GET request " + HttpResponseStatus.BAD_REQUEST.code());
                routingContext.response()
                        .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                        .end();
            }
        };
    }

    private String getJsonResponse(List<Row> rows) {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("[");
        for (int i = 0; i < rows.size(); i++) {
            if (i > 0) responseBuilder.append(",");
            responseBuilder.append(rows.get(i).getString(0));
        }
        responseBuilder.append("]");
        return responseBuilder.toString();
    }

    protected int parseInt(RoutingContext routingContext, String param) {
        String valueString = routingContext.request().getParam(param);
        int valueInt = 0;
        try {
            valueInt = Integer.parseInt(valueString);
        } catch (NumberFormatException | NullPointerException e) {
            logger.info("Problem with parsing argument " + param);
            routingContext.response()
                    .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end();
        }
        return valueInt;
    }

    protected float parseFloat(RoutingContext routingContext, String param) {
        String valueString = routingContext.request().getParam(param);
        float valueFloat = 0;
        try {
            valueFloat = Float.parseFloat(valueString);
        } catch (NumberFormatException | NullPointerException e) {
            logger.info("Problem with parsing argument " + param);
            routingContext.response()
                    .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end();
        }
        return valueFloat;
    }

    // TODO There will be changes here
    protected String parseUnit(RoutingContext routingContext) {
        String cumulativelyString = routingContext.request().getParam("cumulatively");
        return cumulativelyString != null && cumulativelyString.equals("yes") ? "kWH" : "kW";
    }

    protected String parseDate(RoutingContext routingContext, String param) {
        String dateToValdate = routingContext.request().getParam(param);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        formatter.setLenient(false);
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(dateToValdate);
        } catch (ParseException e) {
            logger.info("Problem with parsing argument " + param);
            routingContext.response()
                    .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                    .end();
        }
        return formatter.format(parsedDate).replace("_", " ");
    }

    protected Date parseTimestamp(RoutingContext routingContext) {
        String timestamp = routingContext.request().getParam("timestamp");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        formatter.setLenient(false);
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(timestamp);
        } catch (ParseException e) {
            return new Date();
        }
        return parsedDate;
    }
}
