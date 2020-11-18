package com.openklaster.core.service.filerepository;

import com.openklaster.core.service.EndpointService;
import com.openklaster.core.service.measurements.MeasurementManager;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;

public class FileRepositoryServiceHandler extends EndpointService {

    private final FileRepositoryManager fileRepositoryManager;

    public FileRepositoryServiceHandler(FileRepositoryManager fileRepositoryManager, String busAddress) {
        super(busAddress);
        this.fileRepositoryManager = fileRepositoryManager;
    }

    @Override
    public void configureEndpoints(EventBus eventBus) {
        MessageConsumer<JsonObject> consumer = eventBus.consumer(getEventBusAddress());
        consumer.handler(this::handleMessage);
    }

    private void handleMessage(Message<JsonObject> message) {
        String methodName = message.headers().get(METHOD_KEY);
        fileRepositoryManager.handleMessage(message, methodName);
    }
}
