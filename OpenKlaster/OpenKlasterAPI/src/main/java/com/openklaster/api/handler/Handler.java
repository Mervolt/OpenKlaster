package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.parser.IParseStrategy;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.api.model.Model;

import java.util.List;
import java.util.Map;

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



    protected void sendPutPostRequest(RoutingContext context, String eventbusMethod) {

        DeliveryOptions deliveryOptions = createRequestDeliveryOptions(eventbusMethod, context);

        if(isPutPostRequestInvalid(context)) {
            handleUnprocessableRequest(context.response());
            return;
        }

        JsonObject jsonModel = context.getBodyAsJson();
        eventBus.request(address, jsonModel, deliveryOptions, coreResponse -> {
            if(gotCorrectResponse(coreResponse)){
                handleSuccessfulRequest(context.response());
            }
            else{
                handleProcessingError(context.response());
            }
        });
    }

    protected boolean isPutPostRequestInvalid(RoutingContext context){
        JsonObject jsonModel = context.getBodyAsJson();
        return isJsonModelUnprocessable(jsonModel);
    }

    protected boolean isJsonModelUnprocessable(JsonObject jsonModel){
        return !isJsonModelValid(jsonModel);
    }

    protected boolean isJsonModelValid(JsonObject jsonModel) {
        try{
            parseStrategy.parseToModel(jsonModel);
            return true;
        }
        catch(IllegalArgumentException ex){
            ex.printStackTrace();
            return false;
        }
    }

    protected void handleUnprocessableRequest(HttpServerResponse response){
        response.setStatusCode(HttpResponseStatus.UNPROCESSABLE_ENTITY.code());
        response.end(HandlerProperties.unprocessableEntityMessage);
    }

    protected void handleSuccessfulRequest(HttpServerResponse response) {
        response.setStatusCode(HttpResponseStatus.OK.code());
        response.end(HandlerProperties.successfulRequestMessage);
    }

    protected boolean isGetDeleteRequestInvalid(RoutingContext context){
        MultiMap params = context.queryParams();
        return areRequestParamsUnprocessable(params);
    }

    protected boolean areRequestParamsUnprocessable(MultiMap modelParams){
        JsonObject jsonModel = convertMultiMapToJson(modelParams.entries());
        return isJsonModelUnprocessable(jsonModel);
    }

    protected JsonObject convertMultiMapToJson(List<Map.Entry<String, String>> modelParams) {
        JsonObject jsonModel = new JsonObject();
        modelParams.forEach(entry -> jsonModel.put(entry.getKey(),entry.getValue()));
        return jsonModel;
    }

    protected DeliveryOptions createRequestDeliveryOptions(String eventbusMethod, RoutingContext context){
        DeliveryOptions deliveryOptions = new DeliveryOptions();
        if (context.queryParams().contains(HandlerProperties.apiToken)) {
            deliveryOptions.addHeader(HandlerProperties.apiToken, context.queryParams().get(HandlerProperties.apiToken));
            context.queryParams().remove(HandlerProperties.apiToken);
        }
        if (context.queryParams().contains(HandlerProperties.sessionToken)) {
            deliveryOptions.addHeader(HandlerProperties.sessionToken, context.queryParams().get(HandlerProperties.sessionToken));
            context.queryParams().remove(HandlerProperties.sessionToken);
        }

        deliveryOptions.addHeader(HandlerProperties.methodKeyHeader, eventbusMethod);
        deliveryOptions.setSendTimeout(nestedConfigAccessor.getInteger(requestDefaultTimeout));
        return deliveryOptions;
    }

    protected void handleProcessingError(HttpServerResponse response) {
        response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
        response.end(HandlerProperties.processingErrorMessage);
    }

    protected boolean gotCorrectResponse(AsyncResult<Message<Object>> coreResponse) {
        boolean gotResponse = coreResponse.succeeded();
        boolean isResponseCorrect = coreResponse.result().headers().get("statusCode").equals("200");
        return gotResponse && isResponseCorrect;
    }
}
