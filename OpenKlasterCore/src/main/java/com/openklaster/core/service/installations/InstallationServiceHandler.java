package com.openklaster.core.service.installations;

import com.openklaster.core.service.EndpointService;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;

public class InstallationServiceHandler extends EndpointService {

    private final InstallationModelManager installationModelManager;

    public InstallationServiceHandler(InstallationModelManager installationModelManager, String busAddress) {
        super(busAddress);
        this.installationModelManager = installationModelManager;
    }

    @Override
    public void configureEndpoints(EventBus eventBus) {
        MessageConsumer<JsonObject> consumer = eventBus.consumer(getEventBusAddress());
        consumer.handler(this::handleMessage);
    }

    private void handleMessage(Message<JsonObject> message) {
        String methodName = message.headers().get(METHOD_KEY);
        installationModelManager.handleMessage(message,methodName);
    }
}
