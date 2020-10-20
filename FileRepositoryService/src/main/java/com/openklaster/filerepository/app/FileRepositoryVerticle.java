package com.openklaster.filerepository.app;

import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.verticle.OpenklasterVerticle;
import com.openklaster.filerepository.properties.FileRepositoryProperties;
import com.openklaster.filerepository.service.ChartFileRepositoryHandler;
import com.openklaster.filerepository.service.FileRepositoryHandler;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;


public class FileRepositoryVerticle extends OpenklasterVerticle {
    private NestedConfigAccessor configAccessor;
    private final Logger logger = LoggerFactory.getLogger(FileRepositoryVerticle.class);
    private EventBus eventBus;
    private FileSystem vertxFileSystem;

    public FileRepositoryVerticle(boolean isDevMode) {
        super(isDevMode);
    }

    public FileRepositoryVerticle() {
        super();
    }

    @Override
    public void init(Vertx vertx, Context context) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.vertxFileSystem = vertx.fileSystem();

        ConfigFilesManager configFilesManager = new ConfigFilesManager(this.configFilenamePrefix);
        configFilesManager.getConfig(vertx).getConfig(config -> {
            if (config.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(config.result());

                List<FileRepositoryHandler<?>> handlers = prepareHandlers();
                eventBusConfig(handlers);
            } else {
                logger.error("Could not retrieve com.openklaster.filerepository.app.FileRepositoryVerticle config!");
                logger.error(config.cause().getMessage());
                vertx.close();
            }
        });

    }

    private List<FileRepositoryHandler<?>> prepareHandlers() {
        return Arrays.asList(
                new ChartFileRepositoryHandler(vertxFileSystem, configAccessor.getPathConfigAccessor(FileRepositoryProperties.CHART_FILE_REPOSITORY))
        );
    }

    private void eventBusConfig(List<FileRepositoryHandler<?>> handlers) {
        handlers.forEach(config -> {
            MessageConsumer<JsonObject> consumer = eventBus.consumer(config.getAddress());
            consumer.handler(message -> handlerMap(config, message));
        });
    }

    private void handlerMap(FileRepositoryHandler<?> handler, Message<JsonObject> message) {
        switch (message.headers().get(METHOD_KEY)) {
            case FileRepositoryProperties.GET_METHOD_NAME:
                handler.createGetHandler(message);
                break;
        }
    }
}
