package com.openklaster.core.vertx.messages.repository;

import com.openklaster.common.model.Installation;
import com.openklaster.common.model.LoadMeasurement;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        eventBus.<JsonObject>request(address, content, options, handler -> {
            if (handler.succeeded()) {
                resultPromise.complete(handler.result().body().mapTo(this.modelClass));
            } else {
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
                resultPromise.complete(mapToListContent(handler.result().body()));
            } else {
                resultPromise.fail(handler.cause());
            }
        });
        return resultPromise.future();
    }

    private List<T> mapToListContent(JsonArray body) {
        // Todo
        Stream<T> out = body.stream().map(result -> {
            JsonObject jsonObject = JsonObject.mapFrom(result);
            System.out.println("json " + jsonObject);
            System.out.println(modelClass);
            return jsonObject.mapTo(modelClass);
        });
        System.out.println("STREAM" + out);
        List<T> output = out.collect(Collectors.toList());
        System.out.println("LIST" + output);
        System.out.println("LIST" + output.getClass());
        return output;
    }

    private DeliveryOptions getMethodOptions(String methodName) {
        return new DeliveryOptions().addHeader(METHOD_KEY, methodName);
    }
}