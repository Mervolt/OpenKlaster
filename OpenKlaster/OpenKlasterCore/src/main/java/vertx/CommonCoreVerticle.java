package vertx;

import io.vertx.core.Vertx;
import vertx.clients.GrowattClient;

public class CommonCoreVerticle{

    public static void main(String[] args) {
        GrowattClient growattClient = new GrowattClient();
        Vertx vertx = Vertx.vertx();
        growattClient.getMeasurement(vertx);
    }
}