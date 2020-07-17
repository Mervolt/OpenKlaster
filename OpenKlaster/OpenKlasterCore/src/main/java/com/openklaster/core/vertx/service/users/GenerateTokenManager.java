package com.openklaster.core.vertx.service.users;

import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.model.User;
import com.openklaster.common.model.UserToken;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.messages.repository.Repository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

public class GenerateTokenManager extends AuthenticatedManager {
    private static final String methodName = "generateToken";
    private static final String successMessage = "Token generated for user - %s";
    private static final String failureMessage = "Could not generate token - %s";
    private final TokenHandler tokenHandler;

    public GenerateTokenManager(AuthenticationClient authenticationClient, TokenHandler tokenHandler,
                                Repository<User> userRepository) {
        super(LoggerFactory.getLogger(GenerateTokenManager.class), authenticationClient, userRepository);
        this.tokenHandler = tokenHandler;
    }

    @Override
    public String getMethodsName() {
        return methodName;
    }

    @Override
    protected Future<JsonObject> processUser(User user) {
        UserToken token = tokenHandler.generateUserToken();
        return storeToken(user, token);
    }

    @Override
    protected String getSuccessMessage(JsonObject result) {
        return String.format(successMessage, result);
    }

    @Override
    protected String getFailureMessage(String reason) {
        return String.format(failureMessage, reason);
    }

    private Future<JsonObject> storeToken(User user, UserToken token) {
        user.addUserToken(token);
        return userRepository.update(user).map(userResult -> JsonObject.mapFrom(token));
    }
}
