package com.openklaster.mongo.app;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.mongo.config.CalculatorConfig;
import com.openklaster.mongo.config.EntityConfig;
import com.openklaster.mongo.config.InstallationConfig;
import com.openklaster.mongo.config.UserConfig;
import com.openklaster.mongo.parser.EnergySourceCalculatorParser;
import com.openklaster.mongo.parser.InstallationParser;
import com.openklaster.mongo.parser.UserParser;
import com.openklaster.mongo.service.EntityHandler;
import com.openklaster.mongo.service.MongoPersistenceService;

import java.util.Arrays;
import java.util.List;

public class MongoVerticle extends AbstractVerticle {

    private MongoClient client;
    private MongoPersistenceService persistenceService;
    private List<EntityConfig> entityConfigs;
    private final EventBus eventBus;
    private final ConfigRetriever configRetriever;
    private static final Logger logger = LoggerFactory.getLogger(MongoVerticle.class);
    private NestedConfigAccessor configAccessor;



    public MongoVerticle(Vertx vertx, ConfigRetriever configRetriever) {
        this.configRetriever = configRetriever;
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
    }

    @Override
    public void start(Promise<Void> promise) {
        this.configRetriever.getConfig(config -> {
            if (config.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(config.result());
                handlePostConfig(promise);
            } else {
                logger.error("Could not retrieve app.MongoVerticle openklaster.common.openklaster.rest.config!");
                logger.error(config.cause());
                vertx.close();
            }
        });

    }


    private void handlePostConfig(Promise<Void> promise) {
        this.client = MongoClient.createShared(vertx, this.configAccessor.getJsonObject("database.mongo"));
        this.persistenceService = new MongoPersistenceService(client);

        this.entityConfigs = Arrays.asList(
                new CalculatorConfig(persistenceService, new EnergySourceCalculatorParser(),
                        configAccessor.getPathConfigAccessor("calculator")),
                new InstallationConfig(persistenceService, new InstallationParser(),
                        configAccessor.getPathConfigAccessor("installation")),
                new UserConfig(persistenceService, new UserParser(), configAccessor.getPathConfigAccessor("user"))
        );
        eventBusConfig();
        promise.complete();
    }

    private void eventBusConfig() {
        entityConfigs.forEach(config -> {
            MessageConsumer<JsonObject> consumer = eventBus.consumer(config.getBusAddress());
            consumer.handler(x -> handlerMap(config.getHandler(), x));
        });
    }

    private void handlerMap(EntityHandler handler, Message<JsonObject> msg) {
        switch (msg.headers().get("method")) {
            case "add":
                handler.add(msg);
                break;
            case "remove":
                handler.delete(msg);
                break;
            case "find":
                handler.findById(msg);
                break;
        }
    }

}
