import config.ConfigFilesManager;
import io.vertx.core.Vertx;

public class App {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        ConfigFilesManager configFilesManager = new ConfigFilesManager();
        Verticle verticle = new Verticle(vertx, configFilesManager.getConfig(vertx));
        vertx.deployVerticle(verticle);
    }
}
