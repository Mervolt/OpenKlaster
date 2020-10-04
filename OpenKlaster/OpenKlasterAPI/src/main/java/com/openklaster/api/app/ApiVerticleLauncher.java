package com.openklaster.api.app;

import com.openklaster.common.verticle.OpenklasterVerticleLauncher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class ApiVerticleLauncher extends OpenklasterVerticleLauncher {
    public static void main(String[] args) {
      launchVerticle(new ApiVerticle(true));
    }
}
