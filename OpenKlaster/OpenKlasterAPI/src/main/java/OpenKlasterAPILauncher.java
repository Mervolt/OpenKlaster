import config.ConfigFilesManager;
import io.vertx.core.Vertx;

public class OpenKlasterAPILauncher {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        OpenKlasterAPIVerticle verticle = new OpenKlasterAPIVerticle();
        vertx.deployVerticle(verticle);
    }
}
