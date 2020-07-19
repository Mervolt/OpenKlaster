package com.openklaster.core.vertx.service.users;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.authentication.AuthenticationResult;
import com.openklaster.core.vertx.authentication.SuccessfulSessionAuthentication;
import com.openklaster.core.vertx.messages.repository.CrudRepository;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class LoginManager implements UserManager {
    private static final String methodName = "login";
    private static final String successMessage = "User logged in - %s";
    private static final String failedMessage = "Cannot login - %s";
    private static final Logger logger = LoggerFactory.getLogger(LoginManager.class);
    private final AuthenticationClient authenticationClient;
    private final CrudRepository<User> userCrudRepository;

    public LoginManager(AuthenticationClient authenticationClient, CrudRepository<User> userCrudRepository) {
        this.authenticationClient = authenticationClient;
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public void handleMessage(Message<JsonObject> message) {
        authenticateUser(message.body()).onComplete(handler -> {
            if (handler.succeeded()) {
                handleSuccess(handler.result(), message);
            } else {
                handleFailure(handler.cause().getMessage(), message);
            }
        });
    }

    private void handleFailure(String reason, Message<JsonObject> message) {
        logger.error(String.format(failedMessage, reason));
        BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.UNAUTHORIZED,
                String.format(failedMessage, reason));
    }

    private void handleSuccess(AuthenticationResult result, Message<JsonObject> message) {
        logger.debug(String.format(successMessage, result));
        BusMessageReplyUtils.replyWithBodyAndStatus(message, JsonObject.mapFrom(result), HttpResponseStatus.OK);
    }

    private Future<AuthenticationResult> authenticateUser(JsonObject body) {
        String username = body.getString(usernameKey);
        String password = body.getString(passwordKey);

        return getUser(username)
                .map(user -> authenticationClient.authenticateWithPassword(user, password))
                .compose(result -> storeToken(result, username));
    }

    private Future<User> getUser(String username) {
        return userCrudRepository.get(username);
    }

    private Future<AuthenticationResult> storeToken(AuthenticationResult result, String username) {
        if (result.succeeded()) {
            return handleSuccessfulAuthenticationResult(result, username);
        } else {
            return handleFailedAuthenticationResult(result);
        }
    }

    private Future<AuthenticationResult> handleFailedAuthenticationResult(AuthenticationResult result) {
        return Future.failedFuture(result.getCause());
    }

    private Future<AuthenticationResult> handleSuccessfulAuthenticationResult(AuthenticationResult result,
                                                                              String username) {
        Future<AuthenticationResult> resultFuture = Future.succeededFuture(result);
        return persistSessionToken(username, result).compose(res -> resultFuture);
    }

    private Future<User> persistSessionToken(String username, AuthenticationResult result) {

        SuccessfulSessionAuthentication sessionAuthResult = (SuccessfulSessionAuthentication) result;

        return userCrudRepository.get(username)
                .compose(userResult -> {
                    userResult.setSessionToken(sessionAuthResult.getSessionToken());
                    return userCrudRepository.update(userResult);
                });
    }
}
