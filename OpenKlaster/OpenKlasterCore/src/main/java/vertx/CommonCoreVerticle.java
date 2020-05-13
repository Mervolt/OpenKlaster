package vertx;

import io.vertx.core.Vertx;
import vertx.clients.DemoClient;
import vertx.clients.GrowattClient;

public class CommonCoreVerticle{

    public static void main(String[] args) {
        //GrowattClient growattClient = new GrowattClient();
        DemoClient demoClient = new DemoClient();
        Vertx vertx = Vertx.vertx();
        demoClient.getMeasurement(vertx, 5);
    }
}