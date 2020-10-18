package com.openklaster.cassandra.app;

import com.openklaster.cassandra.VerticleConfig;
import com.openklaster.cassandra.properties.CassandraProperties;
import com.openklaster.cassandra.service.CassandraHandler;
import com.openklaster.cassandra.service.HandlerContainer;
import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.verticle.OpenklasterVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;


public class CassandraVerticle extends OpenklasterVerticle {
    private final Logger logger = LoggerFactory.getLogger(CassandraVerticle.class);
    private EventBus eventBus;
    GenericApplicationContext ctx;

    public CassandraVerticle(boolean isDevMode) {
        super(isDevMode);
    }

    public CassandraVerticle() {
        super();
    }

    @Override
    public void init(Vertx vertx, Context context) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        ctx = new AnnotationConfigApplicationContext(VerticleConfig.class);
        ctx.registerBean(Vertx.class, () -> vertx);

        ConfigFilesManager configFilesManager = new ConfigFilesManager(this.configFilenamePrefix);
        configFilesManager.getConfig(vertx).getConfig(config -> {
            if (config.succeeded()) {

                List<CassandraHandler<?>> handlers = prepareHandlers();
                eventBusConfig(handlers);
            } else {
                logger.error("Could not retrieve com.openklaster.cassandra.app.CassandraVerticle config!");
                logger.error(config.cause().getMessage());
                vertx.close();
            }
        });

    }

    private List<CassandraHandler<?>> prepareHandlers() {
        return ctx.getBean(HandlerContainer.class).retrieveHandlers();
    }

    private void eventBusConfig(List<CassandraHandler<?>> handlers) {
        handlers.forEach(config -> {
            MessageConsumer<JsonObject> consumer = eventBus.consumer(config.getAddress());
            consumer.handler(message -> handlerMap(config, message));
        });
    }

    private void handlerMap(CassandraHandler<?> handler, Message<JsonObject> message) {
        switch (message.headers().get(METHOD_KEY)) {
            case CassandraProperties.GET:
                handler.createGetHandler(message);
                break;
            case CassandraProperties.POST:
                handler.createPostHandler(message);
                break;
        }
    }
}
