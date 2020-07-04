package openklaster.rest.config;

import openklaster.rest.handler.PowerMeasurementHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class PowerMeasurementConfig extends EndpointConfig {

    private final String PRODUCTION_ROUTE ;
    private final String CONSUMPTION_ROUTE;
    private final PowerMeasurementHandler handler;

    public PowerMeasurementConfig(JsonObject configObject, PowerMeasurementHandler handler) {
        super(configObject);
        this.handler = handler;
        PRODUCTION_ROUTE = config.getString("route.production");
        CONSUMPTION_ROUTE = config.getString("route.consumption");
    }

    @Override
    public void configureRouter(Router router) {
        router.route(MAIN_ROUTE + CONSUMPTION_ROUTE).handler(BodyHandler.create());
        router.get(MAIN_ROUTE + CONSUMPTION_ROUTE).consumes(ROUTE_CONSUMES)
                .handler(handler::getPowerConsumption);
        router.post(MAIN_ROUTE + CONSUMPTION_ROUTE).consumes(ROUTE_CONSUMES)
                .handler(handler::addPowerConsumption);

        router.route(MAIN_ROUTE + PRODUCTION_ROUTE).handler(BodyHandler.create());
        router.get(MAIN_ROUTE + PRODUCTION_ROUTE).consumes(ROUTE_CONSUMES)
                .handler(handler::getPowerProduction);
        router.post(MAIN_ROUTE + PRODUCTION_ROUTE).consumes(ROUTE_CONSUMES)
                .handler(handler::addPowerProduction);
    }
}
