package com.openklaster.common.tests.bus;

import io.vertx.core.eventbus.DeliveryOptions;
import lombok.ToString;

@ToString
public class SuccessfulFakeReply implements FakeReply {
    @Override
    public boolean succeeded() {
        return true;
    }
    @ToString.Include
    private final Object body;
    @ToString.Include
    private final DeliveryOptions deliveryOptions;

    public SuccessfulFakeReply(Object body, DeliveryOptions deliveryOptions) {
        this.body = body;
        this.deliveryOptions = deliveryOptions;
    }

    @Override
    public Object body() {
        return body;
    }

    @Override
    public DeliveryOptions deliveryOptions() {
        return deliveryOptions;
    }

    @Override
    public String cause() {
        return null;
    }

    @Override
    public int errorCode() {
        return -1;
    }
}
