package parser;

import io.vertx.core.json.JsonObject;
import model.Source;

public class SourceParsers implements EntityParser<Source> {
    @Override
    public JsonObject toJsonObject(Source entity) {
        return new JsonObject()
                .put("_id", entity.get_id())
                .put("azimuth", entity.getAzimuth())
                .put("inverterId", entity.getInverterId())
                .put("tilt", entity.getTilt())
                .put("capacity", entity.getCapacity())
                .put("description", entity.getDescription());
    }

    @Override
    public Source toEntity(JsonObject jsonObject) {
        Source outp = new Source();
        outp.set_id(jsonObject.getString("_id"));
        outp.setAzimuth(jsonObject.getInteger("azimuth"));
        outp.setInverterId(jsonObject.getString("inverterId"));
        outp.setTilt(jsonObject.getInteger("tilt"));
        outp.setDescription(jsonObject.getString("description"));
        outp.setCapacity(jsonObject.getInteger("capacity"));
        return outp;
    }
}
