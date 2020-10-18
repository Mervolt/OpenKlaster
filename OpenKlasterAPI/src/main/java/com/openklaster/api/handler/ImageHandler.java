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

public class ImageHandler extends CoreCommunicationHandler {
    private final FileSystem vertxFileSystem;

    public ImageHandler(String route, String address, EventBus eventBus, NestedConfigAccessor nestedConfigAccessor, IParseStrategy<? extends Model> parseStrategy, FileSystem vertxFileSystem) {
        super(HandlerProperties.getMethodHeader, route, HandlerProperties.getMethodHeader, address, eventBus, nestedConfigAccessor, parseStrategy);
        this.vertxFileSystem = vertxFileSystem;
        System.out.println(route);
    }

    @Override
    public void handle(RoutingContext context) {
        System.out.println("test");
        vertxFileSystem.readFile("C:\\Users\\Rafał\\IdeaProjects\\OpenKlaster\\OpenKlasterAPI\\src\\main\\resources\\cat.jpg", ar -> {
            if (ar.succeeded()) {
                byte[] content = ar.result().getBytes();
                String encodeBase64 = Base64.getEncoder().encodeToString(content);
                System.out.println(content);
                context.response().end(encodeBase64);
            } else {
                System.out.println("asdasd");
            }
        });

    }
}
