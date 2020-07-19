package com.openklaster.mongo.service;

import io.vertx.core.json.JsonObject;

public class MongoQuery {

    public static JsonObject updateQuery(JsonObject fields){
        return new JsonObject()
                .put("$set", fields);
    }
}
