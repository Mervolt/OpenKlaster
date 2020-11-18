package com.openklaster.api.handler;

import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public interface ApiHandler {
    void handle(RoutingContext context, EventBus eventBus);

    void configure(Router router, EventBus eventBus);
}
