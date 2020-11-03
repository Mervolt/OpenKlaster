package com.openklaster.api.handler.summary;

import com.openklaster.api.handler.properties.SummaryProperties;
import com.openklaster.api.model.Unit;
import com.openklaster.api.model.summary.EnvironmentalBenefits;
import com.openklaster.api.model.summary.Measurement;
import com.openklaster.api.model.summary.SummaryResponse;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SummaryCreator {

    public SummaryResponse createSummary(AsyncResult<Message<JsonArray>> response, EnvironmentalBenefits environmentalConfig) {
        Map<Unit, List<Measurement>> measurements = groupMeasurementsFromJsonArray(response.result().body());
        Measurement latestEnergyMeasurement = findLastMeasurement(measurements.get(Unit.kWh));
        Measurement latestPowerMeasurement = findLastMeasurement(measurements.get(Unit.kW));
        Map<String, Double> powerMeasurements = convertMeasurementArraysIntoMap(measurements.get(Unit.kW));
        BigDecimal energyProducedToday = countEnergyProducedToday(measurements.get(Unit.kWh));

        return SummaryResponse.builder()
                .totalEnergy(new BigDecimal(latestEnergyMeasurement.getValue()))
                .currentPower(new BigDecimal(latestPowerMeasurement.getValue()))
                .power(powerMeasurements)
                .energyProducedToday(energyProducedToday)
                .environmentalBenefits(environmentalConfig)
                .build();
    }


    private Map<String, Double> convertMeasurementArraysIntoMap(List<Measurement> measurements) {
        return measurements.stream()
                .filter(measurement -> isItToday(measurement.getTimestamp()))
                .collect(Collectors.toMap(a -> parseTimeFromTimeStamp(a.getTimestamp()),
                        Measurement::getValue,
                        (prev, next) -> next, HashMap::new));
    }

    private Measurement findLastMeasurement(List<Measurement> energyMeasurements) {
        return Optional.ofNullable(energyMeasurements)
                .orElse(Collections.singletonList(new Measurement()))
                .stream()
                .max(Comparator.comparing(Measurement::getTimestamp))
                .get();
    }

    private BigDecimal countEnergyProducedToday(List<Measurement> energyMeasurements) {
        Map<Object, List<Measurement>> booleanMeasurementMap = Optional.ofNullable(energyMeasurements).orElse(Collections.emptyList()).stream()
                .collect(Collectors.groupingBy(measurement -> isItToday(measurement.getTimestamp())));
        Double currentEnergy = findLastMeasurement(booleanMeasurementMap.get(true)).getValue();
        Double yesterdaysLastEnergyMeasurement = findLastMeasurement(booleanMeasurementMap.get(false)).getValue();
        if (yesterdaysLastEnergyMeasurement - currentEnergy > 0)
            return new BigDecimal(0);
        else
            return new BigDecimal(currentEnergy - yesterdaysLastEnergyMeasurement);
    }

    private Map<Unit, List<Measurement>> groupMeasurementsFromJsonArray(JsonArray jsonArray) {
        return jsonArray.stream()
                .map(result -> JsonObject.mapFrom(result).mapTo(Measurement.class))
                .collect(Collectors.groupingBy(Measurement::getUnit));
    }

    private String parseTimeFromTimeStamp(Date timestamp) {
        return new SimpleDateFormat(SummaryProperties.TIME_FORMAT).format(timestamp);
    }

    private boolean isItToday(Date timestamp) {
        return DateUtils.isSameDay(timestamp, new Date());
    }
}
