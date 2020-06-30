package handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import model.Model;
import parser.IParseStrategy;

import java.util.List;
import java.util.Map;

public class DefaultHandler extends Handler {
    public DefaultHandler(String coreRoute, String route, EventBus eventBus, IParseStrategy<? extends Model> parseStrategy) {
        super(coreRoute, route, eventBus, parseStrategy);
    }

    @Override
    public void post(RoutingContext context) {
        if(isPutPostRequestInvalid(context)) {
            handleUnprocessableRequest(context.response());
            return;
        }

        JsonObject jsonModel = context.getBodyAsJson();
        DeliveryOptions deliveryOptions = createRequestDeliveryOptions(HandlerProperties.postMethodHeader);

        eventBus.request(coreRoute, jsonModel, deliveryOptions, coreResponse -> {
            if(coreResponse.succeeded()){
                handleSuccessfulRequest(context.response());
            }
            else{
                handleProcessingError(context.response());
            }
        });
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
                * TODO add already existing record handler*/
                context.response().putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(coreResponse.result().body()));
            }
            else{
                handleProcessingError(context.response());
            }
        });
    }

    @Override
    public void put(RoutingContext context) {
        if(isPutPostRequestInvalid(context)) {
            handleUnprocessableRequest(context.response());
            return;
        }

        JsonObject jsonModel = context.getBodyAsJson();
        DeliveryOptions deliveryOptions = createRequestDeliveryOptions(HandlerProperties.putMethodHeader);

        eventBus.request(coreRoute, jsonModel, deliveryOptions, coreResponse -> {
            if(coreResponse.succeeded()){
                handleSuccessfulRequest(context.response());
            }
            else{
                handleProcessingError(context.response());
            }
        });
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
            /*
             * some logging...
             * */
            ex.printStackTrace();
            return false;
        }
    }

    private void handleUnprocessableRequest(HttpServerResponse response){
        response.setStatusCode(HttpResponseStatus.UNPROCESSABLE_ENTITY.code());
        response.setStatusMessage(HandlerProperties.unprocessableEntityMessage);
        response.end();
    }

    private void handleSuccessfulRequest(HttpServerResponse response) {
        response.setStatusCode(HttpResponseStatus.OK.code());
        response.setStatusMessage(HandlerProperties.successfulRequestMessage);
        response.end();
    }

    private boolean isGetDeleteRequestInvalid(RoutingContext context){
        MultiMap params = context.queryParams();
        params.remove(HandlerProperties.accessToken);
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
        deliveryOptions.setSendTimeout(HandlerProperties.requestDefaultTimeout);
        return deliveryOptions;
    }

    private void handleProcessingError(HttpServerResponse response) {
        response.putHeader("content-type", "text/html");
        response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
        response.end(HandlerProperties.processingErrorMessage);
    }
}
