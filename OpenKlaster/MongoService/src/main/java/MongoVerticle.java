import config.*;
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
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import model.Installation;
import parser.*;
import service.EntityHandler;
import service.MongoPersistenceService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                handlePostConfig();
            } else {
                logger.error("Could not retrieve MongoVerticle config!");
                logger.error(config.cause());
                vertx.close();
            }
        });
    }

    private void handlePostConfig() {
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
