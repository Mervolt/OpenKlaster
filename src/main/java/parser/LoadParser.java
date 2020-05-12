package parser;

import io.vertx.core.json.JsonObject;
import model.Load;

public class LoadParser implements EntityParser<Load> {
    @Override
    public JsonObject toJsonObject(Load entity) {
        return new JsonObject()
                .put("_id", entity.get_id())
                .put("installationId", entity.getInstallationId())
                .put("name", entity.getName())
                .put("description", entity.getDescription());
    }

    @Override
    public Load toEntity(JsonObject jsonObject) {
        Load outp = new Load();
        outp.set_id(jsonObject.getString("_id"));
        outp.setInstallationId(jsonObject.getString("installationId"));
        outp.setName(jsonObject.getString("name"));
        outp.setDescription(jsonObject.getString("description"));
        return outp;
    }
}
