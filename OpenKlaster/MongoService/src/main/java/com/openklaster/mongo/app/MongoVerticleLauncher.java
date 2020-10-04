package com.openklaster.mongo.app;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class MongoVerticleLauncher {
    public static void main(String[] args) {
        ClusterManager clusterManager = new HazelcastClusterManager();

        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);

        Vertx.clusteredVertx(options, res -> {
            Vertx vertx = res.result();
            MongoVerticle verticle = new MongoVerticle();
            vertx.deployVerticle(verticle);
        });
    }
}
