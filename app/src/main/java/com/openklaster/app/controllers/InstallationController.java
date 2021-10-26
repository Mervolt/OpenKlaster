package com.openklaster.app.controllers;

import com.openklaster.app.model.entities.installation.InstallationEntity;
import com.openklaster.app.model.requests.InstallationRequest;
import com.openklaster.app.model.requests.InstallationUpdateRequest;
import com.openklaster.app.model.responses.InstallationResponse;
import com.openklaster.app.services.InstallationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("installations")
public class InstallationController {

    @Autowired
    InstallationService installationService;

    @GetMapping(path = "all")
    public List<InstallationResponse> getAllInstallations(@RequestParam String username) {
        return installationService.getAllInstallations(username)
                .stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping()
    public InstallationResponse getInstallation(@RequestParam String installationId) {
        return fromEntity(installationService.getInstallationOrThrow404(installationId));
    }

    @PostMapping(path = "/add")
    public InstallationResponse createInstallation(@RequestBody InstallationRequest installationRequest) {
        return fromEntity(installationService.addNewInstallation(installationRequest));
    }

    @PostMapping(path = "/update")
    public InstallationResponse updateInstallation(@RequestBody InstallationUpdateRequest installationUpdateRequest) {
        return fromEntity(installationService.updateInstallation(installationUpdateRequest));
    }

    @DeleteMapping()
    public void removeInstallation(@RequestParam String installationId) {
        installationService.removeInstallation(installationId);
    }

    private InstallationResponse fromEntity(InstallationEntity entity) {
        return InstallationResponse.builder()
                .installationId(entity.getId())
                .username(entity.getUsername())
                .installationType(entity.getInstallationType())
                .longitude(entity.getLongitude())
                .latitude(entity.getLatitude())
                .description(entity.getDescription())
                .load(entity.getLoad())
                .inverter(entity.getInverter())
                .source(entity.getSource())
                .build();
    }
}
