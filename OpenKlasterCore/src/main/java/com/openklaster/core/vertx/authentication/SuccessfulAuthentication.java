package com.openklaster.core.vertx.authentication;

public class SuccessfulAuthentication implements  AuthenticationResult{

    @Override
    public boolean succeeded() {
        return true;
    }

    @Override
    public Exception getCause() {
        return null;
    }
}
