package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class DeleteAllTokensManager implements UserManager {
    private static final String methodName = "deleteAllTokens";
    private static final String successMessage = "Tokens deleted - %s";
    private static final String failureMessage = "Could not delete all tokens - %s";
    private static final Logger logger = LoggerFactory.getLogger(DeleteAllTokensManager.class);
    private final AuthenticatedManager authenticatedManager;
    private final AuthenticationClient authenticationClient;

    public DeleteAllTokensManager(AuthenticationClient authenticationClient) {
        this.authenticationClient = authenticationClient;
        this.authenticatedManager = new AuthenticatedManager(logger, authenticationClient, successMessage, failureMessage);
    }

    @Override
    public String getMethodsName() {
        return methodName;
    }

    @Override
    public void handleMessage(Message<JsonObject> message) {
        authenticatedManager.handleMessage(message, this::deleteAllTokens);
    }

    private Future<JsonObject> deleteAllTokens(User user){
        return Future.succeededFuture();
    }
}
