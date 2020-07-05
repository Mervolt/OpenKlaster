package com.openklaster.mongo.parser;

import com.openklaster.mongo.model.Installation;
import io.vertx.core.json.JsonObject;

public class InstallationParser implements EntityParser<Installation> {
    @Override
    public JsonObject toJsonObject(Installation entity) {
        return JsonObject.mapFrom(entity);
    }

    @Override
    public Installation toEntity(JsonObject jsonObject) {
       return jsonObject.mapTo(Installation.class);
    }

}
