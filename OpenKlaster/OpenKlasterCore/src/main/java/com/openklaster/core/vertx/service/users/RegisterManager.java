package com.openklaster.core.vertx.service.users;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class RegisterManager implements UserManager {
    private static final String methodName = "register";
    private static final Logger logger = LoggerFactory.getLogger(RegisterManager.class);
    private final AuthenticationClient authenticationClient;

    @Override
    public String getMethodsName() {
        return methodName;
    }

    public RegisterManager(AuthenticationClient authenticationClient) {
        this.authenticationClient = authenticationClient;
    }

    @Override
    public void handleMessage(Message<JsonObject> message) {
        User user = processUser(message.body());
        addUser(user).future().onComplete(handler -> {
            if (handler.succeeded()) {
                logger.debug(String.format("User registered - %s", user.getUsername()));
                BusMessageReplyUtils.replyWithStatus(message, HttpResponseStatus.OK);
            } else {
                logger.error(String.format("Could not register user - %s(%s)",
                        user.getUsername(),handler.cause().getMessage()));
                BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.BAD_REQUEST,
                        handler.cause().getMessage());
            }
        });
    }

    private Promise<User> addUser(User user) {
        return Promise.promise();
    }

    User processUser(JsonObject userJson) {
        User user = userJson.mapTo(User.class);
        user.setPassword(authenticationClient.hashUserPassword(user.getPassword()));
        return user;
    }
}
