package com.openklaster.core.vertx.messages.repository;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message){
        super(message);
    }
}
