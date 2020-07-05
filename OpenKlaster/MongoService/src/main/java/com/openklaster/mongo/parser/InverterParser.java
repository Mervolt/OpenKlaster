package com.openklaster.mongo.parser;

import com.openklaster.mongo.model.Inverter;
import io.vertx.core.json.JsonObject;

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
