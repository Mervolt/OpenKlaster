package vertx.model;

public class DemoServer {
    int port = 3000;
    String host = "localhost";
    String endpointAddress = "/measurements";


    public static DemoServer getMeasurementEndpoint(){
        return new DemoServer();
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

    public String getEndpointAddress() {
        return endpointAddress;
    }

    public void setEndpointAddress(String endpointAddress) {
        this.endpointAddress = endpointAddress;
    }
}
