package com.openklaster.core.vertx.service;

import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.authentication.AuthenticationResult;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import lombok.SneakyThrows;

//Yea this class has methods similar to service.users *Manager classes but
// unifying this will be done on next iteration
public abstract class ModelManager<T> extends AuthManager {

    protected final Class<T> modelClass;
    protected final UserRetriever userRetriever;
    protected final static String getMethodName = "get";
    protected final static String updateMethodName = "put";
    protected final static String addMethodName = "post";
    protected final static String deleteMethodName = "delete";
    protected final static String idKey = "installationId";

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
    protected abstract Future<JsonObject> processAuthenticatedMessage(JsonObject authResult, Message<JsonObject> message,
                                                                      String methodName);

    @Override
    protected Future<JsonObject> authenticate(MultiMap headers, JsonObject body) {
        return userRetriever.retrieveUser(body)
                .map(user -> authenticateUser(headers, user));
    }

    private JsonObject authenticateUser(MultiMap headers, User user) {
        AuthenticationResult authenticationResult;
        if (headers.contains(apiTokenKey)) {
            authenticationResult = authenticationClient.authenticateWithToken(user, headers.get(apiTokenKey));
        } else if (headers.contains(sessionTokenKey)) {
            authenticationResult = authenticationClient.authenticateWithSessionToken(user, headers.get(sessionTokenKey));
        } else throw new IllegalArgumentException(String.format(noTokenMsg, user.getUsername()));

        return resolveAuthenticationResult(authenticationResult, user);
    }

    @SneakyThrows
    private JsonObject resolveAuthenticationResult(AuthenticationResult authenticationResult, User user) {
        if (authenticationResult.succeeded()) {
            return JsonObject.mapFrom(user);
        } else throw authenticationResult.getCause();
    }
}
