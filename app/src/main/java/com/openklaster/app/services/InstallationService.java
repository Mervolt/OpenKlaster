package com.openklaster.app.services;


import com.openklaster.app.model.entities.installation.InstallationEntity;
import com.openklaster.app.model.requests.InstallationRequest;
import com.openklaster.app.model.requests.InstallationUpdateRequest;
import com.openklaster.app.persistence.mongo.MongoSequenceGenerator;
import com.openklaster.app.persistence.mongo.dao.InstallationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class InstallationService {

    @Autowired
    InstallationRepository installationRepository;

    @Autowired
    MongoSequenceGenerator mongoSequenceGenerator;

    public List<InstallationEntity> getAllInstallations(String username) {
        return installationRepository.findAllByUsername(username);
    }

    public InstallationEntity addNewInstallation(InstallationRequest installationRequest) {
        InstallationEntity newInstallation = InstallationEntity.builder()
                .username(installationRequest.getUsername())
                .installationType(installationRequest.getInstallationType())
                .longitude(installationRequest.getLongitude())
                .latitude(installationRequest.getLatitude())
                .description(installationRequest.getDescription())
                .load(installationRequest.getLoad())
                .inverter(installationRequest.getInverter())
                .source(installationRequest.getSource())
                .id(generateId())
                .build();
        installationRepository.insert(newInstallation);
        return newInstallation;
    }

    private String generateId() {
        return String.format("installation:%d", mongoSequenceGenerator.generateSequence(InstallationEntity.SEQUENCE_NAME));
    }

    public InstallationEntity updateInstallation(InstallationUpdateRequest installationRequest) {
        InstallationEntity entity = getInstallationOrThrow404(installationRequest.getInstallationId());
        InstallationEntity newInstallation = InstallationEntity.builder()
                .username(installationRequest.getUsername())
                .installationType(installationRequest.getInstallationType())
                .longitude(installationRequest.getLongitude())
                .latitude(installationRequest.getLatitude())
                .description(installationRequest.getDescription())
                .load(installationRequest.getLoad())
                .inverter(installationRequest.getInverter())
                .source(installationRequest.getSource())
                .id(entity.getId())
                .build();
        installationRepository.save(newInstallation);
        return newInstallation;
    }

    public void removeInstallation(String installationId) {
        getInstallationOrThrow404(installationId);
        installationRepository.deleteById(installationId);
    }

    public InstallationEntity getInstallationOrThrow404(String installationId) {
        return installationRepository.findById(installationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
