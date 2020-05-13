package config;

import io.vertx.core.json.JsonObject;

public class DbConfig {

    public JsonObject getMongoConfig(){
        return new JsonObject()
                .put("host","localhost")
                .put("port",27017);
    }
}
