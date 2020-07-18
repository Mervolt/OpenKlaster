package com.openklaster.core.vertx.messages.repository;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class MongoRepository<T> implements Repository<T> {
    private final Class<T> modelClass;
    private final EventBus eventBus;
    private final String address;
    private final static String methodKey = "method";
    private final static String idKey = "_id";
    private final static String addKey = "add";
    private final static String getKey = "find";
    private final static String deleteKey = "remove";
    private final static String updateKey = "update";

    public MongoRepository(Class<T> modelClass, EventBus eventBus, String address) {
        this.modelClass = modelClass;
        this.eventBus = eventBus;
        this.address = address;
    }

    @Override
    public Future<T> add(T content) {
        return handleWithContent(addKey, JsonObject.mapFrom(content));
    }

    @Override
    public Future<T> get(String id) {
        JsonObject content = new JsonObject().put(idKey, id);
        return handleWithContent(getKey, content);
    }

    @Override
    public Future<T> update(T content) {
        return handleWithContent(updateKey, JsonObject.mapFrom(content));
    }

    @Override
    public Future<Void> delete(String id) {
        JsonObject content = new JsonObject().put(idKey, id);
        return handleVoid(deleteKey, content);
    }

    @Override
    public Future<Void> delete(T content) {
        JsonObject mappedContent = JsonObject.mapFrom(content);
        return delete(mappedContent.getString(idKey));
    }

    private Future<Void> handleVoid(String methodName, JsonObject content) {
        Promise<Void> resultPromise = Promise.promise();
        DeliveryOptions options = new DeliveryOptions().addHeader(methodKey, methodName);
        eventBus.request(address, content, options, handler -> {
            if (handler.succeeded()) {
                resultPromise.complete();
            } else {
                resultPromise.fail(handler.cause());
            }
        });
        return resultPromise.future();
    }

    private Future<T> handleWithContent(String methodName, JsonObject content) {
        Promise<T> resultPromise = Promise.promise();
        DeliveryOptions options = new DeliveryOptions().addHeader(methodKey, methodName);
        eventBus.<JsonObject>request(address, content, options, handler -> {
            if (handler.succeeded()) {
                resultPromise.complete(handler.result().body().mapTo(this.modelClass));
            } else {
                resultPromise.fail(handler.cause());
            }
        });
        return resultPromise.future();
    }
}
