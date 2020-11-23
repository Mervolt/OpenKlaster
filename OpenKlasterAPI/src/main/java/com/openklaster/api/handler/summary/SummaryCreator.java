package com.openklaster.api.handler.summary;

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
import java.util.*;
import java.util.stream.Collectors;

public class SummaryCreator {

    public SummaryResponse createSummary(AsyncResult<Message<JsonArray>> response, EnvironmentalBenefits benefitsCoefficients) {
        Map<Unit, List<Measurement>> measurements = groupMeasurementsFromJsonArray(response.result().body());
        Measurement latestEnergyMeasurement = findLastMeasurement(measurements.get(Unit.kWh));
        Measurement latestPowerMeasurement = findLastMeasurement(measurements.get(Unit.kW));
        Map<Date, BigDecimal> powerMeasurements = convertMeasurementArraysIntoMap(measurements.get(Unit.kW));
        BigDecimal energyProducedToday = countEnergyProducedToday(measurements.get(Unit.kWh));
        EnvironmentalBenefits environmentalBenefits = EnvironmentalBenefits.builder()
                .co2Reduced(calculateEnvironmentalBenefit(latestEnergyMeasurement.getValue(), benefitsCoefficients.getCo2Reduced()))
                .treesSaved(calculateEnvironmentalBenefit(latestEnergyMeasurement.getValue(), benefitsCoefficients.getTreesSaved()))
                .build();
        ;

        return SummaryResponse.builder()
                .totalEnergy(BigDecimal.valueOf(latestEnergyMeasurement.getValue()))
                .currentPower(BigDecimal.valueOf(latestPowerMeasurement.getValue()))
                .power(powerMeasurements)
                .energyProducedToday(energyProducedToday)
                .environmentalBenefits(environmentalBenefits)
                .build();
    }


    private Map<Date, BigDecimal> convertMeasurementArraysIntoMap(List<Measurement> measurementsList) {
        return Optional.ofNullable(measurementsList).map(measurements ->
                measurements.stream().filter(measurement -> isItToday(measurement.getTimestamp()))
                        .collect(Collectors.toMap(Measurement::getTimestamp,
                                value -> BigDecimal.valueOf(value.getValue()),
                                (prev, next) -> next, HashMap::new)))
                .orElse(new HashMap<>());
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

    private int calculateEnvironmentalBenefit(double energy, int coefficient) {
        return (int) (((double) coefficient / 100) * energy);
    }

    private boolean isItToday(Date timestamp) {
        return DateUtils.isSameDay(timestamp, new Date());
    }
}
