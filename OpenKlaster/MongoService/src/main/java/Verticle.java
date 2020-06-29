import config.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import parser.*;
import service.MongoPersistenceService;

import java.util.Arrays;
import java.util.List;

public class Verticle extends AbstractVerticle {

    private final DbConfig dbConfig;
    private final MongoClient client;
    private final MongoPersistenceService persistenceService;
    private final List<EntityConfig> entityConfigs;

    public Verticle(Vertx vertx) {
        this.vertx = vertx;
        this.dbConfig = new DbConfig();
        this.client = MongoClient.createShared(vertx, dbConfig.getMongoConfig());
        this.persistenceService = new MongoPersistenceService(client);
        this.entityConfigs = Arrays.asList(
                new CalculatorConfig(persistenceService, new EnergySourceCalculatorParser()),
                new InstallationConfig(persistenceService, new InstallationParser()),
                new InverterConfig(persistenceService, new InverterParsers()),
                new LoadConfig(persistenceService, new LoadParsers()),
                new SourceConfig(persistenceService, new SourceParsers()),
                new UserConfig(persistenceService, new UserParser())
        );
    }

    @Override
    public void start(Promise<Void> promise) {
        Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .end("Ended...");
        });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8081);
        routerConfig(router);

    }

    private void routerConfig(Router router) {
        entityConfigs.forEach(config -> {
            router.route(config.getRoute()).handler(BodyHandler.create());
            router.post(config.getRoute()).consumes("application/json").handler(config.getHandler()::add);
            router.get(config.getRoute()).consumes("application/json").handler(config.getHandler()::findById);
            router.delete(config.getRoute()).consumes("application/json").handler(config.getHandler()::delete);
        });
    }


}
