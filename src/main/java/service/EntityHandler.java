package service;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.ext.web.RoutingContext;
import parser.EntityParser;

public abstract class EntityHandler<T> {

    protected static Logger logger;
    protected EntityParser<T> parser;
    protected String collectionName;
    protected static String ID_FIELD_KEY = "_id";

    protected MongoPersistenceService persistenceService;

    public EntityHandler(EntityParser<T> parser, MongoPersistenceService service) {
        this.parser = parser;
        this.persistenceService = service;
    }

    public void add(RoutingContext context) {
        JsonObject jsonObject = context.getBodyAsJson();
        persistenceService.upsertByQuery(jsonObject, this.collectionName,
                handler -> {
                    if (handler.succeeded()) {
                        logger.info(String.format("Entity added - %s", jsonObject.getString("_id")));
                        context.response()
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .end(Json.encodePrettily(parser.toEntity(jsonObject)));
                    } else {
                        logger.info(String.format("Problem with adding entity - %s", jsonObject.getString("_id")));
                        context.response()
                                .setStatusCode(400)
                                .end();
                    }
                });
    }

    public void findById(RoutingContext context) {
        String id = context.request().getParam("id");
        if(id==null){
            context.response().setStatusCode(400)
                    .end();
            return;
        }
        JsonObject jsonObject = new JsonObject()
                .put(ID_FIELD_KEY, id);
        persistenceService.findOneByQuery(jsonObject, collectionName,
                handler -> {
                    if (handler.succeeded()) {
                        logger.info(String.format("Entity Found - %s", jsonObject.getString("_id")));
                        context.response()
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .end(Json.encodePrettily(parser.toEntity(handler.result())));
                    } else {
                        logger.info(String.format("Problem Finding entity - %s", jsonObject.getString("_id")));
                        context.response()
                                .setStatusCode(404)
                                .end();
                    }
                });

    }

    public void delete(RoutingContext context) {
        String id = context.request().getParam("id");
        if(id==null){
            context.response().setStatusCode(400)
                    .end();
            return;
        }
        JsonObject jsonObject = new JsonObject()
                .put(ID_FIELD_KEY, id);
        persistenceService.removeByQuery(jsonObject, collectionName,
                handler -> {
                    if (handler.succeeded()) {
                        logger.info(String.format("Entity deleted - %s", jsonObject.getString("_id")));
                        context.response()
                                .setStatusCode(204)
                                .end();
                    } else {
                        logger.info(String.format("Problem with removing entity - %s", jsonObject.getString("_id")));
                        context.response()
                                .setStatusCode(404)
                                .end();
                    }
                });
    }

}
