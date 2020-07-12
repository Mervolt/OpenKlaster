package com.openklaster.cassandra.app;

import com.openklaster.cassandra.service.*;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import com.openklaster.common.config.NestedConfigAccessor;

import java.util.Arrays;
import java.util.List;


public class CassandraVerticle extends AbstractVerticle {
    private CassandraClientOptions options;
    private CassandraClient cassandraClient;
    private List<CassandraHandler> handlers;

    private ConfigRetriever configRetriever;
    private NestedConfigAccessor configAccessor;
    private static final Logger logger = LoggerFactory.getLogger(CassandraVerticle.class);
    private final EventBus eventBus;

    public CassandraVerticle(Vertx vertx, ConfigRetriever configRetriever) {
        this.vertx = vertx;
        this.configRetriever = configRetriever;
        this.eventBus = vertx.eventBus();
    }

    @Override
    public void start(Promise<Void> promise) {
        this.configRetriever.getConfig(config -> {
            if (config.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(config.result());
                this.options = new CassandraClientOptions()
                        .setPort(configAccessor.getInteger("cassandra.port"))
                        .setKeyspace(configAccessor.getString("cassandra.keyspace"));
                this.cassandraClient = CassandraClient.create(vertx, options);

                this.handlers = Arrays.asList(
                        new LoadMeasurementHandler(cassandraClient, configAccessor.getJsonObject("loadmeasurement")),
                        new SourceMeasurementHandler(cassandraClient, configAccessor.getJsonObject("sourcemeasurement")),
                        new EnergyPredictionsHandler(cassandraClient, configAccessor.getJsonObject("energypredictions")),
                        new WeatherConditionsHandler(cassandraClient, configAccessor.getJsonObject("weatherconditions"))
                );
                eventBusConfig();
            } else {
                logger.error("Could not retrieve com.openklaster.cassandra.app.CassandraVerticle config!");
                logger.error(config.cause());
                vertx.close();
            }
        });
    }


    private void eventBusConfig() {
        handlers.forEach(config -> {
            MessageConsumer<JsonObject> consumer = eventBus.consumer(config.getAddress());
            consumer.handler(message -> {
                System.out.println(message.headers());
                System.out.println(message.body());

                DeliveryOptions deliveryOptions = new DeliveryOptions();
                deliveryOptions.addHeader("statusCode", "200");
                message.reply(new JsonObject("{\n" +
                        "    \"timestamp\": \"2010-10-10 02:10:10\",\n" +
                        "    \"value\": 22.2,\n" +
                        "    \"receiverId\": 42\n" +
                        "}"), deliveryOptions);
                handlerMap(config, message);
            });
        });
    }

    private void handlerMap(CassandraHandler handler, Message<JsonObject> message) {
        switch (message.headers().get("method")) {
            case "get":
                handler.createGetHandler(message);
                break;
            case "post":
                handler.createPostHandler(message);
                break;
        }
    }
}
