package com.openklaster.core.vertx.service.users;

import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.model.User;
import com.openklaster.common.model.UserToken;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.messages.repository.CrudRepository;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

public class GenerateTokenManager extends UserManagerHelper {
    private static final String methodName = "generateToken";
    private static final String successMessage = "Token generated for user - %s";
    private static final String failureMessage = "Could not generate token - %s.\nReason: %s";
    private final TokenHandler tokenHandler;
    private final CrudRepository<User> userCrudRepository;

    public GenerateTokenManager(TokenHandler tokenHandler, CrudRepository<User> userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
        this.tokenHandler = tokenHandler;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    protected Future<JsonObject> processMessage(User authenticatedUser, Message<JsonObject> message) {
        UserToken token = tokenHandler.generateUserToken();
        return storeToken(authenticatedUser, token);
    }

    @Override
    public String getFailureMessage(Throwable reason, Message<JsonObject> message) {
        return String.format(failureMessage, message.toString(), reason.getMessage());
    }

    @Override
    public String getSuccessMessage(JsonObject result, Message<JsonObject> message) {
        return String.format(successMessage, result.toString());
    }

    private Future<JsonObject> storeToken(User user, UserToken token) {
        user.addUserToken(token);
        return userCrudRepository.update(user).map(userResult -> JsonObject.mapFrom(token));
    }
}
