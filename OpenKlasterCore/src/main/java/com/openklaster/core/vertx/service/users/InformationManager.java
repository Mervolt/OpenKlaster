package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.User;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class InformationManager extends UserManagerHelper {
    private static final String methodName = "info";
    private static final String failureMessage = "Could not get user info - %s.\nReason :%s";
    private static final String successMessage = "User info retrieved - %s";

    @Override
    protected Future<JsonObject> processMessage(User authenticatedUser, Message<JsonObject> message) {
        return Future.succeededFuture(authenticatedUser.toUserInfo());
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public String getFailureMessage(Throwable reason, Message<JsonObject> message) {
        return String.format(failureMessage, message.toString(), reason.getMessage());
    }

    @Override
    public String getSuccessMessage(JsonObject result, Message<JsonObject> message) {
        return String.format(successMessage, result.toString());
    }
}
