package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.SummaryProperties;
import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.model.Model;
import com.openklaster.api.model.Unit;
import com.openklaster.api.model.summary.EnvironmentalBenefits;
import com.openklaster.api.model.summary.Measurement;
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
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.*;
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

                    Measurement energyMeasurement = getLatestMeasurement(measurements.get(Unit.kWh));

                    Map<String, Double> powerMeasurements = measurements.get(Unit.kW).stream()
                            .filter(measurement -> isItToday(measurement.getTimestamp()))
                            .collect(Collectors.toMap(a -> getTimeFromTimeStamp(a.getTimestamp()),
                                    Measurement::getValue,
                                    (prev, next) -> next, HashMap::new));

                    EnvironmentalBenefits environmentalBenefits = EnvironmentalBenefits.builder()
                            .co2reduced(getEnvironmentalBenefit(SummaryProperties.CO2REDUCED, energyMeasurement.getValue()))
                            .treessaved(getEnvironmentalBenefit(SummaryProperties.TREES_SAVED, energyMeasurement.getValue()))
                            .build();

                    SummaryResponse summaryResponse = new SummaryResponse(energyMeasurement.getValue(), powerMeasurements, environmentalBenefits);
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

    private Measurement getLatestMeasurement(List<Measurement> energyMeasurements) {
        return Optional.ofNullable(energyMeasurements)
                .orElse(Collections.singletonList(new Measurement()))
                .stream()
                .max(Comparator.comparing(Measurement::getTimestamp))
                .get();
    }

    private int getEnvironmentalBenefit(String path, double energy) {
        return (int) (((double) nestedConfigAccessor.getInteger(path) / 100) * energy);
    }

    private String getTimeFromTimeStamp(Date timestamp) {
        return new SimpleDateFormat(SummaryProperties.TIME_FORMAT).format(timestamp);
    }

    private boolean isItToday(Date timestamp) {
        Date today = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
        return today.equals(DateUtils.truncate(timestamp, java.util.Calendar.DAY_OF_MONTH));
    }
}
