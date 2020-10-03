package com.openklaster.cassandra.app;

import com.openklaster.cassandra.properties.CassandraProperties;
import com.openklaster.cassandra.service.*;
import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
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

import java.util.Arrays;
import java.util.List;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;


public class CassandraVerticle extends AbstractVerticle {
    private CassandraClient cassandraClient;
    private final ConfigRetriever configRetriever;
    private NestedConfigAccessor configAccessor;
    private final Logger logger = LoggerFactory.getLogger(CassandraVerticle.class);
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
                CassandraClientOptions options = new CassandraClientOptions()
                        .setPort(configAccessor.getInteger(CassandraProperties.CASSANDRA_PORT))
                        .setKeyspace(configAccessor.getString(CassandraProperties.CASSANDRA_KEYSPACE));
                this.cassandraClient = CassandraClient.create(vertx, options);

                List<CassandraHandler<?>> handlers = prepareHandlers();
                eventBusConfig(promise, handlers);
            } else {
                logger.error("Could not retrieve com.openklaster.cassandra.app.CassandraVerticle config!");
                logger.error(config.cause());
                vertx.close();
            }
        });

    }

    public CassandraClient getCassandraClient() {
        return cassandraClient;
    }

    private List<CassandraHandler<?>> prepareHandlers() {
        return Arrays.asList(
                new LoadMeasurementHandler(cassandraClient, configAccessor.getJsonObject(CassandraProperties.LOAD_MEASUREMENT)),
                new SourceMeasurementHandler(cassandraClient, configAccessor.getJsonObject(CassandraProperties.SOURCE_MEASUREMENT)),
                new EnergyPredictionsHandler(cassandraClient, configAccessor.getJsonObject(CassandraProperties.ENERGY_PREDICTIONS)),
                new WeatherConditionsHandler(cassandraClient, configAccessor.getJsonObject(CassandraProperties.WEATHER_CONDITIONS))
        );
    }

    private void eventBusConfig(Promise<Void> promise, List<CassandraHandler<?>> handlers) {
        handlers.forEach(config -> {
            MessageConsumer<JsonObject> consumer = eventBus.consumer(config.getAddress());
            consumer.handler(message -> handlerMap(config, message));
        });
        promise.complete();
    }

    private void handlerMap(CassandraHandler<?> handler, Message<JsonObject> message) {
        switch (message.headers().get(METHOD_KEY)) {
            case CassandraProperties.GET:
                handler.createGetHandler(message);
                break;
            case CassandraProperties.POST:
                handler.createPostHandler(message);
                break;
        }
    }
}
