package parser;

import io.vertx.core.json.JsonObject;
import model.EnergySourceCalculator;
import model.Installation;

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
