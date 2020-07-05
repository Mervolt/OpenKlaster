package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.model.Model;
import com.openklaster.api.parser.IParseStrategy;

import java.util.List;
import java.util.Map;

public class DefaultHandler extends Handler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultHandler.class);
    private static final String requestDefaultTimeout = "eventBus.timeout";

    public DefaultHandler(String coreRoute, String route, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor,
                          IParseStrategy<? extends Model> parseStrategy) {
        super(coreRoute, route, eventBus, nestedConfigAccessor, parseStrategy);
    }


    @Override
    public void post(RoutingContext context) {
        sendPutPostRequest(context, HandlerProperties.postMethodHeader);
    }

    @Override
    public void get(RoutingContext context) {
        if(isGetDeleteRequestInvalid(context)) {
            handleUnprocessableRequest(context.response());
            return;
        }

        JsonObject jsonModel = context.getBodyAsJson();
        DeliveryOptions deliveryOptions = createRequestDeliveryOptions(HandlerProperties.getMethodHeader);

        eventBus.request(coreRoute, jsonModel, deliveryOptions, coreResponse -> {
            if(coreResponse.succeeded()){
                /*
                * TODO add already existing record com.openklaster.api.handler*/
                context.response().end(Json.encodePrettily(coreResponse.result().body()));
            }
            else{
                handleProcessingError(context.response());
            }
        });
    }

    @Override
    public void put(RoutingContext context) {
        sendPutPostRequest(context, HandlerProperties.putMethodHeader);
    }

    @Override
    public void delete(RoutingContext context) {
        if(isGetDeleteRequestInvalid(context)) {
            handleUnprocessableRequest(context.response());
            return;
        }

        JsonObject jsonModel = context.getBodyAsJson();
            DeliveryOptions deliveryOptions = createRequestDeliveryOptions(HandlerProperties.deleteMethodHeader);

            eventBus.request(coreRoute, jsonModel, deliveryOptions, coreResponse -> {
                if(coreResponse.succeeded()){
                    handleSuccessfulRequest(context.response());
                }
                else{
                    handleProcessingError(context.response());
                }
        });
    }

    private void sendPutPostRequest(RoutingContext context, String methodHeader) {
        if(isPutPostRequestInvalid(context)) {
            handleUnprocessableRequest(context.response());
            return;
        }

        JsonObject jsonModel = context.getBodyAsJson();
        DeliveryOptions deliveryOptions = createRequestDeliveryOptions(methodHeader);

        eventBus.request(coreRoute, jsonModel, deliveryOptions, coreResponse -> {
            if(coreResponse.succeeded()){
                handleSuccessfulRequest(context.response());
            }
            else{
                handleProcessingError(context.response());
            }
        });
    }

    private boolean isPutPostRequestInvalid(RoutingContext context){
        JsonObject jsonModel = context.getBodyAsJson();
        return isJsonModelUnprocessable(jsonModel);
    }

    private boolean isJsonModelUnprocessable(JsonObject jsonModel){
        return !isJsonModelValid(jsonModel);
    }

    private boolean isJsonModelValid(JsonObject jsonModel) {
        try{
            parseStrategy.parseToModel(jsonModel);
            return true;
        }
        catch(IllegalArgumentException ex){
            logger.error(ex.getStackTrace());
            return false;
        }
    }

    private void handleUnprocessableRequest(HttpServerResponse response){
        response.setStatusCode(HttpResponseStatus.UNPROCESSABLE_ENTITY.code());
        response.end(HandlerProperties.unprocessableEntityMessage);
    }

    private void handleSuccessfulRequest(HttpServerResponse response) {
        response.setStatusCode(HttpResponseStatus.OK.code());
        response.end(HandlerProperties.successfulRequestMessage);
    }

    private boolean isGetDeleteRequestInvalid(RoutingContext context){
        MultiMap params = context.queryParams();
        return areRequestParamsUnprocessable(params);
    }

    private boolean areRequestParamsUnprocessable(MultiMap modelParams){
        JsonObject jsonModel = convertMultiMapToJson(modelParams.entries());
        return isJsonModelUnprocessable(jsonModel);
    }

    private JsonObject convertMultiMapToJson(List<Map.Entry<String, String>> modelParams) {
        JsonObject jsonModel = new JsonObject();
        modelParams.forEach(entry -> jsonModel.put(entry.getKey(),entry.getValue()));
        return jsonModel;
    }

    private DeliveryOptions createRequestDeliveryOptions(String requestMethod){
        DeliveryOptions deliveryOptions = new DeliveryOptions();
        deliveryOptions.addHeader(HandlerProperties.methodKeyHeader, requestMethod);
        deliveryOptions.setSendTimeout(nestedConfigAccessor.getInteger(requestDefaultTimeout));
        return deliveryOptions;
    }

    private void handleProcessingError(HttpServerResponse response) {
        response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
        response.end(HandlerProperties.processingErrorMessage);
    }
}
