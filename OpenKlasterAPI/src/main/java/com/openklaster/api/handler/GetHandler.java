package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.parser.IParseStrategy;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;

public class GetHandler extends Handler {
    public GetHandler(String route, String address, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.getMethodHeader, route, HandlerProperties.getMethodHeader, address, parseStrategy);
    }

    public GetHandler(String route, String address, String eventbusMethod, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.getMethodHeader, route, eventbusMethod, address, parseStrategy);
    }

    @Override
    public void handle(RoutingContext context, EventBus eventBus) {
        sendGetDeleteRequest(context, eventBus);
    }
}
