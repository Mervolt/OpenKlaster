package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.parser.IParseStrategy;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;

public class PutCoreCommunicationHandler extends CoreCommunicationHandler {
    public PutCoreCommunicationHandler(String route, String address, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.putMethodHeader, route, HandlerProperties.putMethodHeader, address, parseStrategy);
    }

    public PutCoreCommunicationHandler(String route, String address, String eventbusMethod, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.putMethodHeader, route, eventbusMethod, address, parseStrategy);
    }

    @Override
    public void handle(RoutingContext context, EventBus eventBus) {
        sendPutPostRequest(context, eventBus);
    }
}
