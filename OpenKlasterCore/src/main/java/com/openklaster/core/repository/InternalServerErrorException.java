package com.openklaster.core.repository;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message){
        super(message);
    }
}
