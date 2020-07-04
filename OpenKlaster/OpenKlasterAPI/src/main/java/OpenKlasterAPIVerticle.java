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

    private static final String userEndpointRoute = "http.endpoint.route.user";
    private static final String loadEndpointRoute = "http.endpoint.route.load";
    private static final String sourceEndpointRoute = "http.endpoint.route.source";
    private static final String inverterEndpointRoute = "http.endpoint.route.inverter";
    private static final String installationEndpointRoute = "http.endpoint.route.installation";
    private static final String energyEndpointRoute = "http.endpoint.route.energySourceCalculator";

    private static final String userCoreRoute = "core.route.user";
    private static final String loadCoreRoute = "core.route.load";
    private static final String sourceCoreRoute = "core.route.source";
    private static final String inverterCoreRoute = "core.route.inverter";
    private static final String installationCoreRoute = "core.route.installation";
    private static final String energyCoreRoute = "core.route.energySourceCalculator";


    private ConfigRetriever configRetriever;
    private NestedConfigAccessor configAccessor;
    private Vertx vertx;
    private EventBus eventBus;
    private List<Handler> handlers;

    public OpenKlasterAPIVerticle(){
    }

    @Override
    public void start(Promise<Void> promise){
        Promise<Void> deployPrepared = Promise.promise();
        prepareDeploy(deployPrepared);
        deployPrepared.future().onComplete(result -> {
            prepareConfig();
        });
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
                promise.complete();

            }
            else
                handleClusteredVertxFailure();
        });

    }

    private void handleClusteredVertxFailure() {
        logger.error("Succeeded during launching clustered VertX");
    }

    private void prepareConfig(){
        configRetriever.getConfig(config ->{
            if(config.succeeded()){
                this.configAccessor = new NestedConfigAccessor(config.result());
                startVerticle();
            }
            else{
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
        handlers = Arrays.asList(
                new DefaultHandler(configAccessor.getString(userEndpointRoute), configAccessor.getString(userCoreRoute),
                        eventBus, configAccessor, new DefaultParseStrategy<User>(User.class)),

                new DefaultHandler(configAccessor.getString(loadEndpointRoute), configAccessor.getString(loadCoreRoute),
                        eventBus, configAccessor, new DefaultParseStrategy<Load>(Load.class)),

                new DefaultHandler(configAccessor.getString(sourceEndpointRoute),
                        configAccessor.getString(sourceCoreRoute), eventBus, configAccessor,
                        new DefaultParseStrategy<Source>(Source.class)),

                new DefaultHandler(configAccessor.getString(inverterEndpointRoute),
                        configAccessor.getString(inverterCoreRoute), eventBus, configAccessor,
                        new DefaultParseStrategy<Inverter>(Inverter.class)),

                new DefaultHandler(configAccessor.getString(installationEndpointRoute),
                        configAccessor.getString(installationCoreRoute), eventBus, configAccessor,
                        new DefaultParseStrategy<Installation>(Installation.class)),

                new DefaultHandler(configAccessor.getString(energyEndpointRoute),
                        configAccessor.getString(energyCoreRoute), eventBus, configAccessor,
                        new DefaultParseStrategy<EnergySourceCalculator>(EnergySourceCalculator.class)));

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
