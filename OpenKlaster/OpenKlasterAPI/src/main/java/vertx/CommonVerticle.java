package vertx;
import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import vertx.controller.RegisterController;

public class CommonVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        CommonVerticle verticle = new CommonVerticle();
        try {
            verticle.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() throws Exception {
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        HttpServer server = vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
        RegisterController registerController = new RegisterController();
        registerController.registerHandler(router);
        registerController.registerRequestHandler(router);
    }


    /***This should wait for Microservice implementation***/
//    private Promise<Void> publishEndpoint(String name, String host, int port){
//        Record record = HttpEndpoint.createRecord(name, host, port, "/",
//                new JsonObject().put("api.name", config().getString("api.name", ""))
//        );
//        return publish(record);
//    }
//
//    private Promise<Void> publish(Record record){
//         Promise<Void> promise = Promise.promise();
//        serviceDiscovery.publish(record, request ->{
//            if(request.succeeded())
//                handlePublishSuccess(promise, request, record);
//            else
//                handlePublishFailure(promise, request);
//        });
//        return promise;
//    }
//
//    private void handlePublishSuccess(Promise<Void> promise, AsyncResult<Record> request, Record record) {
//        records.add(record);
//        promise.complete();
//    }
//
//    private void handlePublishFailure(Promise<Void> promise, AsyncResult<Record> request) {
//        promise.fail(request.cause());
//    }
}
