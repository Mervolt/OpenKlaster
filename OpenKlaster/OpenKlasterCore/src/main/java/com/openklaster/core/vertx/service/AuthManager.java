package com.openklaster.core.vertx.service;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;

//TODO merge this with service.users.AuthenticatedManager on next iteration
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
        authenticate(message.headers(), message.body())
                .compose(authResult -> processAuthenticatedMessage(authResult, message, methodName))
                .onComplete(handler -> {
                    if (handler.succeeded()) {
                        System.out.println("HALOOOO");
                        System.out.println(handler.result());
                        handleSuccess(methodName, handler.result(), message);
                    } else {
                        handleFailure(methodName, handler.cause(), message);
                    }
                });
    }

    protected void handleFailure(String methodName, Throwable reason, Message<JsonObject> message) {
        logger.error(getFailureMessage(methodName, reason, message));
        BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.BAD_REQUEST, reason.getMessage());
    }

    protected void handleSuccess(String methodName, JsonObject result, Message<JsonObject> message) {
        logger.debug(getSuccessMessage(methodName, result, message));
        BusMessageReplyUtils.replyWithBodyAndStatus(message, result, HttpResponseStatus.OK);
    }

    protected abstract String getFailureMessage(String methodName, Throwable reason, Message<JsonObject> message);

    protected abstract String getSuccessMessage(String methodName, JsonObject result, Message<JsonObject> message);

    protected abstract Future<JsonObject> processAuthenticatedMessage(JsonObject authResult,
                                                                      Message<JsonObject> message, String methodName);

    protected abstract Future<JsonObject> authenticate(MultiMap headers, JsonObject body);

}
