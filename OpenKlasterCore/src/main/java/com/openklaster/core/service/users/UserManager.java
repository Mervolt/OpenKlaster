package com.openklaster.core.service.users;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public interface UserManager {

    String getMethodName();

    void handleMessage(Message<JsonObject> message);
}
