package com.openklaster.core.service.installations;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.Installation;
import com.openklaster.common.model.User;
import com.openklaster.core.authentication.AuthenticationClient;
import com.openklaster.core.repository.CrudRepository;
import com.openklaster.core.repository.MongoCrudRepository;
import com.openklaster.core.service.ModelManager;
import com.openklaster.core.service.UserRetriever;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

import java.util.List;

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
            case getAllMethodName:
                return getAllByUsername(message.body().getString(userKey)).map(list -> new JsonObject().put(BusMessageReplyUtils.RETURN_LIST, list));
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

    protected Future<List<Installation>> getAllByUsername(String username) {
        MongoCrudRepository mongoCrudRepository = (MongoCrudRepository) installationCrudRepository;
        return mongoCrudRepository.getAllByUsername(username);
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
