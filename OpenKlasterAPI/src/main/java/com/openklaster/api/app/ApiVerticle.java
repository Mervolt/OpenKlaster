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
                (PostHandler) ctx.getBean("postHandlerLogin"),
                (PostHandler) ctx.getBean("postHandlerRegister"),
                (PostHandler) ctx.getBean("postHandlerUsername"),
                (PostHandler) ctx.getBean("postHandlerPostInstallation"),
                (PostHandler) ctx.getBean("postHandlerMeasurementPower"),
                (PostHandler) ctx.getBean("postHandlerMeasurementEnergyConsumed"),
                (PostHandler) ctx.getBean("postHandlerMeasurementEnergyProduced"),
                (PutHandler) ctx.getBean("putHandlerUpdateUser"),
                (PutHandler) ctx.getBean("putHandlerInstallation"),
                (GetHandler) ctx.getBean("getHandlerUser"),
                (GetHandler) ctx.getBean("getHandlerInstallationRequest"),
                (GetHandler) ctx.getBean("getHandlerAllInstallationsRequest"),
                (GetHandler) ctx.getBean("getHandlerMeasurementPowerRequestConsumption"),
                (GetHandler) ctx.getBean("getHandlerMeasurementPowerRequestProduction"),
                (GetHandler) ctx.getBean("getHandlerMeasurementEnergyRequestConsumption"),
                (GetHandler) ctx.getBean("getHandlerMeasurementEnergyRequestProduction"),
                (DeleteHandler) ctx.getBean("deleteHandlerUsernameToken"),
                (DeleteHandler) ctx.getBean("deleteHandlerUsernameAllTokens"),
                (DeleteHandler) ctx.getBean("deleteHandlerInstallationRequest"),
                (SummaryHandler) ctx.getBean("summaryHandler"));
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
