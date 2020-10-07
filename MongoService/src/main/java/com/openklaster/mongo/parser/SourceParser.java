package com.openklaster.mongo.parser;

import com.openklaster.common.model.Source;
import io.vertx.core.json.JsonObject;

public class SourceParser implements EntityParser<Source> {
    @Override
    public JsonObject toJsonObject(Source entity) {
        return JsonObject.mapFrom(entity);
    }

    @Override
    public Source toEntity(JsonObject jsonObject) {
        return jsonObject.mapTo(Source.class);
    }
}
