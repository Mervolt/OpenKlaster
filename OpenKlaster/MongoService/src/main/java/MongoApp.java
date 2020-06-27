import config.ConfigFilesManager;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class MongoApp {

    public static void main(String[] args) {

        ClusterManager clusterManager = new HazelcastClusterManager();
        ConfigFilesManager configFilesManager = new ConfigFilesManager();
        VertxOptions options = new VertxOptions().setClusterManager(clusterManager);
        Vertx.clusteredVertx(options, res -> {
            Vertx vertx = res.result();
            MongoVerticle myMongoVerticle = new MongoVerticle(vertx,configFilesManager.getConfig(vertx));
            vertx.deployVerticle(myMongoVerticle);

        });
    }
}
