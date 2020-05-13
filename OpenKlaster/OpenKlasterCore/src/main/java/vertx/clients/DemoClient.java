package vertx.clients;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class DemoClient {
    public void getMeasurement(Vertx vertx, int id){
        WebClient client = WebClient.create(vertx);

        client.get(3000, "localhost", "/measurements")
                .addQueryParam("id", Integer.valueOf(id).toString())
                .send(result -> {
                    if(result.succeeded()) {
                        HttpResponse<Buffer> response = result.result();
                        System.out.println(response.statusCode());
                        JsonObject responseBody = response.bodyAsJsonObject();
                        client.post(8082, "localhost", "/postMeasurement")
                                .send(request ->{
                                    if(request.succeeded()){
                                        System.out.println("Posted");
                                    }
                                    else
                                        System.out.println(request.result());
                                });
                    }
                    else {
                        System.out.println(result.result());
                    }
                });
    }
}
