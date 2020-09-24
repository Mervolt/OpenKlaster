package com.openklaster.api.app;


import com.openklaster.api.handler.*;
import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.*;
import com.openklaster.api.parser.DefaultParseStrategy;
import com.openklaster.api.properties.EndpointRouteProperties;
import com.openklaster.api.properties.EventBusAddressProperties;
import com.openklaster.api.properties.EventbusMethods;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.Arrays;
import java.util.List;

public class OpenKlasterAPIVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(OpenKlasterAPIVerticle.class);
    private static final int VERSION1 = 1;
    private ConfigRetriever configRetriever;
    private NestedConfigAccessor configAccessor;
    private Vertx vertx;
    private EventBus eventBus;
    private List<Handler> handlers;


    public OpenKlasterAPIVerticle(Vertx vertx, ConfigRetriever configRetriever) {
        this.vertx = vertx;
        this.configRetriever = configRetriever;
        this.eventBus = vertx.eventBus();
    }

    @Override
    public void start(Promise<Void> promise) {
        configRetriever.getConfig(config -> {
            if (config.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(config.result());
                startVerticle(promise);
            } else {
                logger.error(config.cause());
                vertx.close();
            }
        });
    }

    private void startVerticle(Promise<Void> promise) {
        Router router = Router.router(vertx);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(configAccessor.getInteger(EndpointRouteProperties.listeningPortKey));
        handlers = Arrays.asList(
                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.loginEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.LOGIN,
                        eventBus, configAccessor, new DefaultParseStrategy<Login>(Login.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.REGISTER,
                        eventBus, configAccessor, new DefaultParseStrategy<Register>(Register.class)),

                new PutHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.UPDATE_USER,
                        eventBus, configAccessor, new DefaultParseStrategy<UpdateUser>(UpdateUser.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.INFO,
                        eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.GENERATE_TOKEN,
                        eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)),

                new DeleteHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.DELETE_TOKEN,
                        eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)),

                new DeleteHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.allTokensEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.DELETE_ALL_TOKENS,
                        eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<InstallationRequest>(InstallationRequest.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.allinstallationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey), EventbusMethods.GET_ALL,
                        eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<PostInstallation>(PostInstallation.class)),

                new PutHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Installation>(Installation.class)),

                new DeleteHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<InstallationRequest>(InstallationRequest.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementPowerRequest>(MeasurementPowerRequest.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementPower>(MeasurementPower.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerproductionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementPowerRequest>(MeasurementPowerRequest.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerproductionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementPower>(MeasurementPower.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyconsumedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementEnergyRequest>(MeasurementEnergyRequest.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyconsumedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementEnergy>(MeasurementEnergy.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyproducedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementEnergyRequest>(MeasurementEnergyRequest.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyproducedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementEnergy>(MeasurementEnergy.class)),

                new SummaryHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.summaryEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<SummaryRequest>(SummaryRequest.class))
        );


        routerConfig(router, promise);
    }

    private void routerConfig(Router router, Promise<Void> promise) {
        handlers.forEach(handler -> {
            configureRouteHandler(router);
            switch (handler.getMethod()) {
                case HandlerProperties.getMethodHeader:
                    router.get(handler.getRoute()).handler(handler::handle);
                    break;
                case HandlerProperties.postMethodHeader:
                    router.post(handler.getRoute()).consumes("application/json").handler(handler::handle);
                    break;
                case HandlerProperties.putMethodHeader:
                    router.put(handler.getRoute()).consumes("application/json").handler(handler::handle);
                    break;
                case HandlerProperties.deleteMethodHeader:
                    router.delete(handler.getRoute()).handler(handler::handle);
                    break;
            }
        });
        promise.complete();
    }

    private void configureRouteHandler(Router router) {
        router.route().handler(BodyHandler.create())
                .handler(CorsHandler.create("*")
                        .allowedHeader("Content-Type")
                        .allowedHeader("responseType"));
    }

    public static String buildEndpoint(NestedConfigAccessor configAccessor, int version, String route) {
        return configAccessor.getString(EndpointRouteProperties.prefix) +
                "/" + version + configAccessor.getString(route);
    }
}
