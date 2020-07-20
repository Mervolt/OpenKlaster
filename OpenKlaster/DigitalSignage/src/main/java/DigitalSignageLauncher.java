import io.vertx.core.Vertx;

public class DigitalSignageLauncher {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        DigitalSignageVerticle verticle = new DigitalSignageVerticle(vertx);
        vertx.deployVerticle(verticle);
    }
}
