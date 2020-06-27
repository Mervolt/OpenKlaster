package service;

import config.NestedConfigAccessor;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.ext.web.RoutingContext;
import parser.EntityParser;

public abstract class EntityHandler {

    protected static Logger logger;
    protected EntityParser parser;
    protected static String ID_FIELD_KEY = "_id";
    protected NestedConfigAccessor config;
    protected MongoPersistenceService persistenceService;

    public EntityHandler(EntityParser parser, MongoPersistenceService service, NestedConfigAccessor config) {
        this.parser = parser;
        this.persistenceService = service;
        this.config = config;
    }

    protected String getCollectionName(){
        return config.getString("mongo.collectionName");
    }

    public void add(Message<JsonObject> busMessage) {
        JsonObject jsonObject = busMessage.body();
        persistenceService.upsertByQuery(jsonObject, this.getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        logger.debug(String.format("Entity added - %s", jsonObject.getString("_id")));
                        busMessage.reply(Json.encodePrettily(jsonObject));
                    } else {
                        logger.warn(String.format("Problem with adding entity - %s", jsonObject.getString("_id")));
                        busMessage.reply(HttpResponseStatus.BAD_REQUEST);
                    }
                });
    }

    public void findById(Message<JsonObject> busMessage ) {
        String id = busMessage.body().getString("id");
        if (id == null) {
            busMessage.reply(HttpResponseStatus.BAD_REQUEST);
            return;
        }
        JsonObject jsonObject = new JsonObject()
                .put(ID_FIELD_KEY, id);
        persistenceService.findOneByQuery(jsonObject, getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        logger.debug(String.format("Entity Found - %s", jsonObject.getString("_id")));
                        busMessage.reply(Json.encodePrettily(parser.toEntity(handler.result())));
                    } else {
                        logger.warn(String.format("Problem Finding entity - %s", jsonObject.getString("_id")));
                        busMessage.reply(HttpResponseStatus.NOT_FOUND);
                    }
                });
    }

    public void delete(Message<JsonObject> busMessage) {
        String id = busMessage.body().getString("id");
        if (id == null) {
            busMessage.reply(HttpResponseStatus.BAD_REQUEST);
            return;
        }
        JsonObject jsonObject = new JsonObject()
                .put(ID_FIELD_KEY, id);
        persistenceService.removeByQuery(jsonObject, getCollectionName(),
                handler -> {
                    if (handler.succeeded()) {
                        logger.debug(String.format("Entity deleted - %s", jsonObject.getString("_id")));
                        busMessage.reply(HttpResponseStatus.NO_CONTENT);
                    } else {
                        logger.warn(String.format("Problem with removing entity - %s", jsonObject.getString("_id")));
                        busMessage.reply(HttpResponseStatus.NOT_FOUND);
                    }
                });
    }

}
