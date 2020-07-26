package com.openklaster.core.vertx.messages.repository;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;

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
        System.out.println(address  + " " + content + " " + options);
        eventBus.<JsonObject>request(address, content, options, handler -> {
            if (handler.succeeded()) {
                System.out.println("succ");
                resultPromise.complete(handler.result().body().mapTo(this.modelClass));
            } else {
                System.out.println("dsa");
                resultPromise.fail(handler.cause());
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
                resultPromise.fail(handler.cause());
            }
        });
        return resultPromise.future();
    }

    public Future<List<T>> handleWithListContent(String methodName, JsonObject content) {
        Promise<List<T>> resultPromise = Promise.promise();
        DeliveryOptions options = getMethodOptions(methodName);
        eventBus.<JsonArray>request(address, content, options, handler -> {
            if (handler.succeeded()) {
                System.out.println(1);
                resultPromise.complete(mapToListContent(handler.result().body()));
            } else {
                System.out.println(2);
                resultPromise.fail(handler.cause());
            }
        });

        return resultPromise.future();
    }

    private List<T> mapToListContent(JsonArray body) {
        // Todo
        System.out.println(body);
        System.out.println(body.stream().map(result -> JsonObject.mapFrom(result).mapTo(modelClass)).collect(Collectors.toList()));
        return body.stream().map(result -> JsonObject.mapFrom(result).mapTo(modelClass)).collect(Collectors.toList());
    }

    private DeliveryOptions getMethodOptions(String methodName) {
        return new DeliveryOptions().addHeader(METHOD_KEY, methodName);
    }
}
