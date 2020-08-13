package com.openklaster.core.vertx.service.users;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.authentication.AuthenticationResult;
import com.openklaster.core.vertx.messages.repository.CrudRepository;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import lombok.SneakyThrows;

public abstract class AuthenticatedManager implements UserManager {

    protected final Logger logger;
    protected final AuthenticationClient authenticationClient;
    protected final CrudRepository<User> userCrudRepository;
    private static final String sessionTokenKey = "sessionToken";
    private static final String apiTokenKey = "apiToken";
    private static final String noTokenMsg = "No token was provided to authenticate user %s";

    public AuthenticatedManager(Logger logger, AuthenticationClient authenticationClient, CrudRepository<User> userCrudRepository) {
        this.logger = logger;
        this.authenticationClient = authenticationClient;
        this.userCrudRepository = userCrudRepository;
    }

    public void handleMessage(Message<JsonObject> message) {
        authenticate(message.headers(), message.body().getString(usernameKey))
                .compose(this::processUser)
                .onComplete(handler -> {
                    if (handler.succeeded()) {
                        handleSuccess(handler.result(), message);
                    } else {
                        handleFailure(handler.cause().getMessage(), message);
                    }
                });
    }

    protected abstract Future<JsonObject> processUser(User user);

    protected void handleFailure(String reason, Message<JsonObject> message) {
        logger.error(getFailureMessage(reason));
        BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.BAD_REQUEST,
                reason);
    }

    protected void handleSuccess(JsonObject result, Message<JsonObject> message) {
        logger.debug(getSuccessMessage(result));
        BusMessageReplyUtils.replyWithBodyAndStatus(message,result, HttpResponseStatus.OK);
    }

    protected abstract String getSuccessMessage(JsonObject result);

    protected abstract String getFailureMessage(String reason);

    protected Future<User> authenticate(MultiMap map, String username) {
        System.out.println(username);
        return userCrudRepository.get(username)
                .map(user -> authenticateUser(map, user));
    }

    private User authenticateUser(MultiMap map, User user) {
        AuthenticationResult authenticationResult;
        if (map.contains(apiTokenKey)) {
            authenticationResult = authenticationClient.authenticateWithToken(user, map.get(apiTokenKey));
        } else if (map.contains(sessionTokenKey)) {
            authenticationResult = authenticationClient.authenticateWithSessionToken(user, map.get(sessionTokenKey));
        } else throw new IllegalArgumentException(String.format(noTokenMsg, user.getUsername()));

        return resolveAuthenticationResult(authenticationResult, user);
    }

    @SneakyThrows
    private User resolveAuthenticationResult(AuthenticationResult authenticationResult, User user) {
        if (authenticationResult.succeeded()) {
            return user;
        } else throw authenticationResult.getCause();
    }

}
