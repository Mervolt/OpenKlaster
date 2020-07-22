package com.openklaster.cassandra.service;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.Row;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.CassandraGetRequest;
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
import java.util.Collections;
import java.util.Date;
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
        this.address = config.getString("address");
        this.table = config.getString("table");
    }

    public String getAddress() {
        return this.address;
    }

    public abstract void createPostHandler(Message<JsonObject> message);

    public void createGetHandler(Message<JsonObject> message) {
        try {
            CassandraGetRequest cassandraGetRequest = message.body().mapTo(CassandraGetRequest.class);
            String query = buildQuery(cassandraGetRequest);
            cassandraClient.executeWithFullFetch(query, listAsyncResult -> {
                    logger.debug("GET request executed successfully");
                    BusMessageReplyUtils.replyWithBodyAndStatus(message, response, HttpResponseStatus.OK);
                } else {
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
                .map(row -> new JsonObject(row.getString(0)))
                .collect(JsonArray::new, JsonArray::add, JsonArray::add);
    }

    public void handleFailure(Message<JsonObject> message, String errorMessage) {
        logger.error(errorMessage);
        BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.BAD_REQUEST, errorMessage);
    }

    public String buildQuery(CassandraGetRequest cassandraGetRequest) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        return "SELECT JSON * FROM " + table + " " + "WHERE installationid = '" + cassandraGetRequest.getInstallationId() + "'" +
                (cassandraGetRequest.getStartDate() != null ? " AND timestamp >= '" + dateFormat.format(cassandraGetRequest.getStartDate()) + "'" : "") +
                (cassandraGetRequest.getEndDate() != null ? " AND timestamp <= '" + dateFormat.format(cassandraGetRequest.getEndDate()) + "'" : "") +
                " ALLOW FILTERING";
    }

    public T parseToModel(JsonObject jsonObject) {
        return jsonObject.mapTo(this.modelClass);
    }
}
