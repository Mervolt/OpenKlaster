package vertx.clients;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import vertx.model.DemoServer;
import vertx.model.MeasurementServer;
import vertx.model.QueryParam;

public class DemoClient implements IClient{
    //TODO Dependency Injection - Vertx instance should be same for all Verticles
    WebClient client = WebClient.create(Vertx.vertx());

    @Override
    public void getLoadMeasurement(int id){
        DemoServer server = DemoServer.getMeasurementEndpoint();
        client.get(server.getPort(), server.getHost(), server.getLoadMeasurementEndpointAddress())
                .addQueryParam("id", Integer.valueOf(id).toString())
                .send(result -> {
                    if(result.succeeded()) {
                        handleGetLoadMeasurementSuccess(result);
                    }
                    else {
                        handleGetMeasurementFailure(id);
                    }
                });
    }

    private void handleGetLoadMeasurementSuccess(AsyncResult<HttpResponse<Buffer>> result){
        JsonArray requestBody = getRequestBodyFromHttpResponse(result);
        postLoadMeasurement(requestBody);
    }

    private JsonArray getRequestBodyFromHttpResponse(AsyncResult<HttpResponse<Buffer>> result){
        HttpResponse<Buffer> response = result.result();
        return response.bodyAsJsonArray();
    }

    private void handleGetMeasurementFailure(int id){
        //TODO create proper failure handler...
        throw new RuntimeException("Could not collect data for id: " + id);
    }

    private void postLoadMeasurement(JsonArray requestBody){
        MeasurementServer measurementServer = MeasurementServer.getMeasurementEndpoint();
        JsonObject requestData = requestBody.getJsonObject(0);
        QueryParam[] params = prepareLoadMeasurementQueryParams(requestData);
        postMeasurement(requestData, measurementServer, measurementServer.getLoadMeasurementEndpointAddress(), params);
    }

    private QueryParam[] prepareLoadMeasurementQueryParams(JsonObject requestData){
        QueryParam[] params = new QueryParam[2];
        params[0] = new QueryParam();
        params[1] = new QueryParam();
        params[0].setId("receiverId");
        params[0].setValue(requestData.getInteger("receiverId").toString());
        params[1].setId("value");
        params[1].setValue(requestData.getInteger("energy").toString());
        return params;
    }

    private void postMeasurement(JsonObject requestData, MeasurementServer server, String endpointAddress,
                                 QueryParam[] params){
        HttpRequest<Buffer> httpRequest = prepareMeasurementPost(server, endpointAddress, params);

        httpRequest.send(request ->{
                    if(request.succeeded()){
                        handlePostMeasurementSuccess();
                    }
                    else
                        handlePostMeasurementFailure(requestData.getString("id"));
                });
    }

    private HttpRequest<Buffer> prepareMeasurementPost(MeasurementServer server, String endpointAddress,
                                                       QueryParam[] params) {
        HttpRequest<Buffer> httpRequest  = client.post(server.getPort(), server.getHost(), endpointAddress);
        for (QueryParam param : params) {
            httpRequest.addQueryParam(param.getId(), param.getValue());
        }
        return httpRequest;
    }

    private void handlePostMeasurementSuccess() {
        //TODO add logging with introduction of logger
        System.out.println("Success");
    }

    private void handlePostMeasurementFailure(String id){
        //TODO create proper failure handler...
        throw new RuntimeException("Could not post measurement data for id: " + id);
    }

    @Override
    public void getSourceMeasurement(int id) {
        DemoServer server = DemoServer.getMeasurementEndpoint();
        client.get(server.getPort(), server.getHost(), server.getSourceMeasurementEndpointAddress())
                .addQueryParam("id", Integer.valueOf(id).toString())
                .send(result -> {
                    if(result.succeeded()) {
                        handleGetSourceMeasurementSuccess(result);
                    }
                    else {
                        handleGetMeasurementFailure(id);
                    }
                });
    }

    private void handleGetSourceMeasurementSuccess(AsyncResult<HttpResponse<Buffer>> result) {
        JsonArray requestBody = getRequestBodyFromHttpResponse(result);
        postSourceMeasurement(requestBody);
    }

    private void postSourceMeasurement(JsonArray requestBody) {
        MeasurementServer measurementServer = MeasurementServer.getMeasurementEndpoint();
        JsonObject requestData = requestBody.getJsonObject(0);
        QueryParam[] params = prepareSourceMeasurementQueryParams(requestData);
        postMeasurement(requestData, measurementServer, measurementServer.getSourceMeasurementEndpointAddress(), params);
    }

    private QueryParam[] prepareSourceMeasurementQueryParams(JsonObject requestData) {
        QueryParam[] params = new QueryParam[2];
        params[0] = new QueryParam();
        params[1] = new QueryParam();
        params[0].setId("deviceId");
        params[0].setValue(requestData.getInteger("id").toString());
        params[1].setId("value");
        params[1].setValue(requestData.getInteger("energy").toString());
        return params;
    }

    @Override
    public void getEnergyPrediction(int id) {
//        DemoServer server = DemoServer.getMeasurementEndpoint();
//        client.get(server.getPort(), server.getHost(), server.getEnergyPredictionsEndpointAddress())
//                .addQueryParam("id", Integer.valueOf(id).toString())
//                .send(result -> {
//                    if(result.succeeded()) {
//                        handleGetLoadMeasurementSuccess(result);
//                    }
//                    else {
//                        handleGetMeasurementFailure(id);
//                    }
//                });

    }

    @Override
    public void getWeatherConditions(int id) {

    }

}
