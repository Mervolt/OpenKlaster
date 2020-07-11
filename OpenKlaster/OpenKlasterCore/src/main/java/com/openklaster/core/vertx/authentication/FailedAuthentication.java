package com.openklaster.core.vertx.authentication;

public class FailedAuthentication implements  AuthenticationResult {
    private final Throwable cause;
    private final String message;

    public FailedAuthentication(String message, Throwable cause){
        this.cause=cause;
        this.message=message;
    }
    public FailedAuthentication(String message){
        this(message,null);
    }
    public FailedAuthentication(Throwable cause) {
        this(null, cause);
    }


    @Override
    public boolean succeeded() {
        return false;
    }
}
