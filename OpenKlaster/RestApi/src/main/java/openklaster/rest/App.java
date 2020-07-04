package openklaster.rest;

import openklaster.common.config.ConfigFilesManager;
import io.vertx.core.Vertx;

public class App {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        ConfigFilesManager configFilesManager = new ConfigFilesManager();
        ApiVerticle verticle = new ApiVerticle(vertx, configFilesManager.getConfig(vertx));
        vertx.deployVerticle(verticle);
    }
}
