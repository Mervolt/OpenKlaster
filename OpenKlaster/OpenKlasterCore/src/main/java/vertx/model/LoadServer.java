package vertx.model;

public class LoadServer {
    int port = 3000;
    String host = "localhost";
    String measurementEndpointAddress = "/measurements";

    public static LoadServer getMeasurementEndpoint(){
        return new LoadServer();
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

    public String getMeasurementEndpointAddress() {
        return measurementEndpointAddress;
    }

    public void setMeasurementEndpointAddress(String measurementEndpointAddress) {
        this.measurementEndpointAddress = measurementEndpointAddress;
    }
}
