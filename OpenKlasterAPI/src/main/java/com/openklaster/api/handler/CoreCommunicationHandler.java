package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.parser.IParseStrategy;
import com.openklaster.api.validation.ModelValidationErrorMessages;
import com.openklaster.api.validation.ValidationException;
import com.openklaster.common.messages.HttpReplyUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.openklaster.api.handler.properties.HandlerProperties.processingErrorMessage;
import static com.openklaster.api.validation.ValidationExecutor.validate;
import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;
import static com.openklaster.common.messages.BusMessageReplyUtils.isInternalServerError;

@AllArgsConstructor
public abstract class CoreCommunicationHandler implements ApiHandler {
    private static final String requestDefaultTimeout = "eventBus.timeout";
    private static final Logger logger = LoggerFactory.getLogger(CoreCommunicationHandler.class);

    String method;
    String route;
    String eventbusMethod;
    String address;
    IParseStrategy<? extends Model> parseStrategy;

    public void configure(Router router, EventBus eventBus) {
        switch (getMethod()) {
            case HandlerProperties.getMethodHeader:
                router.get(getRoute()).handler(context -> handle(context, eventBus));
                break;
            case HandlerProperties.postMethodHeader:
                router.post(getRoute()).consumes("application/json").handler(context -> handle(context, eventBus));
                break;
            case HandlerProperties.putMethodHeader:
                router.put(getRoute()).consumes("application/json").handler(context -> handle(context, eventBus));
                break;
            case HandlerProperties.deleteMethodHeader:
                router.delete(getRoute()).handler(context -> handle(context, eventBus));
                break;
        }
    }

    public String getRoute() {
        return this.route;
    }

    public String getMethod() {
        return method;
    }

    protected void sendGetDeleteRequest(RoutingContext context, EventBus eventBus) {
        Map<String, String> tokens = retrieveTokensFromContex(context);
        JsonObject jsonModel = convertMultiMapToJson(context.queryParams().entries());
        handleRequest(context, tokens, jsonModel, eventBus);
    }

    protected void sendPutPostRequest(RoutingContext context, EventBus eventBus) {
        Map<String, String> tokens = retrieveTokensFromContex(context);
        JsonObject jsonModel = context.getBodyAsJson();
        handleRequest(context, tokens, jsonModel, eventBus);
    }

    private void handleRequest(RoutingContext context, Map<String, String> tokens, JsonObject jsonModel, EventBus eventBus) {
        try {
            Model model = parseStrategy.parseToModel(jsonModel);
            validate(model, tokens);
            JsonObject validatedModel = JsonObject.mapFrom(model);
            DeliveryOptions deliveryOptions = createRequestDeliveryOptions(eventbusMethod, tokens);
            eventBus.request(address, validatedModel, deliveryOptions, coreResponse -> {
                if (coreResponse.succeeded()) {
                    if (coreResponse.result().body() == null) {
                        handleSuccessfulRequest(context.response());
                    } else {
                        HttpReplyUtils.sendJsonResponse(context.response(), coreResponse.result().body());
                    }
                    logger.debug("Successful request: " + coreResponse.result().body());
                } else {
                    logger.info(coreResponse.cause().getMessage());
                    ReplyException replyException = (ReplyException) coreResponse.cause();
                    if (isInternalServerError(replyException))
                        handleProcessingError(context.response(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), processingErrorMessage);
                    else
                        handleProcessingError(context.response(), replyException.failureCode(), replyException.getMessage());
                }
            });
        } catch (ValidationException e) {
            handleValidationError(context.response(), e.getMessage());
        }
    }


    protected JsonObject convertMultiMapToJson(List<Map.Entry<String, String>> modelParams) {
        JsonObject jsonModel = new JsonObject();
        modelParams.forEach(entry -> jsonModel.put(entry.getKey(), entry.getValue()));
        return jsonModel;
    }

    protected Map<String, String> retrieveTokensFromContex(RoutingContext context) {
        Map<String, String> tokens = new HashMap<>();
        if (context.queryParams().contains(HandlerProperties.apiToken)) {
            tokens.put(HandlerProperties.apiToken, context.queryParams().get(HandlerProperties.apiToken));
            context.queryParams().remove(HandlerProperties.apiToken);
        }
        if (context.queryParams().contains(HandlerProperties.sessionToken)) {
            tokens.put(HandlerProperties.sessionToken, context.queryParams().get(HandlerProperties.sessionToken));
            context.queryParams().remove(HandlerProperties.sessionToken);
        }
        if (context.queryParams().contains(HandlerProperties.technicalToken)) {
            tokens.put(HandlerProperties.technicalToken, context.queryParams().get(HandlerProperties.technicalToken));
            context.queryParams().remove(HandlerProperties.technicalToken);
        }
        return tokens;
    }

    protected DeliveryOptions createRequestDeliveryOptions(String eventbusMethod, Map<String, String> tokens) {
        DeliveryOptions deliveryOptions = new DeliveryOptions();
        for (String token : tokens.keySet()) {
            deliveryOptions.addHeader(token, tokens.get(token));
        }
        deliveryOptions.addHeader(METHOD_KEY, eventbusMethod);
        return deliveryOptions;
    }

    protected void handleSuccessfulRequest(HttpServerResponse response) {
        HttpReplyUtils.sendOkEmptyResponse(response);
        logger.debug("Successful request for: " + response);
    }

    protected void handleProcessingError(HttpServerResponse response, final int code, final String message) {
        HttpReplyUtils.sendFailureResponse(response, code, message);
        logger.error("Failure handling" + message + "code " + code);
    }

    protected void handleValidationError(HttpServerResponse response, String message) {
        String messageContent = ModelValidationErrorMessages.MESSAGE + message;
        HttpReplyUtils.sendFailureResponse(response, HttpResponseStatus.BAD_REQUEST.code(), messageContent);
        logger.error(messageContent);
    }

    @Override
    public String toString() {
        return "Handler{" +
                "method='" + method + '\'' +
                ", route='" + route + '\'' +
                ", eventbusMethod='" + eventbusMethod + '\'' +
                ", address='" + address + '\'' +
                ", parseStrategy=" + parseStrategy +
                '}';
    }
}
