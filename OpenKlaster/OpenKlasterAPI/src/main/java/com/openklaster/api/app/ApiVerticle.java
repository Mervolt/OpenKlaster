package com.openklaster.api.app;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.openklaster.api.handler.DeleteHandler;
import com.openklaster.api.handler.GetHandler;
import com.openklaster.api.handler.Handler;
import com.openklaster.api.handler.PostHandler;
import com.openklaster.api.handler.PutHandler;
import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Installation;
import com.openklaster.api.model.InstallationRequest;
import com.openklaster.api.model.Login;
import com.openklaster.api.model.MeasurementEnergy;
import com.openklaster.api.model.MeasurementPower;
import com.openklaster.api.model.MeasurementRequest;
import com.openklaster.api.model.PostInstallation;
import com.openklaster.api.model.Register;
import com.openklaster.api.model.UpdateUser;
import com.openklaster.api.model.Username;
import com.openklaster.api.parser.DefaultParseStrategy;
import com.openklaster.api.properties.EndpointRouteProperties;
import com.openklaster.api.properties.EventBusAddressProperties;
import com.openklaster.api.properties.EventbusMethods;
import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.config.NestedConfigAccessor;
<<<<<<< HEAD:OpenKlaster/OpenKlasterAPI/src/main/java/com/openklaster/api/app/OpenKlasterAPIVerticle.java

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
=======
import com.openklaster.common.verticle.OpenklasterVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
>>>>>>> master:OpenKlaster/OpenKlasterAPI/src/main/java/com/openklaster/api/app/ApiVerticle.java
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class ApiVerticle extends OpenklasterVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ApiVerticle.class);
    private static final int VERSION1 = 1;
    private NestedConfigAccessor configAccessor;
    private Vertx vertx;
    private EventBus eventBus;
    private List<Handler> handlers;

    public ApiVerticle(boolean isDevModeOn) {
        super(isDevModeOn);
    }

    public ApiVerticle() {
        super();
    }

    @Override
<<<<<<< HEAD:OpenKlaster/OpenKlasterAPI/src/main/java/com/openklaster/api/app/OpenKlasterAPIVerticle.java
    public void start(Promise<Void> promise) {
        configRetriever.getConfig(config -> {
            if(config.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(config.result());
                startVerticle(promise);
            }
            else {
                logger.error(config.cause());
                vertx.close();
=======
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
>>>>>>> master:OpenKlaster/OpenKlasterAPI/src/main/java/com/openklaster/api/app/ApiVerticle.java
            }
        });
    }

    private void startVerticle() {
        Router router = Router.router(vertx);
        vertx.createHttpServer()
             .requestHandler(router)
             .listen(configAccessor.getInteger(EndpointRouteProperties.listeningPortKey));
        handlers = Arrays.asList(
            new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.loginEndpoint),
                            configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.LOGIN,
                            eventBus, configAccessor, new DefaultParseStrategy<Login>(Login.class)
            ),

            new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                            configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.REGISTER,
                            eventBus, configAccessor, new DefaultParseStrategy<Register>(Register.class)
            ),

            new PutHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                           configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.UPDATE_USER,
                           eventBus, configAccessor, new DefaultParseStrategy<UpdateUser>(UpdateUser.class)
            ),

            new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.userEndpoint),
                           configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.INFO,
                           eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)
            ),

            new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint),
                            configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.GENERATE_TOKEN,
                            eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)
            ),

            new DeleteHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.tokenEndpoint),
                              configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.DELETE_TOKEN,
                              eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)
            ),

            new DeleteHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.allTokensEndpoint),
                              configAccessor.getString(EventBusAddressProperties.userCoreAddressKey), EventbusMethods.DELETE_ALL_TOKENS,
                              eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)
            ),

            new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                           configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                           eventBus, configAccessor, new DefaultParseStrategy<InstallationRequest>(InstallationRequest.class)
            ),

            new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.allinstallationEndpoint),
                           configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey), EventbusMethods.GET_ALL,
                           eventBus, configAccessor, new DefaultParseStrategy<Username>(Username.class)
            ),

            new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                            configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                            eventBus, configAccessor, new DefaultParseStrategy<PostInstallation>(PostInstallation.class)
            ),

            new PutHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                           configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                           eventBus, configAccessor, new DefaultParseStrategy<Installation>(Installation.class)
            ),

            new DeleteHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.installationEndpoint),
                              configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                              eventBus, configAccessor, new DefaultParseStrategy<InstallationRequest>(InstallationRequest.class)
            ),

            new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint),
                           configAccessor.getString(EventBusAddressProperties.powerconsumptionCoreAddressKey),
                           eventBus, configAccessor, new DefaultParseStrategy<MeasurementRequest>(MeasurementRequest.class)
            ),

            new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerconsumptionEndpoint),
                            configAccessor.getString(EventBusAddressProperties.powerconsumptionCoreAddressKey),
                            eventBus, configAccessor, new DefaultParseStrategy<MeasurementPower>(MeasurementPower.class)
            ),

            new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerproductionEndpoint),
                           configAccessor.getString(EventBusAddressProperties.powerproductionCoreAddressKey),
                           eventBus, configAccessor, new DefaultParseStrategy<MeasurementRequest>(MeasurementRequest.class)
            ),

            new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.powerproductionEndpoint),
                            configAccessor.getString(EventBusAddressProperties.powerproductionCoreAddressKey),
                            eventBus, configAccessor, new DefaultParseStrategy<MeasurementPower>(MeasurementPower.class)
            ),

            new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyconsumedEndpoint),
                           configAccessor.getString(EventBusAddressProperties.energyconsumedCoreAddressKey),
                           eventBus, configAccessor, new DefaultParseStrategy<MeasurementRequest>(MeasurementRequest.class)
            ),

            new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyconsumedEndpoint),
                            configAccessor.getString(EventBusAddressProperties.energyconsumedCoreAddressKey),
                            eventBus, configAccessor, new DefaultParseStrategy<MeasurementEnergy>(MeasurementEnergy.class)
            ),

            new GetHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyproducedEndpoint),
                           configAccessor.getString(EventBusAddressProperties.energyproducedCoreAddressKey),
                           eventBus, configAccessor, new DefaultParseStrategy<MeasurementRequest>(MeasurementRequest.class)
            ),

            new PostHandler(buildEndpoint(configAccessor, VERSION1, EndpointRouteProperties.energyproducedEndpoint),
                            configAccessor.getString(EventBusAddressProperties.energyproducedCoreAddressKey),
                            eventBus, configAccessor, new DefaultParseStrategy<MeasurementEnergy>(MeasurementEnergy.class)
            )
        );
        routerConfig(router);
    }

    private void routerConfig(Router router) {
        handlers.forEach(handler -> {
            configureRouteHandler(router);
            switch(handler.getMethod()) {
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
<<<<<<< HEAD:OpenKlaster/OpenKlasterAPI/src/main/java/com/openklaster/api/app/OpenKlasterAPIVerticle.java
        router.route("/*").handler(StaticHandler.create("static"));
        promise.complete();
=======
>>>>>>> master:OpenKlaster/OpenKlasterAPI/src/main/java/com/openklaster/api/app/ApiVerticle.java
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
