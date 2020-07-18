package com.openklaster.api.app;


import com.openklaster.api.handler.*;
import com.openklaster.api.handler.Handler;
import com.openklaster.api.model.*;
import com.openklaster.api.properties.EndpointRouteProperties;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.api.parser.DefaultParseStrategy;
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
        createClusteredVertx(promise);
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
                new PostAndReturnJsonHandler(configAccessor.getString(EndpointRouteProperties.loginEndpoint),
                        configAccessor.getString(EventBusAddressProperties.loginCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Login>(Login.class)),

                new PostHandler(configAccessor.getString(EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Register>(Register.class)),

                new PutHandler(configAccessor.getString(EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<UpdateUser>(UpdateUser.class)),

                new GetHandler(configAccessor.getString(EndpointRouteProperties.userEndpoint),
                        configAccessor.getString(EventBusAddressProperties.userCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Login>(Login.class)),

                new PostAndReturnJsonHandler(configAccessor.getString(EndpointRouteProperties.tokenEndpoint),
                        configAccessor.getString(EventBusAddressProperties.tokenCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Login>(Login.class)),

                new DeleteHandler(configAccessor.getString(EndpointRouteProperties.tokenEndpoint),
                        configAccessor.getString(EventBusAddressProperties.tokenCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<TokenId>(TokenId.class)),

                new GetHandler(configAccessor.getString(EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<InstallationRequest>(InstallationRequest.class)),

                new PostHandler(configAccessor.getString(EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Installation>(Installation.class)),

                new PutHandler(configAccessor.getString(EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Installation>(Installation.class)),

                new DeleteHandler(configAccessor.getString(EndpointRouteProperties.installationEndpoint),
                        configAccessor.getString(EventBusAddressProperties.installationCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<InstallationRequest>(InstallationRequest.class)),

                new GetHandler(configAccessor.getString(EndpointRouteProperties.powerconsumptionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.powerconsumptionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementRequest>(MeasurementRequest.class)),

                new PostHandler(configAccessor.getString(EndpointRouteProperties.powerconsumptionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.powerconsumptionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Measurement>(Measurement.class)),

                new GetHandler(configAccessor.getString(EndpointRouteProperties.powerproductionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.powerproductionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementRequest>(MeasurementRequest.class)),

                new PostHandler(configAccessor.getString(EndpointRouteProperties.powerproductionEndpoint),
                        configAccessor.getString(EventBusAddressProperties.powerconsumptionCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Measurement>(Measurement.class)),

                new GetHandler(configAccessor.getString(EndpointRouteProperties.energyconsumedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.energyconsumedCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementRequest>(MeasurementRequest.class)),

                new PostHandler(configAccessor.getString(EndpointRouteProperties.energyconsumedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.energyconsumedCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Measurement>(Measurement.class)),

                new GetHandler(configAccessor.getString(EndpointRouteProperties.energyproducedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.energyproducedCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<MeasurementRequest>(MeasurementRequest.class)),

                new PostHandler(configAccessor.getString(EndpointRouteProperties.energyproducedEndpoint),
                        configAccessor.getString(EventBusAddressProperties.energyproducedCoreAddressKey),
                        eventBus, configAccessor, new DefaultParseStrategy<Measurement>(Measurement.class))
        );



        routerConfig(router);
    }

    private void routerConfig(Router router){
        handlers.forEach(handler -> {
            router.route().handler(BodyHandler.create());
            switch (handler.getMethod()) {
                case "get":
                    router.get(handler.getRoute()).handler(handler::handle);
                    break;
                case "post":
                    router.post(handler.getRoute()).consumes("application/json").handler(handler::handle);
                    break;
                case "put":
                    router.put(handler.getRoute()).consumes("application/json").handler(handler::handle);
                    break;
                case "delete":
                    router.delete(handler.getRoute()).handler(handler::handle);
                    break;
            }
        });
    }
}
