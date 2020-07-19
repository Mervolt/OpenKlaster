package com.openklaster.common.tests.bus;

import io.vertx.core.eventbus.DeliveryOptions;

public interface FakeReply {

    boolean succeeded();

    Object body();
    DeliveryOptions deliveryOptions();

    String cause();
    int errorCode();
}
