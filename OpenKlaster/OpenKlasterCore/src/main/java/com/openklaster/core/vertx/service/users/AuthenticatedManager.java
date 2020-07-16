package com.openklaster.core.vertx.service.users;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;

import java.util.function.Function;

public class AuthenticatedManager {

    private final Logger logger;
    private final AuthenticationClient authenticationClient;
    private static final String usernameKey = "username";
    private final String successMessage;
    private final String failureMessage;

    public AuthenticatedManager(Logger logger, AuthenticationClient authenticationClient, String successMessage,
                                String failureMessage) {
        this.logger = logger;
        this.authenticationClient = authenticationClient;
        this.successMessage = successMessage;
        this.failureMessage = failureMessage;
    }

    public void handleMessage(Message<JsonObject> message, Function<User, Future<JsonObject>> handlerFun) {
        authenticate(message.headers(), message.body().getString(usernameKey))
                .compose(handlerFun)
                .onComplete(handler -> {
                    if (handler.succeeded()) {
                        handleSuccess(handler.result(), message);
                    } else {
                        handleFailure(handler.cause().getMessage(), message);
                    }
                });
    }

    public void handleMessage(Message<JsonObject> message, Future<JsonObject> futureToHandle) {
        authenticate(message.headers(), message.body().getString(usernameKey))
                .compose(res -> futureToHandle)
                .onComplete(handler -> {
                    if (handler.succeeded()) {
                        handleSuccess(handler.result(), message);
                    } else {
                        handleFailure(handler.cause().getMessage(), message);
                    }
                });
    }

    private void handleFailure(String reason, Message<JsonObject> message) {
        logger.error(String.format(failureMessage, reason));
        BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.BAD_REQUEST,
                reason);
    }

    private void handleSuccess(JsonObject result, Message<JsonObject> message) {
        logger.debug(String.format(successMessage, result));
        BusMessageReplyUtils.replyWithStatus(message, HttpResponseStatus.OK);
    }

    private Future<User> authenticate(MultiMap map, String username) {
        return Future.succeededFuture();
    }

}
