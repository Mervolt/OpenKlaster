package com.openklaster.app.controllers;

import com.openklaster.app.model.entities.installation.InstallationEntity;
import com.openklaster.app.model.requests.InstallationRequest;
import com.openklaster.app.model.requests.InstallationUpdateRequest;
import com.openklaster.app.model.responses.InstallationResponse;
import com.openklaster.app.services.InstallationService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "installations", description = "Installations management")
@RestController
@AllArgsConstructor
@RequestMapping("installations")
public class InstallationController {
    InstallationService installationService;

    @GetMapping(path = "all")
    public List<InstallationResponse> getAllInstallations() {
        return installationService.getAllInstallations()
                .stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping()
    public InstallationResponse getInstallation(@RequestParam String installationId) {
        return fromEntity(installationService.getInstallation(installationId));
    }

    @PostMapping()
    public InstallationResponse createInstallation(@RequestBody InstallationRequest installationRequest) {
        return fromEntity(installationService.addNewInstallation(installationRequest));
    }

    @PutMapping()
    public InstallationResponse updateInstallation(@RequestBody @Valid InstallationUpdateRequest installationUpdateRequest) {
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
