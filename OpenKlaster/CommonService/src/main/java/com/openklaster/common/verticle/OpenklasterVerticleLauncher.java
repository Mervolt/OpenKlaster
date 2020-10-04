package com.openklaster.common.verticle;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class OpenklasterVerticleLauncher {

    protected static <T extends OpenklasterVerticle> void launchVerticle(T verticle) {
        ClusterManager clusterManager = new HazelcastClusterManager();

        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);

        Vertx.clusteredVertx(options, res -> {
            Vertx vertx = res.result();
            vertx.deployVerticle(verticle);
        });
    }
}
