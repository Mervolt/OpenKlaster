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
import io.vertx.core.logging.LoggerFactory;

public class InformationManager implements UserManager {
    private static final String methodName = "info";
    private static final String failureMessage = "Could not get user info - %s";
    private static final String successMessage = "User info retrieved - %s";
    private static final Logger logger = LoggerFactory.getLogger(InformationManager.class);
    private final AuthenticationClient authenticationClient;
    private final AuthenticatedManager authenticatedManager;


    public InformationManager(AuthenticationClient authenticationClient) {
        this.authenticationClient = authenticationClient;
        this.authenticatedManager = new AuthenticatedManager(logger,authenticationClient,successMessage,failureMessage);
    }

    @Override
    public String getMethodsName() {
        return methodName;
    }

    @Override
    public void handleMessage(Message<JsonObject> message) {
        authenticatedManager.handleMessage(message,this::getUserInfo);
    }

    private Future<JsonObject> getUserInfo(User user) {
        return Future.succeededFuture();
    }
}
