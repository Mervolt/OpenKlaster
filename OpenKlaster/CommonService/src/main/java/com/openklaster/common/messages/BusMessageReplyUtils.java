package com.openklaster.common.messages;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class BusMessageReplyUtils {

    public static final String STATUS_CODE = "statusCode";
    public static final String METHOD_KEY = "method";

    public static <T> void replyWithBodyAndStatus(Message<T> busMessage, T replyBody, HttpResponseStatus status) {
        busMessage.reply(replyBody, getOptionsForStatus(status));
    }

    public static <T> void replyWithStatus(Message<T> busMessage, HttpResponseStatus status) {
        busMessage.reply(null, getOptionsForStatus(status));
    }

    private static DeliveryOptions getOptionsForStatus(HttpResponseStatus status) {
        return new DeliveryOptions().addHeader(STATUS_CODE, String.valueOf(status.code()));
    }

    public static <T> void replyWithError(Message<T> busMessage, HttpResponseStatus status, String reason) {
        busMessage.fail(status.code(), reason);
    }

    public static HttpResponseStatus statusFromMessage(Message<?> busMessage) {
        return statusFromString(busMessage.headers().get(STATUS_CODE));
    }

    public static HttpResponseStatus statusFromString(String status) {
        return HttpResponseStatus.parseLine(status);
    }
}
