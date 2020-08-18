package com.openklaster.core.vertx.service;

import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;

public abstract class ModelManager<T> extends AuthManager {

    protected final Class<T> modelClass;
    protected final UserRetriever userRetriever;
    protected final static String getMethodName = "get";
    protected final static String getAllMethodName = "getAll";
    protected final static String updateMethodName = "put";
    protected final static String addMethodName = "post";
    protected final static String deleteMethodName = "delete";
    protected final static String idKey = "installationId";
    protected final static String userKey = "username";

    public ModelManager(Logger logger, AuthenticationClient authClient,
                        Class<T> modelClass, UserRetriever userRetriever) {
        super(logger, authClient);
        this.modelClass = modelClass;
        this.userRetriever = userRetriever;
    }

    @Override
    protected String getFailureMessage(String methodName, Throwable reason, Message<JsonObject> message) {
        return String.format("Problem with method %s for %s entity: %s.\nReason: %s",
                methodName, modelClass.getSimpleName(), message.body(), reason.getCause());
    }

    @Override
    protected String getSuccessMessage(String methodName, JsonObject result, Message<JsonObject> message) {
        return String.format("Successful %s for %s entity: %s.",
                methodName, modelClass.getSimpleName(), result);
    }

    @Override
    protected Future<User> getUser(JsonObject entity) {
        return userRetriever.retrieveUser(entity);
    }
}
