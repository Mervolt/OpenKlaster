package vertx.clients;

import io.vertx.core.Vertx;

public interface IClient {
    void getMeasurement(int id);
}
