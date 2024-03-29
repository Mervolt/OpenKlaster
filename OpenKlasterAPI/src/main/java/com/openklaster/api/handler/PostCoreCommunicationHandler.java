package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.parser.IParseStrategy;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;

public class PostCoreCommunicationHandler extends CoreCommunicationHandler {
    public PostCoreCommunicationHandler(String route, String address, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.postMethodHeader, route, HandlerProperties.postMethodHeader, address, parseStrategy);
    }

    public PostCoreCommunicationHandler(String route, String address, String eventbusMethod, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.postMethodHeader, route, eventbusMethod, address, parseStrategy);
    }

    @Override
    public void handle(RoutingContext context, EventBus eventBus) {
        sendPutPostRequest(context, eventBus);
    }
}
