package com.openklaster.mongo.service;

import io.vertx.core.json.JsonObject;

import static com.openklaster.mongo.service.EntityHandler.ID_KEY;

public class MongoQuery {

    public static JsonObject updateQuery(JsonObject fields) {
        return new JsonObject()
                .put("$set", fields);
    }

    public static JsonObject getByIdQuery(String id) {
        return new JsonObject().put(ID_KEY, id);
    }
}
