package com.openklaster.api.handler;

import com.openklaster.api.parser.IParseStrategy;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;
import openklaster.common.config.NestedConfigAccessor;
import openklaster.common.model.Model;

public abstract class Handler {

    String route;
    String coreRoute;
    EventBus eventBus;
    IParseStrategy<? extends Model> parseStrategy;
    NestedConfigAccessor nestedConfigAccessor;

    public Handler(String route, String coreRoute, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor,
                   IParseStrategy<? extends Model> parseStrategy) {
        this.route = route;
        this.coreRoute = coreRoute;
        this.eventBus = eventBus;
        this.nestedConfigAccessor = nestedConfigAccessor;
        this.parseStrategy = parseStrategy;
    }

    public abstract void post(RoutingContext context);

    public abstract void get(RoutingContext context);

    public abstract void put(RoutingContext context);

    public abstract void delete(RoutingContext context);

    public String getRoute() {
        return this.route;
    }

}
