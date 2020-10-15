package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.handler.summary.SummaryCreator;
import com.openklaster.api.model.Model;
import com.openklaster.api.model.summary.SummaryResponse;
import com.openklaster.api.parser.IParseStrategy;
import com.openklaster.api.validation.ValidationException;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

import static com.openklaster.api.validation.ValidationExecutor.validate;

public class SummaryHandler extends Handler {
    private final SummaryCreator summaryCreator;

    public SummaryHandler(String route, String address, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy, SummaryCreator summaryCreator) {
        super(HandlerProperties.getMethodHeader, route, HandlerProperties.getMethodHeader, address, nestedConfigAccessor, parseStrategy);
        this.summaryCreator = summaryCreator;
    }

    @Override
    public void handle(RoutingContext context, EventBus eventBus) {
        Map<String, String> tokens = retrieveTokensFromContex(context);
        JsonObject jsonModel = convertMultiMapToJson(context.queryParams().entries());
        try {
            Model model = parseStrategy.parseToModel(jsonModel);
            validate(model, tokens);
            JsonObject validatedModel = JsonObject.mapFrom(model);
            DeliveryOptions deliveryOptions = createRequestDeliveryOptions(eventbusMethod, tokens);

            eventBus.<JsonArray>request(address, validatedModel, deliveryOptions, coreResponse -> {
                if (coreResponse.succeeded()) {
                    SummaryResponse summaryResponse = summaryCreator.createSummary(coreResponse, nestedConfigAccessor);
                    context.response().end(Json.encodePrettily(JsonObject.mapFrom(summaryResponse)));
                } else {
                    ReplyException replyException = (ReplyException) coreResponse.cause();
                    handleProcessingError(context.response(), replyException.failureCode(), replyException.getMessage());
                }
            });
        } catch (ValidationException e) {
            handleValidationError(context.response(), e.getMessage());
        }
    }
}
