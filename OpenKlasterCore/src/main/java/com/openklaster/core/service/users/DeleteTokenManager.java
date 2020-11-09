package com.openklaster.core.service.users;

import com.openklaster.common.model.User;
import com.openklaster.core.repository.CrudRepository;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class DeleteTokenManager extends UserManagerHelper {
    private static final String methodName = "deleteToken";
    private static final String successMessage = "Token deleted for entity- %s";
    private static final String failureMessage = "Could not delete token - %s.Reason: %s";
    private static final String tokenKey = "apiToken";
    private static final String noTokenFoundMsg = "No token %s token found for user %s";


    private final CrudRepository<User> userCrudRepository;

    public DeleteTokenManager(CrudRepository<User> userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    protected Future<JsonObject> processMessage(User authenticatedUser, Message<JsonObject> message) {
        String token = message.headers().get(tokenKey);
        boolean deleted = authenticatedUser.deleteUserToken(token);
        if (deleted) {
            return userCrudRepository.update(authenticatedUser).map(new JsonObject().put(tokenKey, token));
        } else return Future.failedFuture(String.format(noTokenFoundMsg, token, authenticatedUser.getUsername()));
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
