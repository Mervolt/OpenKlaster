package openklaster.core.vertx.model;

public class MeasurementServer {
    int port = 8190;
    String host = "localhost";
    String energyPredictionsEndpointAddress = "/energypredictions";
    String loadMeasurementEndpointAddress = "/loadmeasurement";
    String sourceMeasurementEndpointAddress = "/energypredictions";
    String weatherConditionsEndpointAddress = "/energypredictions";

    public static MeasurementServer getMeasurementEndpoint(){
        return new MeasurementServer();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLoadMeasurementEndpointAddress() {
        return loadMeasurementEndpointAddress;
    }

    public void setLoadMeasurementEndpointAddress(String loadMeasurementEndpointAddress) {
        this.loadMeasurementEndpointAddress = loadMeasurementEndpointAddress;
    }

    public String getEnergyPredictionsEndpointAddress() {
        return energyPredictionsEndpointAddress;
    }

    public void setEnergyPredictionsEndpointAddress(String energyPredictionsEndpointAddress) {
        this.energyPredictionsEndpointAddress = energyPredictionsEndpointAddress;
    }

    public String getSourceMeasurementEndpointAddress() {
        return sourceMeasurementEndpointAddress;
    }

    public void setSourceMeasurementEndpointAddress(String sourceMeasurementEndpointAddress) {
        this.sourceMeasurementEndpointAddress = sourceMeasurementEndpointAddress;
    }

    public String getWeatherConditionsEndpointAddress() {
        return weatherConditionsEndpointAddress;
    }

    public void setWeatherConditionsEndpointAddress(String weatherConditionsEndpointAddress) {
        this.weatherConditionsEndpointAddress = weatherConditionsEndpointAddress;
    }
}
