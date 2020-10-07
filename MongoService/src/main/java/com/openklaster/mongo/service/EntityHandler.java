package com.openklaster.mongo.service;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.mongo.exceptions.MongoExceptionHandler;
import com.openklaster.mongo.parser.EntityParser;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
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
    protected static String NAME_KEY = "username";

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
                                    String.format("Not found %s", id));
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

    public void findAllByQuery(Message<JsonObject> busMessage) {
        JsonObject jsonObject = busMessage.body();
        String name = jsonObject.getString(NAME_KEY);
        if (name == null) {
            BusMessageReplyUtils.replyWithError(busMessage, HttpResponseStatus.BAD_REQUEST, "No username provided");
            return;
        }
        persistenceService.findAllByQuery(jsonObject, getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        if (handler.result() == null) {
                            logger.debug(String.format("Entities not found - %s", name));
                            BusMessageReplyUtils.replyWithError(busMessage, HttpResponseStatus.NOT_FOUND,
                                    String.format("Entities Not found %s _id", name));
                        } else {
                            JsonArray jsonArray = handler.result().stream().collect(JsonArray::new, JsonArray::add, JsonArray::add);
                            logger.debug(String.format("Entities Found - %s", name));
                            BusMessageReplyUtils.replyWithBodyAndStatus(busMessage, jsonArray, HttpResponseStatus.OK);
                        }
                    } else {
                        handleFailedQuery(name,busMessage,handler.cause(),"Problem with finding entities.");
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

    public void update(Message<JsonObject> busMessage) {
        JsonObject updateBody = busMessage.body();
        String id = updateBody.getString(ID_KEY);
        if (id == null) {
            BusMessageReplyUtils.replyWithError(busMessage, HttpResponseStatus.BAD_REQUEST, "No _id provided");
            return;
        }
        JsonObject findQuery = new JsonObject().put(ID_KEY,id);

        persistenceService.replaceByQuery(findQuery, updateBody, this.getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        logger.debug(String.format("Entity updated - %s", updateBody));
                        BusMessageReplyUtils.replyWithBodyAndStatus(busMessage, updateBody, HttpResponseStatus.OK);
                    } else {
                        handleFailedQuery(id,busMessage,handler.cause(),
                                "Problem with adding entity.");
                    }
                });
    }
}
