package com.openklaster.app.measurements;

import com.openklaster.app.model.entities.measurement.MeasurementUnit;
import com.openklaster.app.model.entities.measurement.SourceMeasurementEntity;
import com.openklaster.app.model.requests.MeasurementRequest;
import com.openklaster.app.persistence.cassandra.dao.SourceMeasurementRepository;
import com.openklaster.app.services.MeasurementsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@EnableAsync
@EnableScheduling
@Component
@ConditionalOnProperty("openklaster.power-energy.enabled")
@AllArgsConstructor
public class PowerToEnergyService {

    @Value("${openklaster.power-energy.installation-id}")
    private final String installationId;

    private final MeasurementsService measurementsService;

    private final SourceMeasurementRepository sourceMeasurementRepository;

    private static final Logger logger = LoggerFactory.getLogger(PowerToEnergyService.class);

    @Async
    @Scheduled(cron = "0 0/60 * * * *")
    public void calculateEnergy() {
        Instant startTime = Instant.now().minus(60, ChronoUnit.MINUTES);
        List<SourceMeasurementEntity> measurements = sourceMeasurementRepository.findByTimestampBetweenAndInstallationId(Date.from(startTime), new Date(),
                installationId)
                .stream()
                .filter(measurement -> measurement.getValue() > 0)
                .collect(Collectors.toList());

        double mediumPower = measurements
                .stream()
                .mapToDouble(SourceMeasurementEntity::getValue)
                .average()
                .orElse(0);

        String measurementsString = measurements
                .stream()
                .map(entity -> String.valueOf(entity.getValue()))
                .collect(Collectors.joining(", "));

        Optional<Double> latestEnergyMeasurement = sourceMeasurementRepository.
                findByTimestampBeforeAndUnitAndInstallationId(new Date(), MeasurementUnit.kWh, installationId)
                .stream()
                .max(Comparator.comparing(SourceMeasurementEntity::getTimestamp))
                .map(SourceMeasurementEntity::getValue);

        MeasurementRequest req = MeasurementRequest.builder()
                .installationId(installationId)
                .timestamp(new Date())
                .value(mediumPower + latestEnergyMeasurement.orElse(0.0))
                .build();


        logger.debug(String.format("Calculated %f energy from measurements:\n%s", mediumPower, measurementsString));
        logger.debug(String.format("New energy measurement is %f, previous was %f", req.getValue(), latestEnergyMeasurement.orElse(0.)));

        measurementsService.addSourceMeasurement(req, MeasurementUnit.kWh);
    }
}
