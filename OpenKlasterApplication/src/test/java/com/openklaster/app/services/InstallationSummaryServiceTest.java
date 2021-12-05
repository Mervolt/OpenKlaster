package com.openklaster.app.services;

import com.openklaster.app.model.entities.measurement.MeasurementUnit;
import com.openklaster.app.model.entities.measurement.SourceMeasurementEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InstallationSummaryServiceTest {

    @Test
    void getMeasurementByUnit() {

        InstallationSummaryService service = new InstallationSummaryService(null);

        List<SourceMeasurementEntity> result = service.getMeasurementByUnit(Arrays.asList(
                createSourceMeas(0),createSourceMeas(0),createSourceMeas(0),createSourceMeas(0),
                createSourceMeas(0), createSourceMeas(0), createSourceMeas(0), createSourceMeas(2),
                createSourceMeas(3),createSourceMeas(0),createSourceMeas(4),createSourceMeas(0)
        ), MeasurementUnit.kW);

        List<Double> resultMapped = result.stream().map(SourceMeasurementEntity::getValue).collect(Collectors.toList());

        // Having: 0, 0, 0, 0, 0, 0, 0, 2, 3, 0, 4, 0
        // Returned result is: 0, 0, 0, 0, 0, 2, 3, 4
        // Because we skip 0 if there were less than 2 zeros in sequence
        assertEquals(0, (double) resultMapped.get(0));
        assertEquals(0, (double) resultMapped.get(1));
        assertEquals(0, (double) resultMapped.get(2));
        assertEquals(0, (double) resultMapped.get(3));
        assertEquals(0, (double) resultMapped.get(4));
        assertEquals(2, (double) resultMapped.get(5));
        assertEquals(3, (double) resultMapped.get(6));
        assertEquals(4, (double) resultMapped.get(7));

        assertEquals(8, resultMapped.size());

    }

    private SourceMeasurementEntity createSourceMeas(double value) {
        return SourceMeasurementEntity.builder()
                .installationId("id")
                .timestamp(new Date())
                .unit(MeasurementUnit.kW)
                .value(value)
                .build();
    }
}