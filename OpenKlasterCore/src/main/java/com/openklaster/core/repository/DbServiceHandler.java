
package com.openklaster.core.repository;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;
import static com.openklaster.common.messages.BusMessageReplyUtils.isInternalServerError;

import java.util.List;
import java.util.stream.Collectors;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class DbServiceHandler<T> {

    private final EventBus eventBus;
    private final Class<T> modelClass;
    private final String address;

    public DbServiceHandler(EventBus eventBus, Class<T> modelClass, String address) {

        this.eventBus = eventBus;
        this.modelClass = modelClass;
        this.address = address;
    }

    public Future<T> handleWithContent(String methodName, JsonObject content) {
        Promise<T> resultPromise = Promise.promise();
        DeliveryOptions options = getMethodOptions(methodName);
        eventBus.<JsonObject>request(address, content, options, handler -> {
            if (handler.succeeded()) {
                resultPromise.complete(handler.result().body().mapTo(this.modelClass));
            } else {
                handleFailure(resultPromise, handler);
            }
        });
        return resultPromise.future();
    }

    public Future<Void> handleVoid(String methodName, JsonObject content) {
        Promise<Void> resultPromise = Promise.promise();
        DeliveryOptions options = getMethodOptions(methodName);
        eventBus.request(address, content, options, handler -> {
            if (handler.succeeded()) {
                resultPromise.complete();
            } else {
                handleFailure(resultPromise, handler);
            }
        });
        return resultPromise.future();
    }

    public Future<List<T>> handleWithListContent(String methodName, JsonObject content) {
        Promise<List<T>> resultPromise = Promise.promise();
        DeliveryOptions options = getMethodOptions(methodName);
        eventBus.<JsonArray>request(address, content, options, handler -> {
            if (handler.succeeded()) {
                resultPromise.complete(mapToListContent(handler.result().body()));
            } else {
                handleFailure(resultPromise, handler);
            }
        });
        return resultPromise.future();
    }

    private List<T> mapToListContent(JsonArray body) {
        return body.stream().map(result -> {
            JsonObject jsonObject = JsonObject.mapFrom(result);
            return jsonObject.mapTo(modelClass);
        }).collect(Collectors.toList());
    }

    private DeliveryOptions getMethodOptions(String methodName) {
        return new DeliveryOptions().addHeader(METHOD_KEY, methodName);
    }

    public void handleFailure(Promise resultPromise, AsyncResult handler) {
        if (isInternalServerError((ReplyException) handler.cause())) {
            resultPromise.fail(new InternalServerErrorException(handler.cause().getMessage()));
        } else {
            resultPromise.fail(handler.cause());
        }
    }
}
