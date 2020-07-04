package service;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import openklaster.common.config.NestedConfigAccessor;
import openklaster.common.messages.BusMessageReplyClient;
import parser.EntityParser;

public abstract class EntityHandler {

    protected static Logger logger;
    protected EntityParser parser;
    protected NestedConfigAccessor config;
    protected MongoPersistenceService persistenceService;
    private final String mongoCollectionNameKey = "mongo.collectionName";

    public EntityHandler(EntityParser parser, MongoPersistenceService service, NestedConfigAccessor config) {
        this.parser = parser;
        this.persistenceService = service;
        this.config = config;
    }

    protected String getCollectionName() {
        return config.getString(mongoCollectionNameKey);
    }

    public void add(Message<JsonObject> busMessage) {
        JsonObject jsonObject = busMessage.body();
        persistenceService.upsertByQuery(jsonObject, this.getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        logger.debug(String.format("Entity added - %s", jsonObject.getString("_id")));
                        BusMessageReplyClient.replyWithBodyAndStatus(busMessage, jsonObject, HttpResponseStatus.OK);
                    } else {
                        logger.warn(String.format("Problem with adding entity - %s", jsonObject.getString("_id")));
                        BusMessageReplyClient.replyWithStatus(busMessage, HttpResponseStatus.BAD_REQUEST);
                    }
                });
    }

    public void findById(Message<JsonObject> busMessage) {
        JsonObject jsonObject = busMessage.body();
        if (jsonObject.getString("_id") == null) {
            BusMessageReplyClient.replyWithStatus(busMessage, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        persistenceService.findOneByQuery(jsonObject, getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        logger.debug(String.format("Entity Found - %s", jsonObject.getString("_id")));
                        BusMessageReplyClient.replyWithBodyAndStatus(busMessage, handler.result(),
                                HttpResponseStatus.OK);
                    } else {
                        logger.warn(String.format("Problem Finding entity - %s", jsonObject.getString("_id")));
                        BusMessageReplyClient.replyWithStatus(busMessage, HttpResponseStatus.BAD_REQUEST);
                    }
                });
    }

    public void delete(Message<JsonObject> busMessage) {
        JsonObject jsonObject = busMessage.body();
        if (jsonObject.getString("_id") == null) {
            BusMessageReplyClient.replyWithStatus(busMessage, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        persistenceService.removeByQuery(jsonObject, getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        logger.debug(String.format("Entity deleted - %s", jsonObject.getString("_id")));
                        BusMessageReplyClient.replyWithStatus(busMessage, HttpResponseStatus.NO_CONTENT);
                    } else {
                        logger.warn(String.format("Problem with removing entity - %s", jsonObject.getString("_id")));
                        BusMessageReplyClient.replyWithStatus(busMessage, HttpResponseStatus.NOT_FOUND);
                    }
                });
    }

}
