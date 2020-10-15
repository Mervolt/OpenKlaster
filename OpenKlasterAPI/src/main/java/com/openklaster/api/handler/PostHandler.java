package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.parser.IParseStrategy;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;

public class PostHandler extends Handler {
    public PostHandler(String route, String address, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.postMethodHeader, route, HandlerProperties.postMethodHeader, address, nestedConfigAccessor, parseStrategy);
    }

    public PostHandler(String route, String address, String eventbusMethod, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.postMethodHeader, route, eventbusMethod, address, nestedConfigAccessor, parseStrategy);
    }

    @Override
    public void handle(RoutingContext context, EventBus eventBus) {
        sendPutPostRequest(context, eventBus);
    }
}
