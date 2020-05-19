package vertx.clients;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import vertx.model.DemoServer;
import vertx.model.LoadServer;

public class DemoClient implements IClient{
    //TODO Dependency Injection - Vertx instance should be same for all Verticles
    WebClient client = WebClient.create(Vertx.vertx());


    public void getMeasurement(int id){
        DemoServer server = DemoServer.getMeasurementEndpoint();
        client.get(server.getPort(), server.getHost(), server.getEndpointAddress())
                .addQueryParam("id", Integer.valueOf(id).toString())
                .send(result -> {
                    if(result.succeeded()) {
                        handleGetMeasurementSuccess(result);
                    }
                    else {
                        handleGetMeasurementFailure(id);
                    }
                });
    }

    private void handleGetMeasurementSuccess(AsyncResult<HttpResponse<Buffer>> result){
        HttpResponse<Buffer> response = result.result();
        JsonArray requestBody = response.bodyAsJsonArray();
        postMeasurement(requestBody);
    }

    private void handleGetMeasurementFailure(int id){
        //TODO create proper failure handler...
        throw new RuntimeException("Could not collect data for id: " + id);
    }

    private void postMeasurement(JsonArray requestBody){
        JsonObject requestData = requestBody.getJsonObject(0);
        LoadServer loadServer = LoadServer.getMeasurementEndpoint();
        client.post(loadServer.getPort(), loadServer.getHost(), loadServer.getMeasurementEndpointAddress())
                .addQueryParam("receiverId", requestData.getString("id"))
                .addQueryParam("value", requestData.getString("energy"))
                .send(request ->{
                    if(request.succeeded()){
                        handlePostMeasurementSuccess();
                    }
                    else
                        handlePostMeasurementFailure(requestData.getString("id"));
                });
    }

    private void handlePostMeasurementSuccess() {
        //TODO add logging with introduction of logger
        System.out.println("Success");
    }

    private void handlePostMeasurementFailure(String id){
        //TODO create proper failure handler...
        throw new RuntimeException("Could not post measurement data for id: " + id);
    }
}
