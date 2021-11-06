package com.openklaster.app.controllers;

import com.openklaster.app.model.entities.measurement.MeasurementUnit;
import com.openklaster.app.model.requests.MeasurementRequest;
import com.openklaster.app.model.responses.MeasurementResponse;
import com.openklaster.app.model.responses.summary.SummaryResponse;
import com.openklaster.app.services.MeasurementsService;
import com.openklaster.app.services.InstallationSummaryService;
import com.openklaster.app.validation.installation.SafeInstallation;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Api(tags = "measurements", description = "Energy and power measurements")
@RestController
@AllArgsConstructor
@Validated
public class MeasurementsController {
    private final MeasurementsService measurementsService;
    private final InstallationSummaryService installationSummaryService;

    @PostMapping(path = "powerConsumption")
    public MeasurementResponse addPowerConsumption(@RequestBody @Valid MeasurementRequest measurementRequest) {
        String installationId = measurementRequest.getInstallationId();
        Date date = Optional.ofNullable(measurementRequest.getTimestamp()).orElse(new Date());
        double value = measurementRequest.getValue();
        return measurementsService.addLoadMeasurement(installationId, date, value, MeasurementUnit.kW);
    }

    @PostMapping(path = "powerProduction")
    public MeasurementResponse addPowerProduction(@RequestBody @Valid MeasurementRequest measurementRequest) {
        String installationId = measurementRequest.getInstallationId();
        Date date = Optional.ofNullable(measurementRequest.getTimestamp()).orElse(new Date());
        double value = measurementRequest.getValue();
        return measurementsService.addSourceMeasurement(installationId, date, value, MeasurementUnit.kW);
    }

    @PostMapping(path = "energyConsumed")
    public MeasurementResponse addEnergyConsumed(@RequestBody @Valid MeasurementRequest measurementRequest) {
        String installationId = measurementRequest.getInstallationId();
        Date date = Optional.ofNullable(measurementRequest.getTimestamp()).orElse(new Date());
        double value = measurementRequest.getValue();
        return measurementsService.addLoadMeasurement(installationId, date, value, MeasurementUnit.kW);
    }

    @PostMapping(path = "energyProduced")
    public MeasurementResponse addEnergyProduced(@RequestBody @Valid MeasurementRequest measurementRequest) {
        String installationId = measurementRequest.getInstallationId();
        Date date = Optional.ofNullable(measurementRequest.getTimestamp()).orElse(new Date());
        double value = measurementRequest.getValue();
        return measurementsService.addSourceMeasurement(installationId, date, value, MeasurementUnit.kW);
    }


    @GetMapping(path = "powerConsumption")
    public List<MeasurementResponse> getPowerConsumption(@RequestParam @SafeInstallation String installationId,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        return measurementsService.getLoadMeasurements(installationId, startDate, endDate, MeasurementUnit.kW);
    }

    @GetMapping(path = "powerProduction")
    public List<MeasurementResponse> getPowerProduction(@RequestParam @SafeInstallation String installationId,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        return measurementsService.getSourceMeasurements(installationId, startDate, endDate, MeasurementUnit.kW);
    }

    @GetMapping(path = "energyConsumed")
    public List<MeasurementResponse> getEnergyConsumed(@RequestParam @SafeInstallation String installationId,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        return measurementsService.getLoadMeasurements(installationId, startDate, endDate, MeasurementUnit.kWh);
    }

    @GetMapping(path = "energyProduced")
    public List<MeasurementResponse> getEnergyProduced(@RequestParam @SafeInstallation String installationId,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        return measurementsService.getSourceMeasurements(installationId, startDate, endDate, MeasurementUnit.kWh);
    }

    @GetMapping(path = "summary")
    public SummaryResponse getSummary(@RequestParam @SafeInstallation String installationId) {
        return installationSummaryService.createSummary(installationId);
    }
}
