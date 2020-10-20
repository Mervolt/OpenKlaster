package com.openklaster.api.handler;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public interface ApiHandler {
    void handle(RoutingContext context);
    void configure(Router router);
}
