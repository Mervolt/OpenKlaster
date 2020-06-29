import config.ConfigFilesManager;
import io.vertx.core.Vertx;

public class App {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        OpenKlasterAPIVerticle verticle = new OpenKlasterAPIVerticle();
        vertx.deployVerticle(verticle);
    }
}
