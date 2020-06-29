package vertx;

import codec.GenericCodec;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import model.User;
import vertx.clients.DemoClient;

import java.util.Arrays;

public class CommonCoreVerticle{

    public static void main(String[] args) {
        //GrowattClient growattClient = new GrowattClient();
        DemoClient demoClient = new DemoClient();
        //Vertx vertx = Vertx.vertx();
        //demoClient.getLoadMeasurement(5);


        ClusterManager clusterManager = new HazelcastClusterManager();
        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);
        Vertx.clusteredVertx(options, result -> {
            if (result.succeeded()) {
                System.out.println("Success");
                Vertx vertx = result.result();
                EventBus eventBus = vertx.eventBus();

                eventBus.<JsonObject>consumer("openKlaster.core.request.post", message -> {
                    JsonObject json = message.body();
                    System.out.println(json);
                });
            }
        });
    }
}
