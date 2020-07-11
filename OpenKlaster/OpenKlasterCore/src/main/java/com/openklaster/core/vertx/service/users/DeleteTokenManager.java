package com.openklaster.core.vertx.service.users;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class DeleteTokenManager implements  UserManager {
    @Override
    public String getMethodsName() {
        return null;
    }

    @Override
    public void handleMessage(Message<JsonObject> message) {

    }
}
