package com.openklaster.app.measurements;

import com.openklaster.app.measurements.growatt.GrowattCredentials;
import com.openklaster.app.measurements.growatt.GrowattInverter;
import com.openklaster.app.model.entities.installation.InstallationEntity;
import com.openklaster.app.model.entities.measurement.MeasurementUnit;
import com.openklaster.app.persistence.mongo.installation.InstallationRepository;
import com.openklaster.app.services.MeasurementsService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@EnableAsync
@EnableScheduling
@Component
@Profile("!EXTRACTOR_OFF")
@Log
@AllArgsConstructor
public class MeasurementsExtractor {
    private final MeasurementsService measurementsService;
    private final InstallationRepository installationRepository;

    private final GrowattInverter growattInverter;

    // Todo refactor ;)
    @Async
    @Scheduled(cron = "0 0/30 * * * *")
    public void scheduleMeasurementsExtractingTaskAsync() {
        CopyOnWriteArrayList<InstallationEntity> allInstallations = new CopyOnWriteArrayList<>(installationRepository.findAll());
        for (InstallationEntity installation: allInstallations) {
            String id = installation.getId();
            String manufacturer = installation.getInverter().getManufacturer();
            Map<String, String> credentials = installation.getInverter().getCredentials();

            Measurement measurement = null;

            if (manufacturer.equals("Growatt")) {
                GrowattCredentials growattCredensials = getGrowattCredensials(credentials);
                if (Objects.nonNull(growattCredensials.getUsername()) &&  Objects.nonNull(growattCredensials.getPassword())) {
                    measurement = growattInverter.retrieveMeasurement(growattCredensials);
                }
            }


            if (Objects.nonNull(measurement)) {
                log.info(id  + "(" + manufacturer + "): " + measurement.getPower() + " kWh, " + measurement.getEnergy() + " kW");
                measurementsService.addSourceMeasurementEntity(measurement.getEnergy(), id, new Date(), MeasurementUnit.kWh);
                measurementsService.addSourceMeasurementEntity(measurement.getPower(), id, new Date(), MeasurementUnit.kW);
            }

        }
    }

    private GrowattCredentials getGrowattCredensials(Map<String, String> inverterEntity) {
        String username = Optional.ofNullable(inverterEntity.get("Username")).orElse(null);
        String password =  Optional.ofNullable(inverterEntity.get("Password")).orElse(null);
        return new GrowattCredentials(username, password);
    }
}