package com.openklaster.core.vertx.service;

import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;

public abstract class EndpointService {

    protected final static String busAddressKey = "address";
    protected final static String methodHeaderKey = "method";
    private final static int noMethodErrorCode = 0;
    protected static Logger logger;
    protected NestedConfigAccessor config;

    public String getEventBusAddress() {
        return config.getString(busAddressKey);
    }

    public EndpointService(NestedConfigAccessor config) {
        this.config = config;
    }

    public abstract void configureEndpoints(EventBus eventBus);

    protected void handleUnknownMethod(Message<JsonObject> message, String methodName) {
        message.fail(noMethodErrorCode, String.format("No method %s found for %s", methodName, getEventBusAddress()));
    }
}
