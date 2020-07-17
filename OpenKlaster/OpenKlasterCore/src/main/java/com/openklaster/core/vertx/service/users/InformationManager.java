package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.messages.repository.Repository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

public class InformationManager extends AuthenticatedManager {
    private static final String methodName = "info";
    private static final String failureMessage = "Could not get user info - %s";
    private static final String successMessage = "User info retrieved - %s";

    public InformationManager(AuthenticationClient authenticationClient, Repository<User> userRepository) {
        super(LoggerFactory.getLogger(InformationManager.class), authenticationClient, userRepository);
    }

    @Override
    public String getMethodsName() {
        return methodName;
    }

    @Override
    protected Future<JsonObject> processUser(User user) {
        return Future.succeededFuture(new JsonObject()
                .put(usernameKey, user.getUsername())
                .put(emailKey, user.getEmail())
                .put(userTokensKey, user.getUserTokens()));
    }

    @Override
    protected String getSuccessMessage(JsonObject result) {
        return String.format(successMessage, result);
    }

    @Override
    protected String getFailureMessage(String reason) {
        return String.format(failureMessage, reason);
    }
}
