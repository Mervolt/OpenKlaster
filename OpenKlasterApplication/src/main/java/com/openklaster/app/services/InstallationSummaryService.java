package com.openklaster.app.services;

import com.openklaster.app.model.entities.measurement.MeasurementUnit;
import com.openklaster.app.model.entities.measurement.SourceMeasurementEntity;
import com.openklaster.app.model.responses.summary.EnvironmentalBenefits;
import com.openklaster.app.model.responses.summary.SummaryResponse;
import com.openklaster.app.persistence.cassandra.dao.SourceMeasurementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InstallationSummaryService {
    private final SourceMeasurementRepository sourceMeasurementRepository;

    private static final double CO2_REDUCED = 0.40;
    private static final double TREES_SAVED = 0.06;

    public SummaryResponse createSummary(String installationId) {
        List<SourceMeasurementEntity> sourceMeasurements = getSourceMeasurements(installationId);

        List<SourceMeasurementEntity> powerMeasurements = getMeasurementByUnit(sourceMeasurements, MeasurementUnit.kW);
        List<SourceMeasurementEntity> energyMeasurements = getMeasurementByUnit(sourceMeasurements, MeasurementUnit.kWh);

        double earliestEnergyMeasurement = energyMeasurements.stream().min(Comparator.comparing(SourceMeasurementEntity::getTimestamp))
                .map(SourceMeasurementEntity::getValue).orElse(0.0);
        double latestEnergyMeasurement = energyMeasurements.stream().max(Comparator.comparing(SourceMeasurementEntity::getTimestamp))
                .map(SourceMeasurementEntity::getValue).orElse(0.0);
        double latestPowerMeasurement = powerMeasurements.stream().max(Comparator.comparing(SourceMeasurementEntity::getTimestamp))
                .map(SourceMeasurementEntity::getValue).orElse(0.0);

        Map<Date, BigDecimal> measurementsMap = convertMeasurementArrayIntoMap(powerMeasurements);

        return SummaryResponse.builder()
                .totalEnergy(BigDecimal.valueOf(latestEnergyMeasurement))
                .currentPower(BigDecimal.valueOf(latestPowerMeasurement))
                .power(measurementsMap)
                .energyProducedToday(BigDecimal.valueOf(latestEnergyMeasurement - earliestEnergyMeasurement))
                .environmentalBenefits(calculateEnvironmentalBenefit(latestEnergyMeasurement))
                .build();
    }


    private List<SourceMeasurementEntity> getSourceMeasurements(String installationId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().atStartOfDay();

        Date startTime = Date.from(midnight.atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        return sourceMeasurementRepository.findByTimestampBetweenAndInstallationId(startTime, endTime, installationId)
                .stream()
                .sorted(Comparator.comparing(SourceMeasurementEntity::getTimestamp))
                .collect(Collectors.toList());
    }

    private List<SourceMeasurementEntity> getMeasurementByUnit(List<SourceMeasurementEntity> sourceMeasurements, MeasurementUnit unit) {
        return sourceMeasurements.stream()
                .filter(measurement -> measurement.getUnit().equals(unit))
                .collect(Collectors.toList());

    }

    private Map<Date, BigDecimal> convertMeasurementArrayIntoMap(List<SourceMeasurementEntity> measurementsList) {
        return Optional.ofNullable(measurementsList).map(measurements ->
                measurements.stream()
                        .collect(Collectors.toMap(SourceMeasurementEntity::getTimestamp,
                                value -> BigDecimal.valueOf(value.getValue()),
                                (prev, next) -> next, HashMap::new)))
                .orElse(new HashMap<>());
    }

    private EnvironmentalBenefits calculateEnvironmentalBenefit(double energy) {
        return EnvironmentalBenefits.builder()
                .co2Reduced((int) (energy * CO2_REDUCED))
                .treesSaved((int) (energy * TREES_SAVED))
                .build();
    }
}
