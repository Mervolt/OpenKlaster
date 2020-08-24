package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.User;
import com.openklaster.core.vertx.messages.repository.CrudRepository;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class DeleteAllTokensManager extends UserManagerHelper {
    private static final String methodName = "deleteAllTokens";
    private static final String successMessage = "Tokens deleted for query: %s";
    private static final String failureMessage = "Could not delete all tokens for entity: %s.\nReason: %s";
    private static final String deletedTokensAmountKey = "tokensDeleted";

    private final CrudRepository<User> userCrudRepository;

    public DeleteAllTokensManager(CrudRepository<User> userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    protected Future<JsonObject> processMessage(User authenticatedUser, Message<JsonObject> message) {
        int tokensAmount = authenticatedUser.getUserTokens().size();
        authenticatedUser.deleteAllUserTokens();
        return userCrudRepository.update(authenticatedUser).map(new JsonObject().put(deletedTokensAmountKey, tokensAmount));
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
