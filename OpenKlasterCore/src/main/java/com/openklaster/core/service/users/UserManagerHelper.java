package com.openklaster.core.service.users;

import com.openklaster.common.model.User;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public abstract class UserManagerHelper {

    protected abstract Future<JsonObject> processMessage(User authenticatedUser, Message<JsonObject> message);

    public abstract String getMethodName();
    public abstract String getFailureMessage(Throwable reason, Message<JsonObject> message);
    public abstract String getSuccessMessage(JsonObject result, Message<JsonObject> message);

}
