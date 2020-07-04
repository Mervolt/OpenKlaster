package openklaster.common.config;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public abstract class EndpointConfig {

    protected final String MAIN_ROUTE;
    protected final String ROUTE_CONSUMES;

    protected final NestedConfigAccessor config;

    protected EndpointConfig(JsonObject configObject) {
        this.config = new NestedConfigAccessor(configObject);
        this.MAIN_ROUTE = config.getString("route.main");
        this.ROUTE_CONSUMES = config.getString("consumes");
    }

    public abstract void configureRouter(Router router);
}
