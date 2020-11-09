package com.openklaster.cassandra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class HandlerContainer {
    private final LoadMeasurementHandler loadMeasurementHandler;
    private final SourceMeasurementHandler sourceMeasurementHandler;
    private final EnergyPredictionsHandler energyPredictionsHandler;
    private final WeatherConditionsHandler weatherConditionsHandler;

    @Lazy
    @Autowired
    public HandlerContainer(LoadMeasurementHandler loadMeasurementHandler, SourceMeasurementHandler sourceMeasurementHandler,
                            EnergyPredictionsHandler energyPredictionsHandler, WeatherConditionsHandler weatherConditionsHandler) {
        this.loadMeasurementHandler = loadMeasurementHandler;
        this.sourceMeasurementHandler = sourceMeasurementHandler;
        this.energyPredictionsHandler = energyPredictionsHandler;
        this.weatherConditionsHandler = weatherConditionsHandler;
    }

    public List<CassandraHandler<?>> retrieveHandlers() {
        return Arrays.asList(loadMeasurementHandler, sourceMeasurementHandler, energyPredictionsHandler, weatherConditionsHandler);
    }
}
