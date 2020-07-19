package com.openklaster.core.vertx.service.measurements;

import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.core.vertx.service.EndpointService;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;

public class MeasurementServiceHandler<T> extends EndpointService {

    private final MeasurementManager<T> measurementManager;

    public MeasurementServiceHandler(NestedConfigAccessor config,
                                     MeasurementManager<T> measurementManager) {
        super(config);
        this.measurementManager = measurementManager;
    }

    @Override
    public void configureEndpoints(EventBus eventBus) {
        MessageConsumer<JsonObject> consumer = eventBus.consumer(getEventBusAddress());
        consumer.handler(this::handleMessage);
    }

    private void handleMessage(Message<JsonObject> message) {
        String methodName = message.headers().get(METHOD_KEY);
        measurementManager.handleMessage(message, methodName);
    }
}
