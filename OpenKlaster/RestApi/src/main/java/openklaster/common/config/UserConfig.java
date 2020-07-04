package openklaster.common.config;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class UserConfig extends  EndpointConfig{
    protected UserConfig(JsonObject configObject) {
        super(configObject);
    }

    @Override
    public void configureRouter(Router router) {

    }
}
