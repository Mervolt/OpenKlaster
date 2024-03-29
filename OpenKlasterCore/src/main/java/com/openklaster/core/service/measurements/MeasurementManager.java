package com.openklaster.core.service.measurements;

import com.openklaster.common.messages.BusMessageReplyUtils;
import com.openklaster.common.model.User;
import com.openklaster.core.authentication.AuthenticationClient;
import com.openklaster.core.repository.CassandraRepository;
import com.openklaster.core.service.ModelManager;
import com.openklaster.core.service.UserRetriever;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

import java.util.List;


public class MeasurementManager<T> extends ModelManager<T> {

    private final CassandraRepository<T> cassandraRepository;

    public MeasurementManager(AuthenticationClient authClient,
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
                return get(message.body()).map(list -> new JsonObject().put(BusMessageReplyUtils.RETURN_LIST, list));
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
