package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.parser.IParseStrategy;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;

public class PutHandler extends Handler {
    public PutHandler(String route, String address, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.putMethodHeader, route, HandlerProperties.putMethodHeader, address, nestedConfigAccessor, parseStrategy);
    }

    public PutHandler(String route, String address, String eventbusMethod, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.putMethodHeader, route, eventbusMethod, address, nestedConfigAccessor, parseStrategy);
    }

    @Override
    public void handle(RoutingContext context, EventBus eventBus) {
        sendPutPostRequest(context, eventBus);
    }
}
