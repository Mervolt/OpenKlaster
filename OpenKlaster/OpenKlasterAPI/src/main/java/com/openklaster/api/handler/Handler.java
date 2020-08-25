package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.parser.IParseStrategy;
import com.openklaster.api.validation.ModelValidationErrorMessages;
import com.openklaster.api.validation.ValidationException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.api.model.Model;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.openklaster.api.handler.properties.HandlerProperties.processingErrorMessage;
import static com.openklaster.api.validation.ValidationExecutor.validate;
import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;

@AllArgsConstructor
public abstract class Handler {
    private static final String requestDefaultTimeout = "eventBus.timeout";

    String method;
    String route;
    String eventbusMethod;
    String address;
    EventBus eventBus;
    IParseStrategy<? extends Model> parseStrategy;
    NestedConfigAccessor nestedConfigAccessor;

    public Handler(String method, String route, String eventbusMethod, String address, EventBus eventBus,
                   NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        this.method = method;
        this.route = route;
        this.eventbusMethod = eventbusMethod;
        this.address = address;
        this.eventBus = eventBus;
        this.nestedConfigAccessor = nestedConfigAccessor;
        this.parseStrategy = parseStrategy;
    }

    public abstract void handle(RoutingContext context);

    public String getRoute() {
        return this.route;
    }

    public String getMethod() {
        return method;
    }

    protected void sendGetDeleteRequest(RoutingContext context) {
        Map<String, String> tokens = retrieveTokensFromContex(context);
        JsonObject jsonModel = convertMultiMapToJson(context.queryParams().entries());
        handleRequest(context, tokens, jsonModel);
    }

    protected void sendPutPostRequest(RoutingContext context) {
        Map<String, String> tokens = retrieveTokensFromContex(context);
        JsonObject jsonModel = context.getBodyAsJson();
        handleRequest(context, tokens, jsonModel);
    }

    private void handleRequest(RoutingContext context, Map<String, String> tokens, JsonObject jsonModel) {
        try {
            Model model = parseStrategy.parseToModel(jsonModel);
            validate(model, tokens);
            JsonObject validatedModel = JsonObject.mapFrom(model);
            DeliveryOptions deliveryOptions = createRequestDeliveryOptions(eventbusMethod, tokens);

            eventBus.request(address, validatedModel, deliveryOptions, coreResponse -> {
                if(coreResponse.succeeded()){
                    if (coreResponse.result().body() == null)
                        handleSuccessfulRequest(context.response());
                    else
                        context.response().end(Json.encodePrettily(coreResponse.result().body()));
                }
                else{
                    ReplyException replyException = (ReplyException) coreResponse.cause();
                    if (replyException.failureCode() > 0)
                        handleProcessingError(context.response(), replyException.failureCode(), replyException.getMessage());
                    else
                        handleProcessingError(context.response(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), processingErrorMessage);
                }
            });
        } catch (ValidationException e) {
            handleValidationError(context.response(), e.getMessage());
        }
    }


    protected JsonObject convertMultiMapToJson(List<Map.Entry<String, String>> modelParams) {
        JsonObject jsonModel = new JsonObject();
        modelParams.forEach(entry -> jsonModel.put(entry.getKey(),entry.getValue()));
        return jsonModel;
    }

    protected Map<String, String>  retrieveTokensFromContex(RoutingContext context){
        Map<String, String> tokens = new HashMap<>();
        if (context.queryParams().contains(HandlerProperties.apiToken)) {
            tokens.put(HandlerProperties.apiToken, context.queryParams().get(HandlerProperties.apiToken));
            context.queryParams().remove(HandlerProperties.apiToken);
        }
        if (context.queryParams().contains(HandlerProperties.sessionToken)) {
            tokens.put(HandlerProperties.sessionToken, context.queryParams().get(HandlerProperties.sessionToken));
            context.queryParams().remove(HandlerProperties.sessionToken);
        }
        return tokens;
    }


    protected DeliveryOptions createRequestDeliveryOptions(String eventbusMethod, Map<String, String> tokens){
        DeliveryOptions deliveryOptions = new DeliveryOptions();
        for (String token : tokens.keySet()) {
            deliveryOptions.addHeader(token, tokens.get(token));
        }
        deliveryOptions.addHeader(METHOD_KEY, eventbusMethod);
        deliveryOptions.setSendTimeout(nestedConfigAccessor.getInteger(requestDefaultTimeout));
        return deliveryOptions;
    }

    private void handleSuccessfulRequest(HttpServerResponse response) {
        response.setStatusCode(HttpResponseStatus.OK.code());
        response.end(HandlerProperties.successfulRequestMessage);
    }

    private void handleProcessingError(HttpServerResponse response, final int code, final String message) {
        response.setStatusCode(code);
        response.end(message);
    }

    private void handleValidationError(HttpServerResponse response, String message) {
        response.setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
        response.end(ModelValidationErrorMessages.MESSAGE + message);
    }
}
