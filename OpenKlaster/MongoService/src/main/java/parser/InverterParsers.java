package parser;

import io.vertx.core.json.JsonObject;
import model.Inverter;

public class InverterParsers implements EntityParser<Inverter> {
    @Override
    public JsonObject toJsonObject(Inverter entity) {
        return new JsonObject()
                .put("_id", entity.get_id())
                .put("credentials", entity.getCredentials())
                .put("manufacturer", entity.getManufacturer())
                .put("installationId", entity.getInstallationId())
                .put("modelType", entity.getModelType())
                .put("description", entity.getDescription());
    }

    @Override
    public Inverter toEntity(JsonObject jsonObject) {
        Inverter outp = new Inverter();
        outp.set_id(jsonObject.getString("_id"));
        outp.setCredentials(jsonObject.getString("credentials"));
        outp.setManufacturer(jsonObject.getString("manufacturer"));
        outp.setInstallationId(jsonObject.getString("installationId"));
        outp.setModelType(jsonObject.getString("modelType"));
        outp.setDescription(jsonObject.getString("description"));
        return outp;
    }
}
