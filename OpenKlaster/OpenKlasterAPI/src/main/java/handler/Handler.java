package handler;

import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;
import parser.IParseStrategy;

public abstract class Handler {

    String route;
    EventBus eventBus;
    IParseStrategy parseStrategy;

    public Handler(String route, EventBus eventBus, IParseStrategy parseStrategy) {
        this.route = route;
        this.eventBus = eventBus;
        this.parseStrategy = parseStrategy;
    }

    public abstract void post(RoutingContext context);
    public abstract void get(RoutingContext context);
    public abstract void put(RoutingContext context);
    public abstract void delete(RoutingContext context);

    public String getRoute(){
        return this.route;
    };
}
