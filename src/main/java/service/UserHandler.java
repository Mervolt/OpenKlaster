package service;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import model.User;
import parser.UserParser;

public class UserHandler extends EntityHandler<User> {

    public UserHandler(UserParser parser, MongoPersistenceService service){
        super(parser,service);
        collectionName = "users";
        logger = LoggerFactory.getLogger(UserHandler.class);
    }

    @Override
    public void add(RoutingContext context) {
        JsonObject jsonObject = context.getBodyAsJson();
        String id = jsonObject.getString("username");
        jsonObject.remove("username");
        jsonObject.put("_id",id);
        persistenceService.upsertByQuery(jsonObject, this.collectionName,
                handler -> {
                    if (handler.succeeded()) {
                        logger.info(String.format("Entity %s added to %s ", jsonObject.getString("_id"), this.collectionName));
                        context.response()
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .end(Json.encodePrettily(parser.toEntity(jsonObject)));
                    } else {
                        logger.info(String.format("Problem with adding entity %s to %s", jsonObject.getString("_id"), this.collectionName));
                        context.response()
                                .setStatusCode(400)
                                .end();
                    }
                });
    }
}
