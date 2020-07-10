package com.openklaster.mongo.service;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.mongo.exceptions.MongoExceptionHandler;
import com.openklaster.mongo.parser.EntityParser;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import org.apache.commons.lang3.tuple.Pair;

public abstract class EntityHandler {

    protected static Logger logger;
    protected EntityParser parser;
    protected NestedConfigAccessor config;
    protected MongoPersistenceService persistenceService;
    protected MongoExceptionHandler exceptionHandler;
    private final String mongoCollectionNameKey = "mongo.collectionName";
    protected static String ID_KEY = "_id";

    public EntityHandler(EntityParser parser, MongoPersistenceService service, NestedConfigAccessor config) {
        this.parser = parser;
        this.persistenceService = service;
        this.config = config;
        this.exceptionHandler=new MongoExceptionHandler();
    }

    protected String getCollectionName() {
        return config.getString(mongoCollectionNameKey);
    }

    public void add(Message<JsonObject> busMessage) {
        JsonObject jsonObject = busMessage.body();
        persistenceService.insertByQuery(jsonObject, this.getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        logger.debug(String.format("Entity added - %s", jsonObject.getString("_id")));
                        BusMessageReplyUtils.replyWithBodyAndStatus(busMessage, jsonObject, HttpResponseStatus.OK);
                    } else {
                        handleFailedQuery(jsonObject.getString("_id"),busMessage,handler.cause(),
                                "Problem with adding entity.");
                    }
                });
    }

    public void findById(Message<JsonObject> busMessage) {
        JsonObject jsonObject = busMessage.body();
        String id = jsonObject.getString(ID_KEY);
        if (id == null) {
            BusMessageReplyUtils.replyWithError(busMessage, HttpResponseStatus.BAD_REQUEST, "No _id provided");
            return;
        }
        persistenceService.findOneByQuery(jsonObject, getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        if (handler.result() == null) {
                            logger.debug(String.format("Entity not found - %s", id));
                            BusMessageReplyUtils.replyWithError(busMessage, HttpResponseStatus.NOT_FOUND,
                                    String.format("Entity Not found %s _id", id));
                        } else {
                            logger.debug(String.format("Entity Found - %s", id));
                            BusMessageReplyUtils.replyWithBodyAndStatus(busMessage, handler.result(),
                                    HttpResponseStatus.OK);
                        }
                    } else {
                        handleFailedQuery(id,busMessage,handler.cause(),"Problem with finding entity.");
                    }
                });
    }

    public void remove(Message<JsonObject> busMessage) {
        JsonObject jsonObject = busMessage.body();
        String id = jsonObject.getString(ID_KEY);
        if (id == null) {
            BusMessageReplyUtils.replyWithError(busMessage, HttpResponseStatus.BAD_REQUEST, "No _id provided");
            return;
        }
        persistenceService.removeByQuery(jsonObject, getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        long removedEntities = handler.result().getRemovedCount();
                        logger.debug(String.format("Entity to delete - %s. Removed %d", id, removedEntities));
                        BusMessageReplyUtils.replyWithBodyAndStatus(busMessage,handler.result().toJson(),HttpResponseStatus.OK);
                    } else {
                        handleFailedQuery(id,busMessage,handler.cause(),"Problem with removing entity.");
                    }
                });
    }
    protected void handleFailedQuery(Object id, Message<JsonObject> busMessage, Throwable cause, String logMessage){
        Pair<HttpResponseStatus,String> handledErrorResult =
                exceptionHandler.getStatusAndMessageForException(cause);

        logger.warn(exceptionHandler.getWarnLogMessage(id,getCollectionName(),
                logMessage + " " +handledErrorResult.getRight()));

        BusMessageReplyUtils.replyWithError(busMessage,handledErrorResult.getLeft(),
                exceptionHandler.getReplyFailureMessage(id,logMessage + " " +handledErrorResult.getRight()));
    }
}
