package com.openklaster.core.vertx.service.users;

import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class LoginManager implements  UserManager {
    private static final String methodName = "register";
    private static final Logger logger = LoggerFactory.getLogger(LoginManager.class);
    private final AuthenticationClient authenticationClient;

    public LoginManager(AuthenticationClient authenticationClient){
        this.authenticationClient = authenticationClient;
    }
    @Override
    public String getMethodsName() {
        return methodName;
    }

    @Override
    public void handleMessage(Message<JsonObject> message) {
        getUser(message.body()).future().onComplete(handler -> {
            if(handler.succeeded()){

            }else{

            }
        });
    }

    private Promise<User> getUser(JsonObject body) {
        String username = body.getString(usernameKey);
        return Promise.promise();
    }
}
