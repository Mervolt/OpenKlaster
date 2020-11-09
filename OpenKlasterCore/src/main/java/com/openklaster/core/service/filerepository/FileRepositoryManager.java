package com.openklaster.core.service.filerepository;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.User;
import com.openklaster.core.authentication.AuthenticationClient;
import com.openklaster.core.repository.FileRepository;
import com.openklaster.core.service.AuthManager;
import com.openklaster.core.service.UserRetriever;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

import java.util.List;

public class FileRepositoryManager extends AuthManager {

    private final static String getSelectableDatesMethodName = "selectableDates";

    private final UserRetriever userRetriever;

    private final FileRepository fileRepository;

    public FileRepositoryManager(AuthenticationClient authClient, UserRetriever userRetriever, FileRepository fileRepository) {
        super(LoggerFactory.getLogger(FileRepositoryManager.class), authClient);
        this.userRetriever = userRetriever;
        this.fileRepository = fileRepository;
    }


    @Override
    protected Future<JsonObject> processAuthenticatedMessage(User authenticatedUser, Message<JsonObject> message, String methodName) {
        switch (methodName) {
            case getSelectableDatesMethodName:
                return get(message.body(), authenticatedUser.getUsername())
                        .map(list -> new JsonObject().put(BusMessageReplyUtils.RETURN_LIST, list));


            default:
                throw new IllegalArgumentException(String.format("This operations is not allowed: %s", methodName));
        }
    }

    private Future<JsonArray> get(JsonObject query, String username) {
        query.put("username", username);
        System.out.println(query);
        return fileRepository.handleWithListContent("get", query);
    }

    @Override
    protected String getFailureMessage(String methodName, Throwable reason, Message<JsonObject> message) {
        return null;
    }

    @Override
    protected String getSuccessMessage(String methodName, JsonObject result, Message<JsonObject> message) {
        return null;
    }

    @Override
    protected Future<User> getUser(JsonObject entity) {
        return userRetriever.retrieveUser(entity);
    }
}
