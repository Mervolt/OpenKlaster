package com.openklaster.core.vertx.service;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.authentication.AuthenticationResult;
import com.openklaster.core.vertx.authentication.FailedAuthenticationException;
import com.openklaster.core.vertx.messages.repository.InternalServerErrorException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import lombok.SneakyThrows;

public abstract class AuthManager {

    protected static final String sessionTokenKey = "sessionToken";
    protected static final String apiTokenKey = "apiToken";
    protected static final String noTokenMsg = "No token was provided to authenticate user %s";
    protected final Logger logger;
    protected final AuthenticationClient authenticationClient;

    public AuthManager(Logger logger, AuthenticationClient authClient) {
        this.logger = logger;
        this.authenticationClient = authClient;
    }

    public void handleMessage(Message<JsonObject> message, String methodName) {
        authenticate(message.headers(), getUser(message.body()))
                .compose(authResult -> processAuthenticatedMessage(authResult, message, methodName))
                .onComplete(handler -> {
                    if (handler.succeeded()) {
                        handleSuccess(methodName, handler.result(), message);
                    } else {
                        handleFailure(methodName, handler.cause(), message);
                    }
                });
    }

    protected void handleFailure(String methodName, Throwable reason, Message<JsonObject> message) {
        logger.error(getFailureMessage(methodName, reason, message), reason);

        HttpResponseStatus response;
        if (reason instanceof FailedAuthenticationException) {
            response = HttpResponseStatus.UNAUTHORIZED;
        } else if (reason instanceof InternalServerErrorException) {
            response = HttpResponseStatus.INTERNAL_SERVER_ERROR;
        } else {
            response = HttpResponseStatus.BAD_REQUEST;
        }

        BusMessageReplyUtils.replyWithError(message, response, reason.getMessage());
    }

    protected void handleSuccess(String methodName, JsonObject result, Message<JsonObject> message) {
        logger.debug(getSuccessMessage(methodName, result, message));
        if (result.containsKey(BusMessageReplyUtils.RETURN_LIST)) {
            BusMessageReplyUtils.replyWithBodyAndStatus(message, result.getJsonArray(BusMessageReplyUtils.RETURN_LIST), HttpResponseStatus.OK);
        } else {
            BusMessageReplyUtils.replyWithBodyAndStatus(message, result, HttpResponseStatus.OK);
        }

    }

    protected abstract String getFailureMessage(String methodName, Throwable reason, Message<JsonObject> message);

    protected abstract String getSuccessMessage(String methodName, JsonObject result, Message<JsonObject> message);

    protected abstract Future<JsonObject> processAuthenticatedMessage(User authenticatedUser,
                                                                      Message<JsonObject> message, String methodName);

    protected Future<User> authenticate(MultiMap headers, Future<User> userFuture) {
        return userFuture.map(user -> authenticateUser(headers, user));
    }

    protected abstract Future<User> getUser(JsonObject entity);

    private User authenticateUser(MultiMap headers, User user) {
        AuthenticationResult authenticationResult;
        try {
            if (headers.contains(apiTokenKey)) {
                authenticationResult = authenticationClient.authenticateWithApiToken(user, headers.get(apiTokenKey));
            } else if (headers.contains(sessionTokenKey)) {
                authenticationResult = authenticationClient.authenticateWithSessionToken(user, headers.get(sessionTokenKey));
            } else {
                throw new IllegalArgumentException(String.format(noTokenMsg, user.getUsername()));
            }

            return resolveAuthenticationResult(authenticationResult, user);
        } catch (Exception e) {
            throw new FailedAuthenticationException(e.getMessage());
        }
    }

    @SneakyThrows
    private User resolveAuthenticationResult(AuthenticationResult authenticationResult, User user) {
        if (authenticationResult.succeeded()) {
            return user;
        } else {
            if (authenticationResult.getCause() instanceof FailedAuthenticationException) {
                throw authenticationResult.getCause();
            }
            throw new FailedAuthenticationException(authenticationResult.getCause());
        }
    }

}
