package com.openklaster.core.vertx.service.measurements;

import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.messages.repository.CassandraRepository;
import com.openklaster.core.vertx.messages.repository.CrudRepository;
import com.openklaster.core.vertx.service.ModelManager;
import com.openklaster.core.vertx.service.UserRetriever;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

import java.util.List;


public class MeasurementManager<T> extends ModelManager<T> {

    private final CassandraRepository<T> cassandraRepository;

    public MeasurementManager( AuthenticationClient authClient,
                              UserRetriever userRetriever, Class<T> myModeClass,
                              CassandraRepository<T> cassandraRepository) {
        super(LoggerFactory.getLogger(MeasurementManager.class),
                authClient, myModeClass, userRetriever);
        this.cassandraRepository = cassandraRepository;
    }

    @Override
    protected Future<JsonObject> processAuthenticatedMessage(User authenticatedUser, Message<JsonObject> message, String methodName) {
        switch (methodName) {
            case getMethodName:
                return get(message.body()).map(JsonObject::mapFrom);
            case addMethodName:
                return add(message.body().mapTo(modelClass)).map(JsonObject::mapFrom);
            default:
                throw new IllegalArgumentException(String.format("This operations is not allowed: %s", methodName));
        }
    }

    private Future<T> add(T entity) {
        return cassandraRepository.add(entity);
    }

    private Future<List<T>> get(JsonObject query) {
        return cassandraRepository.getFromDates(query);
    }
}
