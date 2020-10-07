package com.openklaster.core.vertx.authentication;

public class FailedAuthenticationException extends RuntimeException{

    public FailedAuthenticationException(String message){
        super(message);
    }
    public FailedAuthenticationException(String message, Throwable reason){
        super(message, reason);
    }
    public FailedAuthenticationException(Throwable reason){
        super(reason);
    }
}
