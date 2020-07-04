import config.ConfigFilesManager;
import config.NestedConfigAccessor;
import handler.DefaultHandler;
import handler.Handler;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import model.*;
import parser.DefaultParseStrategy;

import java.util.Arrays;
import java.util.List;

public class OpenKlasterAPIVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(OpenKlasterAPIVerticle.class);
    private static final String listeningPortKey = "http.port";

    private ConfigRetriever configRetriever;
    private NestedConfigAccessor configAccessor;
    private Vertx vertx;
    private EventBus eventBus;
    private List<Handler> handlers;

    public OpenKlasterAPIVerticle(){
    }


    private void prepareDeploy(Promise<Void> deployPrepared){
        Promise<Void> promise = Promise.promise();
        createClusteredVertx(promise);;
        promise.future().onComplete(result ->{
            configRetriever = new ConfigFilesManager().getConfig(vertx);
            deployPrepared.complete();
        });
    }

    private void createClusteredVertx(Promise<Void> promise) {
        ClusterManager clusterManager = new HazelcastClusterManager();
        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);
        Vertx.clusteredVertx(options, result ->{
            if(result.succeeded()) {
                logger.info("Succeeded during launching clustered VertX");
                vertx = result.result();
                eventBus = vertx.eventBus();
                handlers = Arrays.asList(
                        new DefaultHandler("openKlaster.core.user",
                                "/user", eventBus, new DefaultParseStrategy<User>(User.class)),
                        new DefaultHandler("openKlaster.core.load",
                                "/load", eventBus, new DefaultParseStrategy<Load>(Load.class)),
                        new DefaultHandler("openKlaster.core.source",
                                "/source", eventBus, new DefaultParseStrategy<Source>(Source.class)),
                        new DefaultHandler("openKlaster.core.inverter",
                                "/inverter", eventBus, new DefaultParseStrategy<Inverter>(Inverter.class)),
                        new DefaultHandler("openKlaster.core.installation",
                                "/installation", eventBus, new DefaultParseStrategy<Installation>(Installation.class)),
                        new DefaultHandler("openKlaster.core.energySourceCalculator",
                                "/energySourceCalculator", eventBus,
                                new DefaultParseStrategy<EnergySourceCalculator>(EnergySourceCalculator.class)));
                promise.complete();

            }
            else
                handleClusteredVertxFailure();
        });

    }

    private void handleClusteredVertxFailure() {
    }


    @Override
    public void start(Promise<Void> promise){
        Promise<Void> deployPrepared = Promise.promise();
        prepareDeploy(deployPrepared);
        deployPrepared.future().onComplete(result -> {
            prepareConfig();
        });
    }

    private void prepareConfig(){
        configRetriever.getConfig(config ->{
            if(config.succeeded()){
                this.configAccessor = new NestedConfigAccessor(config.result());
                startVerticle();
            }else{
                logger.error(config.cause());
                vertx.close();
            }
        });
    }

    private void startVerticle(){
        Router router = Router.router(vertx);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(configAccessor.getInteger(listeningPortKey));
        routerConfig(router);
    }

    private void routerConfig(Router router){
        handlers.forEach(handler -> {
            router.route().handler(BodyHandler.create());
            router.post(handler.getRoute()).consumes("application/json").handler(handler::post);
            router.get(handler.getRoute()).handler(handler::get);
            router.put(handler.getRoute()).consumes("application/json").handler(handler::put);
            router.delete(handler.getRoute()).handler(handler::delete);
        });
    }
}
