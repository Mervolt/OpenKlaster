package com.openklaster.core.vertx.service.users;

import com.openklaster.core.vertx.authentication.AuthenticationClient;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class DeleteTokenManager implements UserManager {
    private static final String methodName = "generateToken";
    private static final String successMessage = "Token deleted - %s";
    private static final String failureMessage = "Could not delete token - %s";
    private static final String tokenKey = "userToken";
    private static final Logger logger = LoggerFactory.getLogger(DeleteTokenManager.class);
    private final AuthenticatedManager authenticatedManager;
    private final AuthenticationClient authenticationClient;

    public DeleteTokenManager(AuthenticationClient authenticationClient) {
        this.authenticationClient = authenticationClient;
        this.authenticatedManager = new AuthenticatedManager(logger, authenticationClient, successMessage, failureMessage);
    }

    @Override
    public String getMethodsName() {
        return methodName;
    }

    @Override
    public void handleMessage(Message<JsonObject> message) {
        authenticatedManager.handleMessage(message,
                deleteToken(message.body().getString(usernameKey), message.body().getString(tokenKey)));
    }

    private Future<JsonObject> deleteToken(String username, String token) {
        return Future.succeededFuture();
    }
}
