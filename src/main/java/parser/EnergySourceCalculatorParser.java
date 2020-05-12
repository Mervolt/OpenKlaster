package parser;

import io.vertx.core.json.JsonObject;
import model.EnergySourceCalculator;

public class EnergySourceCalculatorParser implements  EntityParser<EnergySourceCalculator> {
    @Override
    public JsonObject toJsonObject(EnergySourceCalculator entity) {
        return new JsonObject()
                .put("sourceName", entity.getSourceName())
                .put("energyValue", entity.getEnergyValue());
    }

    @Override
    public EnergySourceCalculator toEntity(JsonObject jsonObject) {
        EnergySourceCalculator outp = new EnergySourceCalculator();
        outp.setEnergyValue(jsonObject.getDouble("energyValue"));
        outp.setSourceName(jsonObject.getString("sourceName"));
        return outp;
    }
}
