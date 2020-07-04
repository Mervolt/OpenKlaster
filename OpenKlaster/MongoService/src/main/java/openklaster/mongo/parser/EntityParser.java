package openklaster.mongo.parser;

import io.vertx.core.json.JsonObject;

public interface EntityParser<T> {

    JsonObject toJsonObject(T entity);
    T toEntity(JsonObject jsonObject);
}
