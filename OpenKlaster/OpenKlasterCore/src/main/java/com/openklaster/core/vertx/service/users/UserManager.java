package com.openklaster.core.vertx.service.users;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public interface UserManager {
    String getMethodName();
    void handleMessage(Message<JsonObject> message);

    String usernameKey = "username";
    String passwordKey = "password";
    String emailKey = "email";
    String userTokensKey = "userTokens";
}