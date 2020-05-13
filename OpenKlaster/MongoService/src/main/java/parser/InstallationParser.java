package parser;

import io.vertx.core.json.JsonObject;
import model.Installation;
import model.InstallationType;

public class InstallationParser implements EntityParser<Installation> {
    @Override
    public JsonObject toJsonObject(Installation entity) {
        return new JsonObject()
                .put("_id", entity.get_id())
                .put("username", entity.getUsername())
                .put("installationType",entity.getInstallationType().toString())
                .put("longtitude",entity.getLongtitude())
                .put("latitude", entity.getLatitude())
                .put("description", entity.getDescription());
    }

    @Override
    public Installation toEntity(JsonObject jsonObject) {
        Installation outp = new Installation();
        outp.set_id(jsonObject.getString("_id"));
        outp.setDescription(jsonObject.getString("description"));
        outp.setInstallationType(getInstallationType(jsonObject.getString("installationType")));
        outp.setLongtitude(jsonObject.getDouble("longtitude"));
        outp.setLatitude(jsonObject.getDouble("latitude"));
        outp.setDescription(jsonObject.getString("description"));

        return outp;
    }

    private InstallationType getInstallationType(String type){
        if(type.equals("wind"))
            return InstallationType.Wind;
        else if(type.equals("solar"))
            return InstallationType.Solar;
        else throw new IllegalArgumentException("Could not recognize installation type");
    }
}
