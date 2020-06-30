package handler;

import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;
import model.Model;
import parser.IParseStrategy;

public abstract class Handler {

    String coreRoute;
    String route;
    EventBus eventBus;
    IParseStrategy<? extends Model> parseStrategy;

    public Handler(String coreRoute, String route, EventBus eventBus, IParseStrategy<? extends Model> parseStrategy) {
        this.coreRoute = coreRoute;
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
