package com.openklaster.mongo.app;

import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.verticle.OpenklasterVerticle;
import com.openklaster.mongo.config.CalculatorConfig;
import com.openklaster.mongo.config.EntityConfig;
import com.openklaster.mongo.config.InstallationConfig;
import com.openklaster.mongo.config.UserConfig;
import com.openklaster.mongo.parser.EnergySourceCalculatorParser;
import com.openklaster.mongo.parser.InstallationParser;
import com.openklaster.mongo.parser.UserParser;
import com.openklaster.mongo.service.EntityHandler;
import com.openklaster.mongo.service.MongoPersistenceService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

import java.util.Arrays;
import java.util.List;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;

public class MongoVerticle extends OpenklasterVerticle {

    private MongoClient client;
    private MongoPersistenceService persistenceService;
    private List<EntityConfig> entityConfigs;
    private EventBus eventBus;
    private static final Logger logger = LoggerFactory.getLogger(MongoVerticle.class);
    private NestedConfigAccessor configAccessor;

    public MongoVerticle(boolean isDevModeOn) {
        super(isDevModeOn);
    }

    public MongoVerticle() {
        super();
    }

    @Override
    public void init(Vertx vertx, Context context) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        ConfigFilesManager configFilesManager = new ConfigFilesManager(this.configFilenamePrefix);
        configFilesManager.getConfig(vertx).getConfig(config -> {
            if (config.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(config.result());
                handlePostConfig();
            } else {
                logger.error("Could not retrieve app.MongoVerticle config!");
                logger.error(config.cause().getMessage());
                vertx.close();
            }
        });

    }

    private void handlePostConfig() {
        JsonObject mongoOptions = this.configAccessor.getJsonObject("database.mongo");
        this.client = MongoClient.createShared(vertx, mongoOptions);
        this.persistenceService = new MongoPersistenceService(client);

        this.entityConfigs = Arrays.asList(
                new CalculatorConfig(persistenceService, new EnergySourceCalculatorParser(),
                        configAccessor.getPathConfigAccessor("calculator")),
                new InstallationConfig(persistenceService, new InstallationParser(),
                        configAccessor.getPathConfigAccessor("installation")),
                new UserConfig(persistenceService, new UserParser(), configAccessor.getPathConfigAccessor("user"))
        );
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
