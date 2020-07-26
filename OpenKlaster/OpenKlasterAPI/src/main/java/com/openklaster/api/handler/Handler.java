package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.parser.IParseStrategy;
import com.openklaster.api.validation.ValidationException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.api.model.Model;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.openklaster.api.validation.ValidationExecutor.validate;
import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;

@AllArgsConstructor
public abstract class Handler {
    private static final String requestDefaultTimeout = "eventBus.timeout";
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);;

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
        try {
            Map<String, String> tokens = retrieveTokensFromContex(context);
            JsonObject jsonModel =context.getBodyAsJson();

            Model model = parseStrategy.parseToModel(jsonModel);
            validate(model, tokens);
            JsonObject validatedModel = JsonObject.mapFrom(model);
            DeliveryOptions deliveryOptions = createRequestDeliveryOptions(eventbusMethod, tokens);

            eventBus.request(address, validatedModel, deliveryOptions, coreResponse -> {
                if(gotCorrectResponse(coreResponse)){
                    handleSuccessfulRequest(context.response());
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

    protected boolean isPutPostRequestInvalid(RoutingContext context){
        JsonObject jsonModel = context.getBodyAsJson();
        return isJsonModelUnprocessable(jsonModel);
    }

    protected boolean isGetDeleteRequestInvalid(RoutingContext context){
        MultiMap params = context.queryParams();
        JsonObject jsonModel = convertMultiMapToJson(params.entries());
        params.entries().forEach(entry -> jsonModel.put(entry.getKey(),entry.getValue()));
        System.out.println(jsonModel);
        return isJsonModelUnprocessable(jsonModel);
    }

    protected boolean isJsonModelUnprocessable(JsonObject jsonModel){
        return !isJsonModelValid(jsonModel);
    }

    // Todo
    protected boolean isJsonModelValid(JsonObject jsonModel) {
        try{


            System.out.println("1");

            return true;
        }
        catch(IllegalArgumentException ex){
            //logger.error(ex.getMessage().substring(0, ex.getMessage().indexOf(" (class")));
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

    protected void handleProcessingError(HttpServerResponse response) {
        response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
        response.end(HandlerProperties.processingErrorMessage);
    }

    protected boolean gotCorrectResponse(AsyncResult<Message<Object>> coreResponse) {
        boolean gotResponse = coreResponse.succeeded();
        return gotResponse;
    }


}
