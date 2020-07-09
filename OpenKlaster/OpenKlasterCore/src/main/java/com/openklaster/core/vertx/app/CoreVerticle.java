package com.openklaster.core.vertx.app;

import com.openklaster.common.config.NestedConfigAccessor;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class CoreVerticle extends AbstractVerticle {

    private ConfigRetriever configRetriever;
    private static final Logger logger = LoggerFactory.getLogger(CoreVerticle.class);
    private EventBus eventBus;
    private NestedConfigAccessor configAccessor;

    public CoreVerticle(Vertx vertx, ConfigRetriever configRetriever) {
        this.vertx = vertx;
        this.configRetriever = configRetriever;
        this.eventBus = vertx.eventBus();
    }

    @Override
    public void start(Promise<Void> promise) {
        this.configRetriever.getConfig(result -> {
            if (result.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(result.result());
                handlePostConfig(promise);
            } else {
                logger.error("Could not retrieve CoreVerticle config");
                logger.error(result.cause());
                vertx.close();
                promise.complete();
            }
        });
    }

    private void handlePostConfig(Promise<Void> promise) {


        promise.complete();

    }
}
