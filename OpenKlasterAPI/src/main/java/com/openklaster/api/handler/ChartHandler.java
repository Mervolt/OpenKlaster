package com.openklaster.api.handler;

import com.openklaster.api.handler.properties.HandlerProperties;
import com.openklaster.api.handler.summary.SummaryCreator;
import com.openklaster.api.model.Model;
import com.openklaster.api.model.summary.SummaryResponse;
import com.openklaster.api.parser.IParseStrategy;
import com.openklaster.api.validation.ValidationException;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.multipart.MultipartForm;

import java.util.Base64;
import java.util.Map;

import static com.openklaster.api.validation.ValidationExecutor.validate;

public class ChartHandler extends Handler {
    private final FileSystem vertxFileSystem;

    public ChartHandler(String route, String address, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy, FileSystem vertxFileSystem) {
        super(HandlerProperties.getMethodHeader, route, HandlerProperties.getMethodHeader, address, eventBus, nestedConfigAccessor, parseStrategy);
        this.vertxFileSystem = vertxFileSystem;
    }

    @Override
    public void handle(RoutingContext context) {
        vertxFileSystem.readFile("cat.jpg", ar -> {
            if (ar.succeeded()) {
                byte[] content = ar.result().getBytes();
                String encodeBase64 = Base64.getEncoder().encodeToString(content);
                context.response().end(Json.encode("data:image/jpg;base64, " + encodeBase64));
            } else {
                System.out.println("costam");
            }
        });

    }
}
