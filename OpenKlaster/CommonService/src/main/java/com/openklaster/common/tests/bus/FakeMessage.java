package com.openklaster.common.tests.bus;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import lombok.Builder;
import lombok.Getter;

public class FakeMessage<T> implements Message<T> {

    private final static String unsupportedOperationMsg = "This is fake message, not used in async operations";
    private final static String address = "fakeAdress";

    private final MultiMap headers;
    private final T body;
    private final boolean isSend;

    @Builder
    public FakeMessage(MultiMap headers, T body, boolean isSend) {
        this.headers = headers;
        this.body = body;
        this.isSend = isSend;
    }

    @Getter
    private FakeReply messageReply;


    @Override
    public String address() {
        return address;
    }

    @Override
    public MultiMap headers() {
        return headers;
    }

    @Override
    public T body() {
        return body;
    }

    @Override
    public String replyAddress() {
        return address;
    }

    @Override
    public boolean isSend() {
        return isSend;
    }

    @Override
    public void reply(Object replyContent) {
        this.messageReply = successReply(replyContent, null);
    }

    @Override
    public <R> void reply(Object replyContent, Handler<AsyncResult<Message<R>>> handler) {
        throw new UnsupportedOperationException(unsupportedOperationMsg);
    }

    @Override
    public void reply(Object replyContent, DeliveryOptions deliveryOptions) {
        this.messageReply = successReply(replyContent, deliveryOptions);
    }

    @Override
    public <R> void reply(Object o, DeliveryOptions deliveryOptions, Handler<AsyncResult<Message<R>>> handler) {
        throw new UnsupportedOperationException(unsupportedOperationMsg);
    }

    @Override
    public <R> void replyAndRequest(Object message, Handler<AsyncResult<Message<R>>> replyHandler) {
        throw new UnsupportedOperationException(unsupportedOperationMsg);
    }

    @Override
    public <R> void replyAndRequest(Object message, DeliveryOptions options, Handler<AsyncResult<Message<R>>> replyHandler) {
        throw new UnsupportedOperationException(unsupportedOperationMsg);
    }

    @Override
    public void fail(int errorCode, String cause) {
        this.messageReply = failReply(errorCode, cause);
    }

    private FakeReply failReply(int errorCode, String cause) {
        return new FailureFakeReply(cause, errorCode);
    }

    private FakeReply successReply(Object replyContent, DeliveryOptions deliveryOptions) {
        return new SuccessfulFakeReply(replyContent, deliveryOptions);
    }
}
