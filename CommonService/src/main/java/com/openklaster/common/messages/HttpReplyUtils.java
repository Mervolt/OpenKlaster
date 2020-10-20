package com.openklaster.common.messages;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;

public class HttpReplyUtils {

    public static final String successfulRequestMessage = "Successful request";

    public static void sendJsonResponse(HttpServerResponse serverResponse, Object content) {
        serverResponse.putHeader("Content-Type", "application/json")
                .end(Json.encodePrettily(content));
    }

    public static void sendOkEmptyResponse(HttpServerResponse serverResponse) {
        serverResponse.setStatusCode(HttpResponseStatus.OK.code())
                .end(successfulRequestMessage);
    }

    public static void sendFailureResponse(HttpServerResponse serverResponse, final int code, final String message){
        serverResponse.setStatusCode(code);
        serverResponse.end(message);
    }
}
