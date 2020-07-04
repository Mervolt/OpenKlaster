package parser;

import io.vertx.core.json.JsonObject;
import model.Load;

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
