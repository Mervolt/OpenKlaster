package openklaster.cassandra;

import io.vertx.core.Vertx;

public class App {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Verticle myVerticle = new Verticle(vertx);
        vertx.deployVerticle(myVerticle);
    }
}
