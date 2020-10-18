package com.openklaster.mongo.app;

import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.verticle.OpenklasterVerticle;
import com.openklaster.mongo.VerticleConfig;
import com.openklaster.mongo.config.EntityConfig;
import com.openklaster.mongo.service.EntityHandler;
import com.openklaster.mongo.service.HandlerContainer;
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

public class MongoVerticle extends OpenklasterVerticle {

    private List<EntityConfig> entityConfigs;
    private EventBus eventBus;
    private static final Logger logger = LoggerFactory.getLogger(MongoVerticle.class);
    private GenericApplicationContext ctx;

    public MongoVerticle(boolean isDevModeOn) {
        super(isDevModeOn);
    }

    public MongoVerticle() {
        super();
    }

    @Override
    public void init(Vertx vertx, Context context) {
        ctx = new AnnotationConfigApplicationContext(VerticleConfig.class);
        ctx.registerBean(Vertx.class, () -> vertx);
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        ConfigFilesManager configFilesManager = new ConfigFilesManager(this.configFilenamePrefix);
        configFilesManager.getConfig(vertx).getConfig(config -> {
            if (config.succeeded()) {
                handlePostConfig();
            } else {
                logger.error("Could not retrieve app.MongoVerticle config!");
                logger.error(config.cause().getMessage());
                vertx.close();
            }
        });

    }

    private void handlePostConfig() {
        this.entityConfigs = ctx.getBean(HandlerContainer.class).retrieveHandler();
        eventBusConfig();
    }

    private void eventBusConfig() {
        entityConfigs.forEach(config -> {
            MessageConsumer<JsonObject> consumer = eventBus.consumer(config.getBusAddress());
            consumer.handler(x -> handlerMap(config.getHandler(), x));
        });
    }

    private void handlerMap(EntityHandler handler, Message<JsonObject> msg) {
        switch (msg.headers().get(METHOD_KEY)) {
            case "add":
                handler.add(msg);
                break;
            case "remove":
                handler.remove(msg);
                break;
            case "find":
                handler.findById(msg);
                break;
            case "findAll":
                handler.findAllByQuery(msg);
                break;
            case "update":
                handler.update(msg);
                break;
        }
    }

}
