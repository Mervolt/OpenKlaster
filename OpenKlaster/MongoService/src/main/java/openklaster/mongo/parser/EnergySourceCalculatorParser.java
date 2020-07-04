package openklaster.mongo.parser;

import io.vertx.core.json.JsonObject;
import openklaster.mongo.model.EnergySourceCalculator;

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
