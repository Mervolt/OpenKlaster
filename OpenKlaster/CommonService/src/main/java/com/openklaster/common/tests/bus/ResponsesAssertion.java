package com.openklaster.common.tests.bus;

import com.openklaster.common.messages.BusMessageReplyUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;

import static org.junit.Assert.*;

public class ResponsesAssertion {

    public static void assertBusResult(HttpResponseStatus expectedStatus, JsonObject expectedBody,
                                       AsyncResult<Message<JsonObject>> result) {
        assertTrue(result.succeeded());
        assertResponse(expectedStatus, expectedBody, result.result());
    }

    public static void assertBusFail(HttpResponseStatus expectedStatus,AsyncResult<Message<JsonObject>> result ){
        assertBusFail(expectedStatus,null,result);
    }
    public static void assertBusFail(String expectedMessage, AsyncResult<Message<JsonObject>> result){
        assertBusFail(null,expectedMessage,result);
    }

    public static void assertBusFail(HttpResponseStatus expectedStatus, String expectedMessage,
                                     AsyncResult<Message<JsonObject>> result ){
        assertTrue(result.failed());
        if(result.cause() instanceof ReplyException){
            ReplyException exception = (ReplyException) result.cause();

            if(expectedMessage != null){
                assertEquals(expectedMessage,exception.getMessage());
            }
            if( expectedStatus != null){
                assertEquals(expectedStatus.code(),exception.failureCode());
            }
        }else {
            fail(String.format("Result exception %s, was not a ReplyException type!", result.cause().toString()));
        }
    }

    public static void assertResponse(HttpResponseStatus expectedStatus, JsonObject expectedBody,
                                      Message<JsonObject> actualMessage) {
        String actualStatusString = actualMessage.headers().get(BusMessageReplyUtils.STATUS_CODE);
        JsonObject actualBody = actualMessage.body();

        assertNotNull(actualStatusString);
        assertEquals(expectedStatus, BusMessageReplyUtils.statusFromString(actualStatusString));
        assertEquals(expectedBody, actualBody);
    }
}
