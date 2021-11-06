package com.openklaster.app.persistence.mongo.installation;

import com.openklaster.app.model.entities.installation.InstallationEntity;
import com.openklaster.app.model.entities.user.UserEntity;
import com.openklaster.app.persistence.mongo.user.UserContextAccessor;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class SafeInstallationAccessor {
    private final InstallationRepository installationRepository;
    private final UserContextAccessor userContextAccessor;

    public List<InstallationEntity> getCurrentUserInstallations() {
        UserEntity currentUser = userContextAccessor.getCurrentUser();
        return installationRepository.findAllByUsername(currentUser.getUsername());
    }

    public InstallationEntity getCurrentUserInstallation(String installationId) {
        UserEntity currentUser = userContextAccessor.getCurrentUser();
        return installationRepository.findByIdAndUsername(installationId, currentUser.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
