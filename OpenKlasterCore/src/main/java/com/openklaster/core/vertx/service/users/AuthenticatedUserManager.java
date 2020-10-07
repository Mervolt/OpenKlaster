package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.messages.repository.CrudRepository;
import com.openklaster.core.vertx.service.AuthManager;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.openklaster.common.model.ModelProperties.usernameKey;

public class AuthenticatedUserManager extends AuthManager {
    private final CrudRepository<User> userRepository;
    private final Map<String, UserManagerHelper> methodHelpers;

    public AuthenticatedUserManager(AuthenticationClient authClient, CrudRepository<User> userRepository) {
        super(LoggerFactory.getLogger(AuthenticatedUserManager.class), authClient);
        this.userRepository = userRepository;
        this.methodHelpers = new HashMap<>();
    }

    public void addMethodHelper(String methodName, UserManagerHelper helper) {
        this.methodHelpers.put(methodName, helper);
    }

    public boolean hasMessageHandler(String methodName) {
        return methodHelpers.containsKey(methodName);
    }

    @Override
    protected Future<User> getUser(JsonObject entity) {
        return userRepository.get(entity.getString(usernameKey));
    }

    @Override
    protected Future<JsonObject> processAuthenticatedMessage(User authenticatedUser,
                                                             Message<JsonObject> message, String methodName) {
        return methodHelpers.get(methodName).processMessage(authenticatedUser, message);
    }

    @Override
    protected String getFailureMessage(String methodName, Throwable reason, Message<JsonObject> message) {
        return methodHelpers.get(methodName).getFailureMessage(reason, message);
    }

    @Override
    protected String getSuccessMessage(String methodName, JsonObject result, Message<JsonObject> message) {
        return methodHelpers.get(methodName).getSuccessMessage(result, message);
    }
}
