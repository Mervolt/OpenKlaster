package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Measurement;
import com.openklaster.api.model.Model;
import com.openklaster.api.model.SummaryResponse;
import com.openklaster.api.model.Unit;
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

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.openklaster.api.validation.ValidationExecutor.validate;

public class SummaryHandler extends Handler {

    public SummaryHandler(String route, String address, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy) {
        super(HandlerProperties.getMethodHeader, route, HandlerProperties.getMethodHeader, address, eventBus, nestedConfigAccessor, parseStrategy);
    }

    @Override
    public void handle(RoutingContext context) {
        Map<String, String> tokens = retrieveTokensFromContex(context);
        JsonObject jsonModel = convertMultiMapToJson(context.queryParams().entries());
        try {
            Model model = parseStrategy.parseToModel(jsonModel);
            validate(model, tokens);
            JsonObject validatedModel = JsonObject.mapFrom(model);
            DeliveryOptions deliveryOptions = createRequestDeliveryOptions(eventbusMethod, tokens);

            eventBus.<JsonArray>request(address, validatedModel, deliveryOptions, coreResponse -> {
                if (coreResponse.succeeded()) {
                    Map<Unit, List<Measurement>> measurements = coreResponse.result().body().stream()
                            .map(result -> JsonObject.mapFrom(result).mapTo(Measurement.class))
                            .collect(Collectors.groupingBy(Measurement::getUnit));
                    SummaryResponse summaryResponse = new SummaryResponse(getLatestMeasurement(measurements.get(Unit.kWh)), measurements.get(Unit.kW));
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

    private Measurement getLatestMeasurement(List<Measurement> measurements) {
        return measurements.stream().max(Comparator.comparing(Measurement::getTimestamp)).get();
    }
}
