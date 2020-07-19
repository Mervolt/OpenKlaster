package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.parser.IParseStrategy;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class DeleteHandler extends Handler{
    public DeleteHandler(String route, String address, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.deleteMethodHeader, route, HandlerProperties.deleteMethodHeader, address, eventBus, nestedConfigAccessor, parseStrategy);
    }

    public DeleteHandler(String route, String address, String eventbusMethod, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.deleteMethodHeader, route, eventbusMethod, address, eventBus, nestedConfigAccessor, parseStrategy);
    }

    @Override
    public void handle(RoutingContext context) {
        DeliveryOptions deliveryOptions = createRequestDeliveryOptions(eventbusMethod, context);

        if(isGetDeleteRequestInvalid(context)) {
            handleUnprocessableRequest(context.response());
            return;
        }

        JsonObject jsonModel = convertMultiMapToJson(context.queryParams().entries());

        eventBus.request(address, jsonModel, deliveryOptions, coreResponse -> {
            if(gotCorrectResponse(coreResponse)){
                handleSuccessfulRequest(context.response());
            }
            else{
                handleProcessingError(context.response());
            }
        });
    }
}
