package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.parser.IParseStrategy;
import com.openklaster.api.validation.ValidationException;
import com.openklaster.common.config.NestedConfigAccessor;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

import static com.openklaster.api.validation.ValidationExecutor.validate;

public class GetHandler extends Handler{
    public GetHandler(String route, String address, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.getMethodHeader, route, HandlerProperties.getMethodHeader, address, eventBus, nestedConfigAccessor, parseStrategy);
    }

    public GetHandler(String route, String address, String eventbusMethod, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.getMethodHeader, route, eventbusMethod, address, eventBus, nestedConfigAccessor, parseStrategy);
    }

    @Override
    public void handle(RoutingContext context) {
        try {
            Map<String, String> tokens = retrieveTokensFromContex(context);
            JsonObject jsonModel = convertMultiMapToJson(context.queryParams().entries());

            Model model = parseStrategy.parseToModel(jsonModel);
            validate(model, tokens);
            JsonObject validatedModel = JsonObject.mapFrom(model);
            DeliveryOptions deliveryOptions = createRequestDeliveryOptions(eventbusMethod, tokens);

            eventBus.request(address, validatedModel, deliveryOptions, coreResponse -> {
                System.out.println(validatedModel);
                if(gotCorrectResponse(coreResponse)){
                    context.response().end(Json.encodePrettily(coreResponse.result().body()));
                }
                else{
                    handleProcessingError(context.response());
                }
            });
        } catch (ValidationException e) {
            context.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
            context.response().end(e.getMessage());
        }
    }
}
