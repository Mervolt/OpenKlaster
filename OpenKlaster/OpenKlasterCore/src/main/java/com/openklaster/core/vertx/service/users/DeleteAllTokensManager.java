package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.messages.repository.Repository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

public class DeleteAllTokensManager extends AuthenticatedManager {
    private static final String methodName = "deleteAllTokens";
    private static final String successMessage = "Tokens deleted - %s";
    private static final String failureMessage = "Could not delete all tokens - %s";
    private static final String deletedTokensAmountKey = "tokensDeleted";

    public DeleteAllTokensManager(AuthenticationClient authenticationClient, Repository<User> userRepository) {
        super(LoggerFactory.getLogger(DeleteAllTokensManager.class), authenticationClient, userRepository);
    }

    @Override
    public String getMethodsName() {
        return methodName;
    }

    @Override
    protected Future<JsonObject> processUser(User user) {
        int tokensAmount = user.getUserTokens().size();
        user.deleteAllUserTokens();
        return userRepository.update(user).map(new JsonObject().put(deletedTokensAmountKey, tokensAmount));
    }

    @Override
    protected String getSuccessMessage(JsonObject result) {
        return String.format(successMessage, result);
    }

    @Override
    protected String getFailureMessage(String reason) {
        return String.format(failureMessage, reason);
    }
}
