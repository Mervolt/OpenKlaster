package openklaster.rest;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;

import openklaster.common.config.NestedConfigAccessor;
import openklaster.rest.config.EndpointConfig;
import openklaster.rest.config.EnergyMeasurementConfig;
import openklaster.rest.config.PowerMeasurementConfig;
import openklaster.rest.handler.EnergyMeasurementHandler;
import openklaster.rest.handler.PowerMeasurementHandler;

import java.util.ArrayList;
import java.util.List;

public class ApiVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ApiVerticle.class);
    private static final String listeningPortKey = "http.port";

    private final List<EndpointConfig> endpointConfigs;
    private final ConfigRetriever configRetriever;
    private NestedConfigAccessor configAccessor;

    public ApiVerticle(Vertx vertx, ConfigRetriever configRetriever){
        this.configRetriever = configRetriever;
        this.vertx =vertx;
        this.endpointConfigs = new ArrayList<>();
    }

    @Override
    public void start(Promise<Void> promise){
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
                .listen(configAccessor.getInteger("http.port"));
        routerConfig(router);
    }

    private void routerConfig(Router router){
        endpointConfigs.add(new EnergyMeasurementConfig(configAccessor.getJsonObject("energy.endpoint"), new EnergyMeasurementHandler()));
        //WebClient waits for DI, now I'm creating new :C
        endpointConfigs.add(new PowerMeasurementConfig(configAccessor.getJsonObject("power.endpoint"),
                new PowerMeasurementHandler(WebClient.create(vertx),configAccessor.getRootConfig())));

        for(EndpointConfig config : endpointConfigs){
            config.configureRouter(router);
        }
    }
}
