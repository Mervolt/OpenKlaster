package com.openklaster.api.handler.summary;

import com.openklaster.api.handler.properties.SummaryProperties;
import com.openklaster.api.model.Unit;
import com.openklaster.api.model.summary.EnvironmentalBenefits;
import com.openklaster.api.model.summary.Measurement;
import com.openklaster.api.model.summary.SummaryResponse;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SummaryCreator {

    public static SummaryResponse createSummary(AsyncResult<Message<JsonArray>> response, NestedConfigAccessor config) {
        Map<Unit, List<Measurement>> measurements = groupMeasurementsFromJsonArray(response.result().body());
        Measurement latestEnergyMeasurement = findLastMeasurement(measurements.get(Unit.kWh));
        Measurement latestPowerMeasurement = findLastMeasurement(measurements.get(Unit.kW));
        Map<String, Double> powerMeasurements = convertMeasurementArraysIntoMap(measurements.get(Unit.kW));
        Double energyProducedToday = countEnergyProducedToday(measurements.get(Unit.kWh));
        EnvironmentalBenefits environmentalBenefits = EnvironmentalBenefits.builder()
                .co2reduced(calculateEnvironmentalBenefit(SummaryProperties.CO2REDUCED, latestEnergyMeasurement.getValue(), config))
                .treessaved(calculateEnvironmentalBenefit(SummaryProperties.TREES_SAVED, latestEnergyMeasurement.getValue(), config))
                .build();
        ;

        return SummaryResponse.builder()
                .totalEnergy(latestEnergyMeasurement.getValue())
                .currentPower(latestPowerMeasurement.getValue())
                .power(powerMeasurements)
                .energyProducedToday(energyProducedToday)
                .environmentalBenefits(environmentalBenefits)
                .build();
    }


    private static Map<String, Double> convertMeasurementArraysIntoMap(List<Measurement> measurements) {
        return measurements.stream()
                .filter(measurement -> isItToday(measurement.getTimestamp()))
                .collect(Collectors.toMap(a -> parseTimeFromTimeStamp(a.getTimestamp()),
                        Measurement::getValue,
                        (prev, next) -> next, HashMap::new));
    }

    private static Measurement findLastMeasurement(List<Measurement> energyMeasurements) {
        return Optional.ofNullable(energyMeasurements)
                .orElse(Collections.singletonList(new Measurement()))
                .stream()
                .max(Comparator.comparing(Measurement::getTimestamp))
                .get();
    }

    private static Double countEnergyProducedToday(List<Measurement> energyMeasurements) {
        Map<Object, List<Measurement>> booleanMeasurementMap = energyMeasurements.stream()
                .collect(Collectors.groupingBy(measurement -> isItToday(measurement.getTimestamp())));
        Double currentEnergy = findLastMeasurement(booleanMeasurementMap.get(true)).getValue();
        Double yesterdaysLastEnergyMeasurement = findLastMeasurement(booleanMeasurementMap.get(false)).getValue();
        return currentEnergy - yesterdaysLastEnergyMeasurement;
    }

    private static Map<Unit, List<Measurement>> groupMeasurementsFromJsonArray(JsonArray jsonArray) {
        return jsonArray.stream()
                .map(result -> JsonObject.mapFrom(result).mapTo(Measurement.class))
                .collect(Collectors.groupingBy(Measurement::getUnit));
    }

    private static int calculateEnvironmentalBenefit(String path, double energy, NestedConfigAccessor config) {
        return (int) (((double) config.getInteger(path) / 100) * energy);
    }

    private static String parseTimeFromTimeStamp(Date timestamp) {
        return new SimpleDateFormat(SummaryProperties.TIME_FORMAT).format(timestamp);
    }

    private static boolean isItToday(Date timestamp) {
        return DateUtils.isSameDay(timestamp, new Date());
    }
}
