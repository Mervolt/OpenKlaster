package com.openklaster.app.services;


import com.openklaster.app.model.entities.installation.InstallationEntity;
import com.openklaster.app.model.entities.installation.InstallationType;
import com.openklaster.app.model.requests.InstallationRequest;
import com.openklaster.app.model.requests.InstallationUpdateRequest;
import com.openklaster.app.persistence.mongo.MongoSequenceGenerator;
import com.openklaster.app.persistence.mongo.installation.InstallationRepository;
import com.openklaster.app.persistence.mongo.installation.SafeInstallationAccessor;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InstallationService {
    InstallationRepository installationRepository;
    SafeInstallationAccessor safeInstallationAccessor;
    MongoSequenceGenerator mongoSequenceGenerator;

    private static final Logger logger = LoggerFactory.getLogger(InstallationService.class);

    public List<InstallationEntity> getAllInstallations() {
        return safeInstallationAccessor.getCurrentUserInstallations();
    }

    public InstallationEntity addNewInstallation(InstallationRequest installationRequest) {
        InstallationType type = Optional.ofNullable(installationRequest.getInstallationType())
                .orElse(InstallationType.Solar);
        InstallationEntity newInstallation = InstallationEntity.builder()
                .username(installationRequest.getUsername())
                .installationType(type)
                .longitude(installationRequest.getLongitude())
                .latitude(installationRequest.getLatitude())
                .description(installationRequest.getDescription())
                .load(installationRequest.getLoad())
                .inverter(installationRequest.getInverter())
                .source(installationRequest.getSource())
                .id(generateId())
                .build();
        installationRepository.insert(newInstallation);
        logger.debug(String.format("Installation with id '%s' for user '%s' added.",
                newInstallation.getId(), newInstallation.getUsername()));
        return newInstallation;
    }

    private String generateId() {
        return String.format("installation:%d", mongoSequenceGenerator.generateSequence(InstallationEntity.SEQUENCE_NAME));
    }

    public InstallationEntity updateInstallation(InstallationUpdateRequest installationRequest) {
        InstallationType type = Optional.ofNullable(installationRequest.getInstallationType())
                .orElse(InstallationType.Solar);
        InstallationEntity entity = getInstallation(installationRequest.getInstallationId());
        InstallationEntity newInstallation = InstallationEntity.builder()
                .username(installationRequest.getUsername())
                .installationType(type)
                .longitude(installationRequest.getLongitude())
                .latitude(installationRequest.getLatitude())
                .description(installationRequest.getDescription())
                .load(installationRequest.getLoad())
                .inverter(installationRequest.getInverter())
                .source(installationRequest.getSource())
                .id(entity.getId())
                .build();
        installationRepository.save(newInstallation);
        logger.debug(String.format("Installation with id '%s' for user '%s' updated.",
                newInstallation.getId(), newInstallation.getUsername()));
        return newInstallation;
    }

    public void removeInstallation(String installationId) {
        getInstallation(installationId);
        installationRepository.deleteById(installationId);
        logger.debug(String.format("Installation with id '%s' removed", installationId));
    }

    public InstallationEntity getInstallation(String installationId) {
        return safeInstallationAccessor.getCurrentUserInstallation(installationId);
    }
}
