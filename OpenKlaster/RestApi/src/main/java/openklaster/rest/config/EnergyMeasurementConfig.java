package openklaster.rest.config;

import openklaster.rest.handler.EnergyMeasurementHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class EnergyMeasurementConfig extends EndpointConfig {

    private final EnergyMeasurementHandler handler;
    private final String PRODUCTION_ROUTE;
    private final String CONSUMPTION_ROUTE;

    public EnergyMeasurementConfig(JsonObject configObject, EnergyMeasurementHandler handler) {
        super(configObject);
        this.handler = handler;
        PRODUCTION_ROUTE = config.getString("route.production");
        CONSUMPTION_ROUTE = config.getString("route.consumption");
    }

    @Override
    public void configureRouter(Router router) {
        router.route(MAIN_ROUTE).handler(BodyHandler.create());
        router.get(MAIN_ROUTE + CONSUMPTION_ROUTE).consumes(ROUTE_CONSUMES)
                .handler(handler::getEnergyConsumed);
        router.get(MAIN_ROUTE + PRODUCTION_ROUTE).consumes(ROUTE_CONSUMES)
                .handler(handler::getEnergyProduced);
    }
}
