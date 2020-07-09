package com.openklaster.api.app;


import com.openklaster.api.handler.DefaultHandler;
import com.openklaster.api.handler.Handler;
import com.openklaster.api.model.*;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.api.parser.DefaultParseStrategy;
import com.openklaster.api.properties.EndpointRouteProperties;
import com.openklaster.api.properties.EventBusAddressProperties;

import java.util.Arrays;
import java.util.List;

public class OpenKlasterAPIVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(OpenKlasterAPIVerticle.class);

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
            promise.complete();
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
        logger.error("Failure during launching clustered VertX");
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
                .listen(configAccessor.getInteger(EndpointRouteProperties.listeningPortKey));
        handlers = Arrays.asList(
                new DefaultHandler(configAccessor.getString(EndpointRouteProperties.userCoreAddressKey),
                        configAccessor.getString(EventBusAddressProperties.userEndpointRouteKey),
                        eventBus, configAccessor, new DefaultParseStrategy<User>(User.class)),

                new DefaultHandler(configAccessor.getString(EndpointRouteProperties.loadCoreAddressKey),
                        configAccessor.getString(EventBusAddressProperties.loadEndpointRouteKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Load>(Load.class)),

                new DefaultHandler(configAccessor.getString(EndpointRouteProperties.sourceCoreAddressKey),
                        configAccessor.getString(EventBusAddressProperties.sourceEndpointRouteKey), eventBus, configAccessor,
                        new DefaultParseStrategy<Source>(Source.class)),

                new DefaultHandler(configAccessor.getString(EndpointRouteProperties.inverterCoreAddressKey),
                        configAccessor.getString(EventBusAddressProperties.inverterEndpointRouteKey), eventBus, configAccessor,
                        new DefaultParseStrategy<Inverter>(Inverter.class)),

                new DefaultHandler(configAccessor.getString(EndpointRouteProperties.installationCoreAddressKey),
                        configAccessor.getString(EventBusAddressProperties.installationEndpointRouteKey), eventBus, configAccessor,
                        new DefaultParseStrategy<Installation>(Installation.class)),

                new DefaultHandler(configAccessor.getString(EndpointRouteProperties.energyCoreAddressKey),
                        configAccessor.getString(EventBusAddressProperties.energyEndpointRouteKey), eventBus, configAccessor,
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