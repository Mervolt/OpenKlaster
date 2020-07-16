package com.openklaster.core.vertx.authentication;

public class FailedAuthenticationException extends Exception{

    public FailedAuthenticationException(String message){
        super(message);
    }
}
