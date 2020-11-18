package com.openklaster.api.handler;

import com.openklaster.common.messages.HttpReplyUtils;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CredentialsApiHandler implements ApiHandler {

    private final String route;
    private final Map<String, List<String>> manufacturersCredentialsMap;

    public CredentialsApiHandler(String route, JsonObject manufacturersCredentials) {
        this.route = route;
        this.manufacturersCredentialsMap = new HashMap<>();
        manufacturersCredentials.fieldNames().forEach(manufacturerName -> addManufacturer(manufacturerName,
                manufacturersCredentials.getJsonArray(manufacturerName).getList()));
    }

    void addManufacturer(String manufacturerName, List<String> credentials) {
        this.manufacturersCredentialsMap.put(manufacturerName, credentials);
    }

    @Override
    public void handle(RoutingContext context, EventBus eventBus) {
        HttpReplyUtils.sendJsonResponse(context.response(), JsonObject.mapFrom(manufacturersCredentialsMap));
    }

    @Override
    public void configure(Router router, EventBus eventBus) {
        router.get(this.route).handler(context -> handle(context, eventBus));
    }
}
