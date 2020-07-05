package com.openklaster.core.vertx.app;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class CoreVerticle {

    public static void main(String[] args) {

        ClusterManager clusterManager = new HazelcastClusterManager();
        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);
        Vertx.clusteredVertx(options, result -> {
            if (result.succeeded()) {
                Vertx vertx = result.result();
                EventBus eventBus = vertx.eventBus();

                eventBus.<JsonObject>consumer("openKlaster.core.user", message -> {
                    MultiMap headers = message.headers();
                    if(!headers.contains("method"))
                        /*
                        * handle unprocessable..
                        */
                        message.reply(null);
                    JsonObject json = message.body();
                    message.reply(json);
                });


                eventBus.<JsonObject>consumer("openKlaster.core.load", message -> {
                    JsonObject json = message.body();
                    message.reply(json);
                });

                eventBus.<JsonObject>consumer("openKlaster.core.source", message -> {
                    JsonObject json = message.body();
                    message.reply(json);
                });

                eventBus.<JsonObject>consumer("openKlaster.core.inverter", message -> {
                    JsonObject json = message.body();
                    message.reply(json);
                });

                eventBus.<JsonObject>consumer("openKlaster.core.installation", message -> {
                    JsonObject json = message.body();
                    message.reply(json);
                });

                eventBus.<JsonObject>consumer("openKlaster.core.energySourceCalculator", message -> {
                    JsonObject json = message.body();
                    message.reply(json);
                });

            }
        });
    }
}
