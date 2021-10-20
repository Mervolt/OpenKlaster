package com.openklaster.app.controllers;

import com.openklaster.app.model.requests.MeasurementRequest;
import com.openklaster.app.model.responses.MeasurementResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController()
public class MeasurementsController {

    @PostMapping(path = "powerConsumption")
    public MeasurementResponse addPowerConsumption(@RequestBody MeasurementRequest measurementRequest) {
        throw new UnsupportedOperationException();
    }

    @PostMapping(path = "powerProduction")
    public MeasurementResponse addPowerProduction(@RequestBody MeasurementRequest measurementRequest) {
        throw new UnsupportedOperationException();
    }

    @PostMapping(path = "energyConsumed")
    public MeasurementResponse addEnergyConsumed(@RequestBody MeasurementRequest measurementRequest) {
        throw new UnsupportedOperationException();
    }

    @PostMapping(path = "energyProduced")
    public MeasurementResponse addEnergyProduced(@RequestBody MeasurementRequest measurementRequest) {
        throw new UnsupportedOperationException();
    }


    @GetMapping(path = "powerConsumption")
    public List<MeasurementResponse> getPowerConsumption(@RequestParam String installationId, @RequestParam Date startDate,
                                                         @RequestParam Date endDate) {
        throw new UnsupportedOperationException();
    }

    @GetMapping(path = "powerProduction")
    public List<MeasurementResponse> getPowerProduction(@RequestParam String installationId, @RequestParam Date startDate,
                                                         @RequestParam Date endDate) {
        throw new UnsupportedOperationException();
    }

    @GetMapping(path = "energyConsumed")
    public List<MeasurementResponse> getEnergyConsumed(@RequestParam String installationId, @RequestParam Date startDate,
                                                         @RequestParam Date endDate) {
        throw new UnsupportedOperationException();
    }

    @GetMapping(path = "energyProduced")
    public List<MeasurementResponse> getEnergyProduced(@RequestParam String installationId, @RequestParam Date startDate,
                                                         @RequestParam Date endDate) {
        throw new UnsupportedOperationException();
    }
}
