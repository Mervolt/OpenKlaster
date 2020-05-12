package vertx.controller;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class RegisterController {
    public void registerHandler(Router router){
        router.route().path("/register/").handler(StaticHandler.create("register.html"));
    }

    public void registerRequestHandler(Router router){
        router.route().handler(BodyHandler.create());
        router.post().handler(CorsHandler.create("*"));
        router.post("/users/").consumes("application/json").handler(request -> {
            System.out.println(request.getBodyAsJson());
            //Send request to User Service
            request.response().end();
        });


    }


}
