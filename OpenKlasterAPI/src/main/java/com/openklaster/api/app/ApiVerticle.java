package com.openklaster.api.app;


import com.openklaster.api.handler.*;
import com.openklaster.api.handler.summary.SummaryCreator;
import com.openklaster.api.model.*;
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
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApiVerticle extends OpenklasterVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ApiVerticle.class);
    private static final int VERSION1 = 1;
    private static final String credentialsConfigKey = "manufacturerCredentials";
    private NestedConfigAccessor configAccessor;
    private Vertx vertx;
    private EventBus eventBus;
    private List<ApiHandler> handlers;

    public ApiVerticle(boolean isDevModeOn) {
        super(isDevModeOn);
    }

    public ApiVerticle() {
        super();
    }

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
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
                new PostCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.loginEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.LOGIN,
                        eventBus, configAccessor, new DefaultParseStrategy<Login>(Login.class)),

                new PostCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.REGISTER,
                        eventBus, configAccessor, new DefaultParseStrategy<Register>(Register.class)),

                new PutCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.UPDATE_USER,
                        eventBus, configAccessor, new DefaultParseStrategy<UpdateUser>(UpdateUser.class)),

                new GetCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.INFO,
                        eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)),

                new PostCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.GENERATE_TOKEN,
                        eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)),

                new DeleteCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.DELETE_TOKEN,
                        eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)),

                new DeleteCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.allTokensEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.DELETE_ALL_TOKENS,
                        eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)),

                new GetCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<InstallationRequest>(InstallationRequest.class)),

                new GetCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.allinstallationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey), EventbusMethods.GET_ALL,
                        eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)),

                new PostCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<PostInstallation>(PostInstallation.class)),

                new PutCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Installation>(Installation.class)),

                new DeleteCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<InstallationRequest>(InstallationRequest.class)),

                new GetCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementPowerRequest>(MeasurementPowerRequest.class)),

                new PostCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementPower>(MeasurementPower.class)),

                new GetCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerproductionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementPowerRequest>(MeasurementPowerRequest.class)),

                new PostCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerproductionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementPower>(MeasurementPower.class)),

                new GetCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyconsumedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementEnergyRequest>(MeasurementEnergyRequest.class)),

                new PostCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyconsumedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.consumptionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementEnergy>(MeasurementEnergy.class)),

                new GetCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyproducedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementEnergyRequest>(MeasurementEnergyRequest.class)),

                new PostCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyproducedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementEnergy>(MeasurementEnergy.class)),

                new SummaryCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.summaryEndpoint),
                        configAccessor.getString(EventBusAddressProperties.productionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<SummaryRequest>(SummaryRequest.class), new SummaryCreator()),
                new GetCoreCommunicationHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.chartEndpoint),
                        configAccessor.getString(EventBusAddressProperties.chartCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Temporary>(Temporary.class)),
                new CredentialsApiHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.manufacturerCredentials),
                        configAccessor.getJsonObject(credentialsConfigKey))
        );
        routerConfig(router);
    }

    private void routerConfig(Router router) {
        configureRouteHandler(router);
        handlers.forEach(handler -> handler.configure(router));
        router.route("/api/1/*").handler(StaticHandler.create("static"));
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
