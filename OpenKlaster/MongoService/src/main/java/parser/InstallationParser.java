package parser;

import io.vertx.core.json.JsonObject;
import model.Installation;

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
