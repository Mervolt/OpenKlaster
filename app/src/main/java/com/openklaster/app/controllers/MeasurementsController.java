package com.openklaster.app.controllers;

import com.openklaster.app.model.entities.measurement.MeasurementUnit;
import com.openklaster.app.model.requests.MeasurementRequest;
import com.openklaster.app.model.responses.MeasurementResponse;
import com.openklaster.app.services.MeasurementService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "measurements", description = "Energy and power measurements")
@RestController()
public class MeasurementsController {

    @Autowired
    MeasurementService measurementService;

    @PostMapping(path = "powerConsumption")
    public MeasurementResponse addPowerConsumption(@RequestBody MeasurementRequest measurementRequest) {
        return measurementService.addLoadMeasurement(measurementRequest, MeasurementUnit.kW);
    }

    @PostMapping(path = "powerProduction")
    public MeasurementResponse addPowerProduction(@RequestBody MeasurementRequest measurementRequest) {
        return measurementService.addSourceMeasurement(measurementRequest, MeasurementUnit.kW);
    }

    @PostMapping(path = "energyConsumed")
    public MeasurementResponse addEnergyConsumed(@RequestBody MeasurementRequest measurementRequest) {
        return measurementService.addLoadMeasurement(measurementRequest, MeasurementUnit.kWh);
    }

    @PostMapping(path = "energyProduced")
    public MeasurementResponse addEnergyProduced(@RequestBody MeasurementRequest measurementRequest) {
        return measurementService.addSourceMeasurement(measurementRequest, MeasurementUnit.kWh);
    }


    @GetMapping(path = "powerConsumption")
    public List<MeasurementResponse> getPowerConsumption(@RequestParam String installationId,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        return measurementService.getLoadMeasurements(installationId, startDate, endDate, MeasurementUnit.kW);
    }

    @GetMapping(path = "powerProduction")
    public List<MeasurementResponse> getPowerProduction(@RequestParam String installationId,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        return measurementService.getSourceMeasurements(installationId, startDate, endDate, MeasurementUnit.kW);
    }

    @GetMapping(path = "energyConsumed")
    public List<MeasurementResponse> getEnergyConsumed(@RequestParam String installationId,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        return measurementService.getLoadMeasurements(installationId, startDate, endDate, MeasurementUnit.kWh);
    }

    @GetMapping(path = "energyProduced")
    public List<MeasurementResponse> getEnergyProduced(@RequestParam String installationId,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate) {
        return measurementService.getSourceMeasurements(installationId, startDate, endDate, MeasurementUnit.kWh);
    }

    @GetMapping(path = "summary")
    public List<MeasurementResponse> getSummary(@RequestParam String installationId) {
        throw new UnsupportedOperationException();
    }
}
