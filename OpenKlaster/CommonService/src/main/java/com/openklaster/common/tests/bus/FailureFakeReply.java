package com.openklaster.common.tests.bus;

import io.vertx.core.eventbus.DeliveryOptions;

public class FailureFakeReply implements FakeReply {

    private final String cause;
    private final int errorCode;

    public FailureFakeReply(String cause, int errorCode) {
        this.cause = cause;
        this.errorCode = errorCode;
    }

    @Override
    public boolean succeeded() {
        return false;
    }

    @Override
    public Object body() {
        return null;
    }

    @Override
    public DeliveryOptions deliveryOptions() {
        return null;
    }

    @Override
    public String cause() {
        return cause;
    }

    @Override
    public int errorCode() {
        return errorCode;
    }
}
