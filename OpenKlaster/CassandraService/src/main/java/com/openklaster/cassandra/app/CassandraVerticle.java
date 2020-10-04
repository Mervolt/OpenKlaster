package com.openklaster.cassandra.app;

import com.openklaster.cassandra.properties.CassandraProperties;
import com.openklaster.cassandra.service.*;
import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.verticle.OpenklasterVerticle;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;


public class CassandraVerticle extends OpenklasterVerticle {
    private CassandraClient cassandraClient;
    private NestedConfigAccessor configAccessor;
    private final Logger logger = LoggerFactory.getLogger(CassandraVerticle.class);
    private EventBus eventBus;

    @Override
    public void init(Vertx vertx, Context context) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();

        ConfigFilesManager configFilesManager = new ConfigFilesManager(this.configFilenamePrefix);
        configFilesManager.getConfig(vertx).getConfig(config -> {
            if (config.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(config.result());
                String hostname = configAccessor.getString(CassandraProperties.CASSANDRA_HOST);
                CassandraClientOptions options = new CassandraClientOptions()
                        .setPort(configAccessor.getInteger(CassandraProperties.CASSANDRA_PORT))
                        .setKeyspace(configAccessor.getString(CassandraProperties.CASSANDRA_KEYSPACE))
                        .setContactPoints(Collections.singletonList(hostname));
                this.cassandraClient = CassandraClient.create(vertx, options);

                List<CassandraHandler<?>> handlers = prepareHandlers();
                eventBusConfig(handlers);
            } else {
                logger.error("Could not retrieve com.openklaster.cassandra.app.CassandraVerticle config!");
                logger.error(config.cause().getMessage());
                vertx.close();
            }
        });

    }

    private List<CassandraHandler<?>> prepareHandlers() {
        return Arrays.asList(
                new LoadMeasurementHandler(cassandraClient, configAccessor.getJsonObject(CassandraProperties.LOAD_MEASUREMENT)),
                new SourceMeasurementHandler(cassandraClient, configAccessor.getJsonObject(CassandraProperties.SOURCE_MEASUREMENT)),
                new EnergyPredictionsHandler(cassandraClient, configAccessor.getJsonObject(CassandraProperties.ENERGY_PREDICTIONS)),
                new WeatherConditionsHandler(cassandraClient, configAccessor.getJsonObject(CassandraProperties.WEATHER_CONDITIONS))
        );
    }

    private void eventBusConfig(List<CassandraHandler<?>> handlers) {
        handlers.forEach(config -> {
            MessageConsumer<JsonObject> consumer = eventBus.consumer(config.getAddress());
            consumer.handler(message -> handlerMap(config, message));
        });
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
