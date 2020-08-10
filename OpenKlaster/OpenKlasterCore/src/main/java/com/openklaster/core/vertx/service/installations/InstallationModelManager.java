package com.openklaster.core.vertx.service.installations;

import com.openklaster.common.model.Installation;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.messages.repository.CrudRepository;
import com.openklaster.core.vertx.service.ModelManager;
import com.openklaster.core.vertx.service.UserRetriever;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

public class InstallationModelManager extends ModelManager<Installation> {

    private final CrudRepository<Installation> installationCrudRepository;

    public InstallationModelManager(AuthenticationClient authClient, CrudRepository<Installation> entityCrudRepository,
                                    UserRetriever userRetriever) {
        super(LoggerFactory.getLogger(InstallationModelManager.class), authClient, Installation.class, userRetriever);;
        this.installationCrudRepository = entityCrudRepository;
    }

    @Override
    protected Future<JsonObject> processAuthenticatedMessage(User authenticatedUser, Message<JsonObject> message, String methodName) {
        switch (methodName) {
            case getMethodName:
                return get(message.body().getString(idKey)).map(JsonObject::mapFrom);
            case addMethodName:
                return add(message.body().mapTo(modelClass)).map(JsonObject::mapFrom);
            case deleteMethodName:
                return delete(message.body().getString(idKey)).map(new JsonObject());
            case updateMethodName:
                return update(message.body().mapTo(modelClass)).map(JsonObject::mapFrom);
            default:
                throw new IllegalArgumentException(String.format("This operations is not allowed: %s", methodName));
        }
    }

    protected Future<Installation> get(String id) {
        return installationCrudRepository.get(id);
    }

    protected Future<Installation> add(Installation entity) {
        return installationCrudRepository.add(entity);
    }

    protected Future<Void> delete(String id) {
        return installationCrudRepository.delete(id);
    }

    protected Future<Installation> update(Installation entity) {
        return installationCrudRepository.update(entity);
    }
}
