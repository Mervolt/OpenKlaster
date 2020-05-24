package handler;

import config.NestedConfigAccessor;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;

public class PowerMeasurementHandler extends EndpointHandler {

    private final WebClient webClient;
    private final NestedConfigAccessor config;
    private static final Logger logger = LoggerFactory.getLogger(PowerMeasurementHandler.class);

    public PowerMeasurementHandler(WebClient webClient, JsonObject configObject) {
        this.webClient = webClient;
        this.config = new NestedConfigAccessor(configObject);
    }

    public void addPowerConsumption(RoutingContext context) {
        addPowerMeasurement("cassandra.route.load","receiverId",context);
    }

    public void addPowerProduction(RoutingContext context) {
        addPowerMeasurement("cassandra.route.source","inverterId",context);
    }

    public void getPowerProduction(RoutingContext context) {
        getPowerMeasurement("cassandra.route.source","inverterId",context);
    }

    public void getPowerConsumption(RoutingContext context) {
        getPowerMeasurement("cassandra.route.load","receiverId",context);
    }

    private void addPowerMeasurement(String routeConfigPath, String deviceIdKey, RoutingContext context) {
        JsonObject jsonObject = context.getBodyAsJson();
        webClient.post(config.getInteger("cassandra.port"), config.getString("cassandra.host"),
                config.getString(routeConfigPath))
                .putHeader("Content-type", config.getString("cassandra.consumes"))
                .addQueryParam("value", jsonObject.getString("value"))
                .addQueryParam(deviceIdKey, jsonObject.getString(deviceIdKey))
                .send(resp -> {
                    if (resp.succeeded()) {
                        context.response().setStatusCode(HttpResponseStatus.OK.code())
                                .end();
                    } else {
                        logger.error(resp.cause());
                        context.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                .end();
                    }
                });
    }
    private void getPowerMeasurement(String routeConfigPath, String deviceIdKey, RoutingContext context){
        JsonObject jsonObject = context.getBodyAsJson();
        String startDate = jsonObject.getString("startDate") != null ? jsonObject.getString("startDate") : "" ;
        String endDate = jsonObject.getString("endDate")!= null ? jsonObject.getString("endDate") : "";
        webClient.get(config.getInteger("cassandra.port"), config.getString("cassandra.host"),
                config.getString(routeConfigPath))
                .putHeader("Content-type", config.getString("cassandra.consumes"))
                .addQueryParam(deviceIdKey, jsonObject.getString(deviceIdKey))
                .addQueryParam("endDate",endDate)
                .addQueryParam("startDate",startDate)
                .send(resp -> {
                    if(resp.succeeded()){
                        context.response().end(resp.result().bodyAsBuffer());
                    }else{
                        context.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code())
                        .end();
                    }
                });
    }
}
