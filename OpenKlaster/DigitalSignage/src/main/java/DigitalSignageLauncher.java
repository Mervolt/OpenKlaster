import io.vertx.core.Vertx;

//Do we even need Java code here #2?
public class DigitalSignageLauncher {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        DigitalSignageVerticle verticle = new DigitalSignageVerticle(vertx);
        vertx.deployVerticle(verticle);
    }
}
