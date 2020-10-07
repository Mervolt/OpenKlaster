package com.openklaster.core.vertx.service;

import com.openklaster.common.model.Installation;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.messages.repository.CrudRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class BasicUserRetriever implements UserRetriever {

    private static final String usernameKey = "username";
    private static final String installationIdKey = "installationId";
    private static final String noKey = "No username or installationId property found for query %s.";

    public BasicUserRetriever(CrudRepository<User> userRepository, CrudRepository<Installation> installationRepository) {
        this.userRepository = userRepository;
        this.installationRepository = installationRepository;
    }

    private final CrudRepository<User> userRepository;
    private final CrudRepository<Installation> installationRepository;

    @Override
    public Future<User> retrieveUser(JsonObject entity) {
        if (entity.containsKey(usernameKey)) {
            return retrieveByUsername(entity.getString(usernameKey));
        } else if (entity.containsKey(installationIdKey)) {
            return retrieveByInstallationId(entity.getString(installationIdKey));
        } else return Future.failedFuture(String.format(noKey, entity));
    }

    private Future<User> retrieveByInstallationId(String installationId) {
        return installationRepository.get(installationId)
                .compose(installation -> retrieveByUsername(installation.getUsername()));
    }

    private Future<User> retrieveByUsername(String username) {
        return userRepository.get(username);
    }
}
