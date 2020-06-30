package handler;

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

    private static String access_token = "access_token";
    private static int requestTimeout = 15000;


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
        DeliveryOptions deliveryOptions = createRequestDeliveryOptions("post");

        eventBus.request(coreRoute, jsonModel, deliveryOptions, coreResponse -> {
            if(coreResponse.succeeded()){
                handleSuccessfulRequest(context.response());
            }
            else{
                context.response().putHeader("content-type", "text/html")
                        .end("<h1>Errors during processing the request</h1>");
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
        DeliveryOptions deliveryOptions = createRequestDeliveryOptions("get");

        eventBus.request(coreRoute, jsonModel, deliveryOptions, coreResponse -> {
            if(coreResponse.succeeded()){
                /*
                * TODO add already existing handler*/
                context.response().putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(coreResponse.result().body()));
            }
            else{
                context.response().putHeader("content-type", "text/html")
                        .end("<h1>Errors during processing the request</h1>");
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
        DeliveryOptions deliveryOptions = createRequestDeliveryOptions("put");

        eventBus.request(coreRoute, jsonModel, deliveryOptions, coreResponse -> {
            if(coreResponse.succeeded()){
                handleSuccessfulRequest(context.response());
            }
            else{
                context.response().putHeader("content-type", "text/html")
                        .end("<h1>Errors during processing the request</h1>");
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
        DeliveryOptions deliveryOptions = createRequestDeliveryOptions("delete");

        eventBus.request(coreRoute, jsonModel, deliveryOptions, coreResponse -> {
            if(coreResponse.succeeded()){
                handleSuccessfulRequest(context.response());
            }
            else{
                context.response().putHeader("content-type", "text/html")
                        .end("<h1>Errors during processing the request</h1>");
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
        response.setStatusCode(422);
        response.setStatusMessage("Request entity is unprocessable for this request");
        response.end();
    }

    private void handleSuccessfulRequest(HttpServerResponse response) {
        response.setStatusCode(200);
        response.setStatusMessage("Successful request");
        response.end();
    }


    private boolean isGetDeleteRequestInvalid(RoutingContext context){
        MultiMap params = context.queryParams();
        params.remove(access_token);
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
        deliveryOptions.addHeader("method", requestMethod);
        deliveryOptions.setSendTimeout(requestTimeout);
        return deliveryOptions;
    }





}
