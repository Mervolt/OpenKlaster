package com.openklaster.core.vertx.app;

import com.openklaster.common.config.ConfigFilesManager;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class CoreApp {

    public static void main(String[] args) {
        ClusterManager clusterManager = new HazelcastClusterManager();
        ConfigFilesManager configFilesManager = new ConfigFilesManager();
        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);
        Vertx.clusteredVertx(options, res ->{
            Vertx vertx = res.result();
            CoreVerticle myCoreVerticle = new CoreVerticle(vertx,configFilesManager.getConfig(vertx));
            vertx.deployVerticle(myCoreVerticle);
        });
    }
}
