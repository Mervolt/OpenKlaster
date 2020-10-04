package com.openklaster.core.vertx.app;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class CoreVerticleLauncher {
    public static void main(String[] args) {
        ClusterManager clusterManager = new HazelcastClusterManager();

        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);

        Vertx.clusteredVertx(options, res -> {
            Vertx vertx = res.result();
            CoreVerticle verticle = new CoreVerticle();
            vertx.deployVerticle(verticle);
        });
    }
}
