package com.openklaster.core.service;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;

public abstract class EndpointService {

    private final static int noMethodErrorCode = 0;
    protected static Logger logger;
    private final String eventBusAddress;

    public String getEventBusAddress() {
        return eventBusAddress;
    }

    public EndpointService(String eventBusAddress) {
        this.eventBusAddress = eventBusAddress;
    }

    public abstract void configureEndpoints(EventBus eventBus);

    protected void handleUnknownMethod(Message<JsonObject> message, String methodName) {
        message.fail(noMethodErrorCode, String.format("No method %s found for %s", methodName, getEventBusAddress()));
    }
}
