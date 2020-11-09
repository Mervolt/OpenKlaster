package com.openklaster.filerepository.app;

import com.openklaster.common.verticle.OpenklasterVerticle;
import com.openklaster.filerepository.FileRepositoryVerticleConfig;
import com.openklaster.filerepository.properties.FileRepositoryProperties;
import com.openklaster.filerepository.service.ChartFileRepositoryHandler;
import com.openklaster.filerepository.service.FileRepositoryHandler;
import com.openklaster.filerepository.service.SelectableDates;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Arrays;
import java.util.List;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;


public class FileRepositoryVerticle extends OpenklasterVerticle {
    private final Logger logger = LoggerFactory.getLogger(FileRepositoryVerticle.class);
    private EventBus eventBus;
    private FileSystem vertxFileSystem;
    private GenericApplicationContext ctx;

    public FileRepositoryVerticle(boolean isDevMode) {
        super(isDevMode);
    }

    public FileRepositoryVerticle() {
        super();
    }

    @Override
    public void init(Vertx vertx, Context context) {
        ctx = new AnnotationConfigApplicationContext(FileRepositoryVerticleConfig.class);
        ctx.registerBean(FileSystem.class, () -> vertxFileSystem);
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.vertxFileSystem = vertx.fileSystem();
        List<FileRepositoryHandler<?>> handlers = prepareHandlers();
        eventBusConfig(handlers);
    }

    private List<FileRepositoryHandler<?>> prepareHandlers() {
        return Arrays.asList(
                ctx.getBean(SelectableDates.class),
                ctx.getBean(ChartFileRepositoryHandler.class));
    }

    private void eventBusConfig(List<FileRepositoryHandler<?>> handlers) {
        handlers.forEach(handler -> {
            MessageConsumer<JsonObject> consumer = eventBus.consumer(handler.getAddress());
            consumer.handler(message -> handlerMap(handler, message));
        });
    }

    private void handlerMap(FileRepositoryHandler<?> handler, Message<JsonObject> message) {
        System.out.println(message.headers());
        System.out.println(message.body());
        switch (message.headers().get(METHOD_KEY)) {
            case FileRepositoryProperties.GET_METHOD_NAME:
                handler.createGetHandler(message);
                break;
        }
    }
}
