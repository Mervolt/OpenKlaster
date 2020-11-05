package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.parser.IParseStrategy;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;


public class DeleteCoreCommunicationHandler extends CoreCommunicationHandler {
    public DeleteCoreCommunicationHandler(String route, String address, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.deleteMethodHeader, route, HandlerProperties.deleteMethodHeader, address, parseStrategy);
    }

    public DeleteCoreCommunicationHandler(String route, String address, String eventbusMethod, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.deleteMethodHeader, route, eventbusMethod, address, parseStrategy);
    }

    @Override
    public void handle(RoutingContext context, EventBus eventBus) {
        sendGetDeleteRequest(context, eventBus);
    }

}
