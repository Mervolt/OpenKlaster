import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import parser.UserParser;
import service.MongoPersistenceService;
import service.UserHandler;

public class Verticle extends AbstractVerticle {

    private final MongoClient client = MongoClient.createShared(vertx,config());
    private final MongoPersistenceService persistenceService = new MongoPersistenceService(client);


    @Override
    public void start(Promise<Void> promise){

        Router router =Router.router(vertx);
        router.route("/").handler(routingContext ->{
            HttpServerResponse response = routingContext.response();
            response
                    .end("Ended...");
        });

        routerConfig(router);

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(8080,result ->{
                    if(result.succeeded()){
                        promise.complete();
                    }else{
                        promise.fail(result.cause());
                    }
                });
    }

    private void routerConfig(Router router){
        configureUsers(router);
    }

    private void configureUsers(Router router){
        UserHandler userHandler = new UserHandler(new UserParser(),persistenceService);
        router.post("/users/").handler(userHandler::add);
        router.get("/users/").handler(userHandler::findById);
        router.delete("/users/").handler(userHandler::delete);
    }
}
