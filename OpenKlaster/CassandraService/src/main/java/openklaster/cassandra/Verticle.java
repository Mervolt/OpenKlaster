package openklaster.cassandra;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import openklaster.cassandra.service.*;

import java.util.Arrays;
import java.util.List;


public class Verticle extends AbstractVerticle {
    CassandraClientOptions options;
    CassandraClient cassandraClient;
    private final List<CassandraHandler> handlers;

    public Verticle(Vertx vertx) {
        this.vertx = vertx;
        this.options = new CassandraClientOptions();
        this.cassandraClient = CassandraClient.create(vertx, options);
        this.handlers = Arrays.asList(
                new LoadMeasurementHandler(cassandraClient),
                new SourceMeasurementHandler(cassandraClient),
                new EnergyPredictionsHandler(cassandraClient),
                new WeatherConditionsHandler(cassandraClient)
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

        HttpServer server = vertx.createHttpServer()
                .requestHandler(router)
                .listen(8191);
        routerConfig(router);

    }

    private void routerConfig(Router router) {
        handlers.forEach(handler -> {
            router.route(HttpMethod.POST, handler.getRoute()).handler(handler.postHandler());
            router.route(HttpMethod.GET, handler.getRoute()).handler(handler.getHandler());
        });
    }
}
