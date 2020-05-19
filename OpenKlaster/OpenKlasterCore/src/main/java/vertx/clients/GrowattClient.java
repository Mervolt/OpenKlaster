package vertx.clients;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class GrowattClient implements IClient{
    //TODO Dependency Injection - Vertx instance should be same for all Verticles
    WebClient client = WebClient.create(Vertx.vertx());
    public void getMeasurement(int id){
        client.get(80, "http://test.growatt.com", "/v1/plant/power")
                .send(result -> {
                    if(result.succeeded()) {
                        HttpResponse<Buffer> response = result.result();
                        System.out.println(response.statusCode());
                    }
                    else {
                        System.out.println(result.result());
                    }
                });
    }
}
