package com.openklaster.core.app;

import com.openklaster.common.verticle.OpenklasterVerticle;
import com.openklaster.core.CoreVerticleConfig;
import com.openklaster.core.service.EndpointService;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CoreVerticle extends OpenklasterVerticle {

    private static final Logger logger = LoggerFactory.getLogger(CoreVerticle.class);
    private EventBus eventBus;
    private GenericApplicationContext ctx;

    public CoreVerticle() {
        super();
    }

    public CoreVerticle(boolean isDevModeOn) {
        super(isDevModeOn);
    }

    @Override
    public void init(Vertx vertx, Context context) {
        ctx = new AnnotationConfigApplicationContext(CoreVerticleConfig.class);
        ctx.registerBean(EventBus.class, () -> eventBus);
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        configureEndpoints();
    }

    private void configureEndpoints() {
        Map<String, EndpointService> services = ctx.getBeansOfType(EndpointService.class);
        List<EndpointService> servicesList = new ArrayList<>(services.values());
        servicesList.forEach(service -> service.configureEndpoints(eventBus));
    }
}
