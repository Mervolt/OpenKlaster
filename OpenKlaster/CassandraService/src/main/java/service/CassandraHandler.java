package service;

import com.datastax.driver.core.Row;
import config.NestedConfigAccessor;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.MappingManager;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public abstract class CassandraHandler {
    public static final String OK = "200";
    public static final String BAD_REQUEST = "400";
    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String select = "SELECT JSON * FROM %s WHERE %s = %d AND timestamp >= '%s' AND timestamp <= '%s' ALLOW FILTERING";
    protected static Logger logger;
    protected final CassandraClient cassandraClient;
    protected final MappingManager mappingManager;
    protected final NestedConfigAccessor config;
    protected final String address;
    protected final String table;
    protected final String idType;

    public CassandraHandler(CassandraClient cassandraClient, JsonObject configObject) {
        this.cassandraClient = cassandraClient;
        this.mappingManager = MappingManager.create(cassandraClient);
        this.config = new NestedConfigAccessor(configObject);
        this.address = config.getString("address");
        this.table = config.getString("table");
        this.idType = config.getString("idtype");
    }

    public String getAddress() {
        return this.address;
    }

    public abstract void createPostHandler(Message<JsonObject> message);

    public void createGetHandler(Message<JsonObject> message) {
        try {
            int id = message.body().getInteger(idType);
            String startDate = valdateDate(message, "startDate");
            String endDate =  valdateDate(message,"endDate");
            String query = String.format(select, table, idType.toLowerCase(), id, startDate, endDate);

            cassandraClient.executeWithFullFetch(query, listAsyncResult -> {
                DeliveryOptions deliveryOptions = new DeliveryOptions();
                if (listAsyncResult.succeeded()) {
                    List<Row> rows = listAsyncResult.result();
                    JsonArray response = getJsonResponse(rows);
                    logger.debug("Status code for GET request " + OK);
                    deliveryOptions.addHeader("statusCode", OK);
                    message.reply(response, deliveryOptions);
                } else {
                    logger.error("Status code for GET request " + BAD_REQUEST);
                    deliveryOptions.addHeader("statusCode", BAD_REQUEST);
                    message.reply(new JsonObject(), deliveryOptions);
                }
            });
        } catch (Exception e) {
            parsingArgumentsError(message);
        }
    }

    protected Handler<AsyncResult<Void>> handler(Message<JsonObject> message, String object) {
        return voidAsyncResult -> {
            DeliveryOptions deliveryOptions = new DeliveryOptions();
            if (voidAsyncResult.succeeded()) {
                logger.debug("New entry in the database " + object);
                deliveryOptions.addHeader("statusCode", OK);
                message.reply(new JsonObject(), deliveryOptions);
            } else {
                logger.error("Unable to add entry to the database " + object);
                deliveryOptions.addHeader("statusCode", BAD_REQUEST);
                message.reply(new JsonObject(), deliveryOptions);
            }
        };
    }

    private JsonArray getJsonResponse(List<Row> rows) {
        JsonArray jsonArray = new JsonArray();
        for (Row row : rows) {
            jsonArray.add(new JsonObject(row.getString(0)));
        }
        return jsonArray;
    }

    protected String valdateDate(Message<JsonObject> message, String param) throws ParseException {
        String dateToValidate = message.body().getString(param);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(false);
        Date parsedDate = formatter.parse(dateToValidate);
        return formatter.format(parsedDate);
    }

    protected Date parseTimestamp(Message<JsonObject> message) throws ParseException {
        String timestamp = message.body().getString("timestamp");
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(false);
        if (timestamp != null) {
            return formatter.parse(timestamp);
        }
        else {
            return new Date();
        }
    }

    public void parsingArgumentsError(Message<JsonObject> message) {
        logger.error("Problem with parsing arguments");
        DeliveryOptions deliveryOptions = new DeliveryOptions();
        deliveryOptions.addHeader("statusCode", BAD_REQUEST);
        message.reply(new JsonObject(), deliveryOptions);
    }
}
