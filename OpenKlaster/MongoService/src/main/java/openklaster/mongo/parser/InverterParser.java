package openklaster.mongo.parser;

import io.vertx.core.json.JsonObject;
import openklaster.mongo.model.Inverter;

public class InverterParser implements EntityParser<Inverter> {
    @Override
    public JsonObject toJsonObject(Inverter entity) {
        return JsonObject.mapFrom(entity);
    }

    @Override
    public Inverter toEntity(JsonObject jsonObject) {
        return jsonObject.mapTo(Inverter.class);
    }
}
