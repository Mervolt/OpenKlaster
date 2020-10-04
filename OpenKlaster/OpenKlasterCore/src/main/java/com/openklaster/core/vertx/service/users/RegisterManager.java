package com.openklaster.core.vertx.service.users;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.messages.repository.CrudRepository;
import com.openklaster.core.vertx.properties.CoreErrorMessages;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class RegisterManager implements UserManager {
    private static final String methodName = "register";
    private static final String successMessage = "User registered - %s";
    private static final String failureMessage = CoreErrorMessages.REGISTER_FAILURE;
    private static final Logger logger = LoggerFactory.getLogger(RegisterManager.class);
    private final AuthenticationClient authenticationClient;
    private final CrudRepository<User> userCrudRepository;

    @Override
    public String getMethodName() {
        return methodName;
    }

    public RegisterManager(AuthenticationClient authenticationClient, CrudRepository<User> userCrudRepository) {
        this.authenticationClient = authenticationClient;
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public void handleMessage(Message<JsonObject> message) {
        User user = getUserWithHashedPassword(message.body());
        addUser(user).onComplete(handler -> {
            if (handler.succeeded()) {
                handleSuccess(handler.result(), message);
            } else {
                handleFailure(user, message, handler.cause().getMessage());
            }
        });
    }

    private void handleSuccess(User user, Message<JsonObject> message) {
        logger.debug(String.format(successMessage, user.getUsername()));
        BusMessageReplyUtils.replyWithStatus(message, HttpResponseStatus.OK);
    }

    private void handleFailure(User user, Message<JsonObject> message, String reason) {
        logger.error(String.format(failureMessage,
                user.getUsername(), reason), reason);
        BusMessageReplyUtils.replyWithError(message, HttpResponseStatus.BAD_REQUEST,
                String.format(failureMessage, reason));
    }

    private Future<User> addUser(User user) {
        return userCrudRepository.add(user);
    }

    private User getUserWithHashedPassword(JsonObject userJson) {
        User user = userJson.mapTo(User.class);
        user.setPassword(authenticationClient.hashUserPassword(user.getPassword()));
        return user;
    }
}
