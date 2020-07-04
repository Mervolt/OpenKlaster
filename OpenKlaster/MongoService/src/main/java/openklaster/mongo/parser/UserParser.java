package openklaster.mongo.parser;

import io.vertx.core.json.JsonObject;
import openklaster.mongo.model.User;

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
