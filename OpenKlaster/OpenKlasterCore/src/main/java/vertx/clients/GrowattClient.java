package vertx.clients;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class GrowattClient {
    public void getMeasurement(Vertx vertx){
        WebClient client = WebClient.create(vertx);
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
