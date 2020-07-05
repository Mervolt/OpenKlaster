package openklaster.mongo.parser;

import io.vertx.core.json.JsonObject;
import openklaster.mongo.model.Source;

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
