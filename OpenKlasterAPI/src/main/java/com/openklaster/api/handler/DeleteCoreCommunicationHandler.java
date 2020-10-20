package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.parser.IParseStrategy;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;

public class DeleteCoreCommunicationHandler extends CoreCommunicationHandler {
    public DeleteCoreCommunicationHandler(String route, String address, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.deleteMethodHeader, route, HandlerProperties.deleteMethodHeader, address, eventBus, nestedConfigAccessor, parseStrategy);
    }

    public DeleteCoreCommunicationHandler(String route, String address, String eventbusMethod, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.deleteMethodHeader, route, eventbusMethod, address, eventBus, nestedConfigAccessor, parseStrategy);
    }

    @Override
    public void handle(RoutingContext context) {
        sendGetDeleteRequest(context);
    }
}
