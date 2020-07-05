package com.openklaster.mongo.parser;

import com.openklaster.mongo.model.EnergySourceCalculator;
import io.vertx.core.json.JsonObject;

public class EnergySourceCalculatorParser implements EntityParser<EnergySourceCalculator> {
    @Override
    public JsonObject toJsonObject(EnergySourceCalculator entity) {
        return JsonObject.mapFrom(entity);
    }

    @Override
    public EnergySourceCalculator toEntity(JsonObject jsonObject) {
        return jsonObject.mapTo(EnergySourceCalculator.class);
    }
}
