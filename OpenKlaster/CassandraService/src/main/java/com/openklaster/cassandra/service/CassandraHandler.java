package com.openklaster.cassandra.service;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.Row;
import com.openklaster.cassandra.properties.CassandraProperties;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.messages.BusMessageReplyUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.Mapper;
import io.vertx.cassandra.MappingManager;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

public abstract class CassandraHandler<T> {
    protected final CassandraClient cassandraClient;
    protected final MappingManager mappingManager;
    protected final NestedConfigAccessor config;
    protected final Class<T> modelClass;
    protected final Logger logger;
    protected final Mapper<T> mapper;
    protected final String address;
    protected final String table;

    public CassandraHandler(CassandraClient cassandraClient, JsonObject configObject, Class<T> modelClass) {
        this.cassandraClient = cassandraClient;
        this.mappingManager = MappingManager.create(cassandraClient);
        this.config = new NestedConfigAccessor(configObject);
        this.modelClass = modelClass;
        this.logger = LoggerFactory.getLogger(modelClass);
        this.mapper = mappingManager.mapper(modelClass);
        this.address = config.getString(CassandraProperties.ADDRESS);
        this.table = config.getString(CassandraProperties.TABLE);
    }

    public String getAddress() {
        return this.address;
    }

    public abstract void createPostHandler(Message<JsonObject> message);

    public void createGetHandler(Message<JsonObject> message) {
        try {
            String query = getMeasurementsQuery(message);
            cassandraClient.executeWithFullFetch(query, listAsyncResult -> {
                if (listAsyncResult.succeeded()) {
                    JsonArray response = getJsonResponse(listAsyncResult.result());
                    logger.debug("GET request executed successfully");
                    BusMessageReplyUtils.replyWithBodyAndStatus(message, response, HttpResponseStatus.OK);
                } else {
                    // Todo nie pamiętam co to wypisuje xd można dodać coś ze dla geta jesli nie ma teraz xd
                    logger.error(listAsyncResult.cause());
                    BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.BAD_REQUEST, listAsyncResult.cause().toString());
                }
            });
        } catch (Exception e) {
            handleFailure(message, e.getMessage());
        }
    }

    protected Handler<AsyncResult<Void>> addToDatabaseResultHandler(Message<JsonObject> message, JsonObject response) {
        return voidAsyncResult -> {
            if (voidAsyncResult.succeeded()) {
                logger.debug("New entry in the database " + response);
                BusMessageReplyUtils.replyWithBodyAndStatus(message, response, HttpResponseStatus.OK);
            } else {
                logger.error(voidAsyncResult.cause());
                BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.BAD_REQUEST, voidAsyncResult.cause().toString());

            }
        };
    }

    private JsonArray getJsonResponse(List<Row> rows) {
        return rows.stream()
                .map(this::parseRow)
                .collect(JsonArray::new, JsonArray::add, JsonArray::add);
    }

    private JsonObject parseRow(Row row) {
        DateFormat dateFormat = new SimpleDateFormat(CassandraProperties.DATETIME_FORMAT);
        JsonObject jsonObject = new JsonObject();
        for (ColumnDefinitions.Definition column : row.getColumnDefinitions()) {
            switch(column.getType().toString()) {
                case CassandraProperties.TIMESTAMP:
                    jsonObject.put(column.getName(), dateFormat.format(row.getTimestamp(column.getName())));
                    break;
                case CassandraProperties.VARCHAR:
                    jsonObject.put(column.getName(), row.getString(column.getName()));
                    break;
                case CassandraProperties.DOUBLE:
                    jsonObject.put(column.getName(), row.getDouble(column.getName()));
                    break;
                default:
                    break;
            }
        }
        return jsonObject;
    }


    public void handleFailure(Message<JsonObject> message, String errorMessage) {
        logger.error(errorMessage);
        BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.BAD_REQUEST, errorMessage);
    }

    public String getMeasurementsQuery(Message<JsonObject> message) {
        String installationId = message.body().getString(CassandraProperties.INSTALLATION_ID);
        String startDate = getValidatedDate(message, "startDate");
        String endDate = getValidatedDate(message, "endDate");
        String unit =  message.body().getString(CassandraProperties.UNIT);

        // Todo maybe query builder
        return "SELECT * FROM " + table + " " + "WHERE " + CassandraProperties.INSTALLATION_ID.toLowerCase() + " = '" + installationId + "'" +
                (startDate != null ? " AND timestamp >= '" + startDate + "'" : "") +
                (endDate != null ? " AND timestamp <= '" + endDate + "'" : "") +
                (unit != null ? " AND unit = '" + unit + "'" : "")+
                " ALLOW FILTERING";
    }

    public T parseToModel(JsonObject jsonObject) {
        return jsonObject.mapTo(this.modelClass);
    }

    private String getValidatedDate(Message<JsonObject> message, String dateProperty) {
        String date = message.body().getString(dateProperty);
        if (date == null) return null;

        String dateFormat = "[" +  CassandraProperties.DATETIME_FORMAT + "]";
        String dateFormatWithoutSeconds = "[" +  CassandraProperties.DATE_FORMAT_MINUTES + "]";
        String dateFormatWithoutMinutes = "[" +  CassandraProperties.DATE_FORMAT_HOURS + "]";
        String dateFormatWithoutHour = "[" +  CassandraProperties.DATE_FORMAT + "]";

        try {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern(dateFormat)
                    .appendPattern(dateFormatWithoutSeconds)
                    .appendPattern(dateFormatWithoutMinutes)
                    .appendPattern(dateFormatWithoutHour)
                    .toFormatter();
            formatter.parse(date);
        } catch (Exception e) {
            BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.BAD_REQUEST, CassandraProperties.WRONG_DATE);
        }
        return date;
    }
}
