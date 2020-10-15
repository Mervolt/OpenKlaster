package com.openklaster.api.app;


import com.openklaster.api.VerticleConfig;
import com.openklaster.api.handler.*;
import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.handler.summary.SummaryCreator;
import com.openklaster.api.model.*;
import com.openklaster.api.model.summary.EnvironmentalConfig;
import com.openklaster.api.model.summary.SummaryRequest;
import com.openklaster.api.parser.DefaultParseStrategy;
import com.openklaster.api.properties.EndpointRouteProperties;
import com.openklaster.api.properties.EventBusAddressProperties;
import com.openklaster.api.properties.EventbusMethods;
import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.verticle.OpenklasterVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApiVerticle extends OpenklasterVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ApiVerticle.class);
    private static final int VERSION1 = 1;
    private NestedConfigAccessor configAccessor;
    private Vertx vertx;
    private EventBus eventBus;
    private List<Handler> handlers;
    ApplicationContext ctx;

    public ApiVerticle(boolean isDevModeOn) {
        super(isDevModeOn);
    }

    public ApiVerticle() {
        super();
    }

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        ctx = new AnnotationConfigApplicationContext(VerticleConfig.class);
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        ConfigFilesManager configFilesManager = new ConfigFilesManager(this.configFilenamePrefix);
        configFilesManager.getConfig(vertx).getConfig(result -> {
            if (result.succeeded()) {
                JsonObject jsonObject = result.result();
                this.configAccessor = new NestedConfigAccessor(jsonObject);
                startVerticle();
            } else {
                logger.error("Failed to load config");
            }
        });
    }

    private void startVerticle() {
        Router router = Router.router(vertx);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(configAccessor.getInteger(EndpointRouteProperties.listeningPortKey));

        handlers = Arrays.asList(
                ctx.getBean(PostHandler.class),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.REGISTER, new DefaultParseStrategy<Register>(Register.class)),

                new PutHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.UPDATE_USER, new DefaultParseStrategy<UpdateUser>(UpdateUser.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.INFO, new DefaultParseStrategy<Username>(Username.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.GENERATE_TOKEN, new DefaultParseStrategy<Username>(Username.class)),

                new DeleteHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.DELETE_TOKEN, new DefaultParseStrategy<Username>(Username.class)),

                new DeleteHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.allTokensEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.DELETE_ALL_TOKENS, new DefaultParseStrategy<Username>(Username.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey), new DefaultParseStrategy<InstallationRequest>(InstallationRequest.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.allinstallationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey), EventbusMethods.GET_ALL, new DefaultParseStrategy<Username>(Username.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey), new DefaultParseStrategy<PostInstallation>(PostInstallation.class)),

                new PutHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey), new DefaultParseStrategy<Installation>(Installation.class)),

                new DeleteHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey), new DefaultParseStrategy<InstallationRequest>(InstallationRequest.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey), new DefaultParseStrategy<MeasurementPowerRequest>(MeasurementPowerRequest.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey), new DefaultParseStrategy<MeasurementPower>(MeasurementPower.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerproductionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey), new DefaultParseStrategy<MeasurementPowerRequest>(MeasurementPowerRequest.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerproductionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey), new DefaultParseStrategy<MeasurementPower>(MeasurementPower.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyconsumedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey), new DefaultParseStrategy<MeasurementEnergyRequest>(MeasurementEnergyRequest.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyconsumedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey), new DefaultParseStrategy<MeasurementEnergy>(MeasurementEnergy.class)),

                new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyproducedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey), new DefaultParseStrategy<MeasurementEnergyRequest>(MeasurementEnergyRequest.class)),

                new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyproducedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey), new DefaultParseStrategy<MeasurementEnergy>(MeasurementEnergy.class)),

                new SummaryHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.summaryEndpoint), configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey),
                        new DefaultParseStrategy<SummaryRequest>(SummaryRequest.class), new SummaryCreator(), new EnvironmentalConfig(2, 2))
        );
        routerConfig(router);
    }

    private void routerConfig(Router router) {
        handlers.forEach(handler -> {
            configureRouteHandler(router);
            switch (handler.getMethod()) {
                case HandlerProperties.getMethodHeader:
                    router.get(handler.getRoute()).handler(context -> handler.handle(context, eventBus));
                    break;
                case HandlerProperties.postMethodHeader:
                    router.post(handler.getRoute()).consumes("application/json").handler(context -> handler.handle(context, eventBus));
                    break;
                case HandlerProperties.putMethodHeader:
                    router.put(handler.getRoute()).consumes("application/json").handler(context -> handler.handle(context, eventBus));
                    break;
                case HandlerProperties.deleteMethodHeader:
                    router.delete(handler.getRoute()).handler(context -> handler.handle(context, eventBus));
                    break;
            }
        });
        router.route("/*").handler(StaticHandler.create("static"));
    }

    private void configureRouteHandler(Router router) {
        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.PUT);
        allowedMethods.add(HttpMethod.DELETE);

        router.route().handler(BodyHandler.create())
                .handler(CorsHandler.create("*")
                        .allowedHeader("Content-Type")
                        .allowedHeader("responseType")
                        .allowedMethods(allowedMethods));
    }

    public static String buildEndpoint(NestedConfigAccessor configAccessor, int version, String route) {
        return configAccessor.getString(EndpointRouteProperties.prefix) +
                "/" + version + configAccessor.getString(route);
    }
}
