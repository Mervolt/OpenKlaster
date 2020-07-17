package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.messages.repository.Repository;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

public class DeleteTokenManager extends AuthenticatedManager {
    private static final String methodName = "generateToken";
    private static final String successMessage = "Token deleted - %s";
    private static final String failureMessage = "Could not delete token - %s";
    private static final String tokenKey = "token";
    private static final String noTokenFoundMsg = "No token %s token found for user %s";
    private static final String unsupportedOperationMsg =
            "Method processUser is not supported for DeleteTokenManager class!";

    public DeleteTokenManager(AuthenticationClient authenticationClient, Repository<User> userRepository) {
        super(LoggerFactory.getLogger(DeleteTokenManager.class), authenticationClient, userRepository);
    }

    @Override
    public String getMethodsName() {
        return methodName;
    }

    @Override
    public void handleMessage(Message<JsonObject> message) {
        authenticate(message.headers(), message.body().getString(usernameKey))
                .compose(user -> deleteToken(user, message.body().getString(tokenKey)))
                .onComplete(handler -> {
                    if (handler.succeeded()) {
                        handleSuccess(handler.result(), message);
                    } else {
                        handleFailure(handler.cause().getMessage(), message);
                    }
                });
    }

    @Override
    protected Future<JsonObject> processUser(User user) {
        throw new UnsupportedOperationException(unsupportedOperationMsg);
    }

    @Override
    protected String getSuccessMessage(JsonObject result) {
        return String.format(successMessage, result);
    }

    @Override
    protected String getFailureMessage(String reason) {
        return String.format(failureMessage, reason);
    }

    private Future<JsonObject> deleteToken(User user, String token) {
        boolean deleted = user.deleteToken(token);
        if (deleted) {
            return userRepository.update(user).map(new JsonObject().put(tokenKey, token));
        } else return Future.failedFuture(String.format(noTokenFoundMsg, token, user.getUsername()));
    }
}
