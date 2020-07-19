package com.openklaster.mongo.parser;

import com.openklaster.common.model.Load;
import io.vertx.core.json.JsonObject;

public class LoadParser implements EntityParser<Load> {
    @Override
    public JsonObject toJsonObject(Load entity) {
        return JsonObject.mapFrom(entity);
    }

    @Override
    public Load toEntity(JsonObject jsonObject) {
        return jsonObject.mapTo(Load.class);
    }
}
