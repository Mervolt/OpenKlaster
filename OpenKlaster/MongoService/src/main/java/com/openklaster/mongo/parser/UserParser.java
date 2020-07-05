package com.openklaster.mongo.parser;

import com.openklaster.mongo.model.User;
import io.vertx.core.json.JsonObject;

public class UserParser implements EntityParser<User> {

    @Override
    public JsonObject toJsonObject(User entity) {
        return JsonObject.mapFrom(entity);
    }

    @Override
    public User toEntity(JsonObject jsonObject) {
        return jsonObject.mapTo(User.class);
    }

}
