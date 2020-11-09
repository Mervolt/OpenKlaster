package com.openklaster.core.service.users;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import static com.openklaster.common.model.ModelProperties.emailKey;
import static com.openklaster.common.model.ModelProperties.newPasswordKey;
import static com.openklaster.common.model.ModelProperties.passwordKey;
import static com.openklaster.common.model.ModelProperties.usernameKey;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.User;
import com.openklaster.core.authentication.AuthenticationClient;
import com.openklaster.core.authentication.AuthenticationResult;
import com.openklaster.core.repository.CrudRepository;

public class UpdateUserManager implements UserManager {
    private static final String methodName = "updateUser";
    private static final String successMessage = "User updated - %s";
    private static final String failureMessage = "Could not update user -  %s";
    private static final Logger logger = LoggerFactory.getLogger(UpdateUserManager.class);
    private final AuthenticationClient authenticationClient;
    private final CrudRepository<User> userCrudRepository;

    public UpdateUserManager(AuthenticationClient authenticationClient, CrudRepository<User> userCrudRepository) {
        this.authenticationClient = authenticationClient;
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public void handleMessage(final Message<JsonObject> message) {
        authenticateUser(message.body()).onComplete(handler -> {
            if (handler.succeeded()) {
                handleSuccess(handler.result(), message);
            } else {
                handleFailure(handler.cause().getMessage(), message);
            }
        });
    }

    private void handleFailure(String reason, Message<JsonObject> message) {
        logger.error(String.format(failureMessage, reason), reason);
        BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.UNAUTHORIZED,
                                            String.format(failureMessage, reason));
    }

    private void handleSuccess(AuthenticationResult user, Message<JsonObject> message) {
        logger.debug(String.format(successMessage, user.succeeded()));
        BusMessageReplyUtils.replyWithStatus(message, HttpResponseStatus.OK);
    }


    private Future<AuthenticationResult> authenticateUser(JsonObject body) {
        String username = body.getString(usernameKey);
        String password = body.getString(passwordKey);

        return getUser(username)
            .map(user -> authenticationClient.authenticateWithPassword(user, password))
            .compose(result -> updateUser(result, body));
    }

    private Future<User> getUser(String username) {
        return userCrudRepository.get(username);
    }

    private Future<AuthenticationResult> updateUser(AuthenticationResult result, JsonObject username) {
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
                                                                              JsonObject username) {
        Future<AuthenticationResult> resultFuture = Future.succeededFuture(result);
        return persistUpdate(username).compose(res -> resultFuture);
    }

    private Future<User> persistUpdate(JsonObject username) {

        return userCrudRepository.get(username.getString(usernameKey))
                                 .compose(userResult -> {
                                     userResult.setPassword(authenticationClient.hashUserPassword(username.getString(newPasswordKey)));
                                     userResult.setEmail(username.getString(emailKey));
                                     return userCrudRepository.update(userResult);
                                 });
    }

}
