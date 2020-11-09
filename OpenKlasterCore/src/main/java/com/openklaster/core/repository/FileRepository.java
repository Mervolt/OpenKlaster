package com.openklaster.core.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;
import static com.openklaster.common.messages.BusMessageReplyUtils.isInternalServerError;

@AllArgsConstructor
public class FileRepository {
    private final EventBus eventBus;
    private final String address;

    public Future<JsonArray> handleWithListContent(String methodName, JsonObject content) {
        Promise<JsonArray> resultPromise = Promise.promise();
        DeliveryOptions options = getMethodOptions(methodName);
        eventBus.<JsonArray>request(address + ".selectableDates", content, options, handler -> {
            if (handler.succeeded()) {
                resultPromise.complete(handler.result().body());
            } else {
                handleFailure(resultPromise, handler);
            }
        });
        return resultPromise.future();
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
