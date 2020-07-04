package openklaster.core.vertx.clients;

import io.vertx.core.Vertx;

public interface IClient {
    void getLoadMeasurement(int id);
    void getSourceMeasurement(int id);
    void getEnergyPrediction(int id);
    void getWeatherConditions(int id);
}
