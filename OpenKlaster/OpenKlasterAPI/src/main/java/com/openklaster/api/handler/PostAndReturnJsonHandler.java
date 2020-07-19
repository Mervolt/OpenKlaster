package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.parser.IParseStrategy;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class PostAndReturnJsonHandler extends Handler {
    public PostAndReturnJsonHandler(String route, String address, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.postMethodHeader, route, HandlerProperties.postMethodHeader, address, eventBus, nestedConfigAccessor, parseStrategy);
    }

    public PostAndReturnJsonHandler(String route, String address, String eventbusMethod, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.postMethodHeader, route, eventbusMethod, address, eventBus, nestedConfigAccessor, parseStrategy);
    }

    @Override
    public void handle(RoutingContext context) {
        DeliveryOptions deliveryOptions = createRequestDeliveryOptions(eventbusMethod, context);

        if(isPutPostRequestInvalid(context)) {
            handleUnprocessableRequest(context.response());
            return;
        }

        JsonObject jsonModel = context.getBodyAsJson();

        eventBus.request(address, jsonModel, deliveryOptions, coreResponse -> {
            if(gotCorrectResponse(coreResponse)){
                context.response().end(Json.encodePrettily(coreResponse.result().body()));
            }
            else{
                handleProcessingError(context.response());
            }
        });
    }
}
