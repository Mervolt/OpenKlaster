package com.openklaster.api.app;


import com.openklaster.api.APIVerticleConfig;
import com.openklaster.api.handler.ApiHandler;
import com.openklaster.api.handler.CredentialsApiHandler;
import com.openklaster.common.verticle.OpenklasterVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApiVerticle extends OpenklasterVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ApiVerticle.class);
    private Vertx vertx;
    private EventBus eventBus;
    private List<ApiHandler> handlers;
    private GenericApplicationContext ctx;
    private Integer launchPort;

    public ApiVerticle(boolean isDevModeOn) {
        super(isDevModeOn);
    }

    public ApiVerticle() {
        super();
    }

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        ctx = new AnnotationConfigApplicationContext(APIVerticleConfig.class);
        ctx.registerBean(EventBus.class, () -> eventBus);
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        launchPort = ctx.getBean(Integer.class);
        startVerticle();
    }

    private void startVerticle() {
        Router router = Router.router(vertx);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(launchPort);
        handlers = new ArrayList<>(ctx.getBeansOfType(ApiHandler.class).values());
        System.out.println(handlers.size());
        routerConfig(router);
    }

    private void routerConfig(Router router) {
        configureRouteHandler(router);
        handlers.forEach(handler -> handler.configure(router, eventBus));
        handlers.forEach(handler -> System.out.println());
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
}
