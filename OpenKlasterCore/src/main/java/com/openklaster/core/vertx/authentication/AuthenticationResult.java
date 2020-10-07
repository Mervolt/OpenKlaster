package com.openklaster.core.vertx.authentication;

public interface AuthenticationResult {
    boolean succeeded();
    Exception getCause();
}
