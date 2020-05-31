import config.NestedConfigAccessor;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.config.ConfigRetriever;
import service.*;

import java.util.Arrays;
import java.util.List;


public class Verticle extends AbstractVerticle {
    private CassandraClientOptions options;
    private CassandraClient cassandraClient;
    private List<CassandraHandler> handlers;

    private ConfigRetriever configRetriever;
    private NestedConfigAccessor configAccessor;

    public Verticle(Vertx vertx, ConfigRetriever configRetriever) {
        this.vertx = vertx;
        this.configRetriever = configRetriever;
        //this.endpointConfigs = new ArrayList<>();
    }

    @Override
    public void start(Promise<Void> promise){
        configRetriever.getConfig(config ->{
            if(config.succeeded()){
                this.configAccessor = new NestedConfigAccessor(config.result());
                this.options = new CassandraClientOptions()
                        .setPort(configAccessor.getInteger("cassandra.port"))
                        .setKeyspace(configAccessor.getString("cassandra.keyspace"));
                this.cassandraClient = CassandraClient.create(vertx, options);

                startVerticle();
            }else{
                //logger.error(config.cause());

                vertx.close();
            }
        });
    }


    private void startVerticle(){
        Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .end("Ended...");
        });

        HttpServer server = vertx.createHttpServer()
                .requestHandler(router)
                .listen(configAccessor.getInteger("http.port"));
        routerConfig(router);
    }


    private void routerConfig(Router router) {
        this.handlers = Arrays.asList(
                new LoadMeasurementHandler(cassandraClient, configAccessor.getJsonObject("loadmeasurement")),
                new SourceMeasurementHandler(cassandraClient, configAccessor.getJsonObject("sourcemeasurement")),
                new EnergyPredictionsHandler(cassandraClient, configAccessor.getJsonObject("energypredictions")),
                new WeatherConditionsHandler(cassandraClient, configAccessor.getJsonObject("weatherconditions"))
        );

        handlers.forEach(handler -> {
            router.route(HttpMethod.POST, handler.getRoute()).handler(handler.createPostHandler());
            router.route(HttpMethod.GET, handler.getRoute()).handler(handler.createGetHandler());
        });
    }
}
