package com.openklaster.core.vertx.service.users;

import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.model.User;
import com.openklaster.common.model.UserToken;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class GenerateTokenManager implements UserManager {
    private static final String methodName = "generateToken";
    private static final String successMessage = "Token generated for user - %s";
    private static final String failureMessage = "Could not generate token - %s";
    private static final Logger logger = LoggerFactory.getLogger(GenerateTokenManager.class);
    private final AuthenticatedManager authenticatedManager;
    private final AuthenticationClient authenticationClient;
    private final TokenHandler tokenHandler;

    public GenerateTokenManager(AuthenticationClient authenticationClient, TokenHandler tokenHandler) {
        this.authenticationClient = authenticationClient;
        this.tokenHandler = tokenHandler;
        this.authenticatedManager = new AuthenticatedManager(logger, authenticationClient, successMessage, failureMessage);
    }

    @Override
    public String getMethodsName() {
        return methodName;
    }

    @Override
    public void handleMessage(Message<JsonObject> message) {
        authenticatedManager.handleMessage(message, this::generateAndStoreToken);
    }

    private Future<JsonObject> generateAndStoreToken(User user) {
        UserToken token = tokenHandler.generateUserToken();
        return storeToken(user, token);
    }

    private Future<JsonObject> storeToken(User user, UserToken token) {
        return Future.succeededFuture();
    }
}
