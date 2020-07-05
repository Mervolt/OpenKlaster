package com.openklaster.users.vertx.controller;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class RegisterController {
    WebClient webClient;

    public RegisterController(WebClient webClient) {
        this.webClient = webClient;
    }

    public void registerHandler(Router router) {
        router.route().path("/register/").handler(StaticHandler.create("register.html"));
    }

    public void registerRequestHandler(Router router){
        router.route().handler(BodyHandler.create());
        router.post().handler(CorsHandler.create("*"));
        router.post("/users/").consumes("application/json").handler(request -> {
            storeUser(request.getBodyAsJson());

            request.response().end();
        });


    }

    private void storeUser(JsonObject userJsonObject) {
        System.out.println(userJsonObject.toString());
        webClient.post(8081, "localhost", "/users/")
                .putHeader("Content-Type", "application/json")
                .sendBuffer(userJsonObject.toBuffer(), resp -> {
                    if (resp.succeeded()) {
                        System.out.println("Added successfully" + resp.result());
                    } else {
                        System.out.println("Could not add!" + resp.cause());
                    }
                });
    }

}
