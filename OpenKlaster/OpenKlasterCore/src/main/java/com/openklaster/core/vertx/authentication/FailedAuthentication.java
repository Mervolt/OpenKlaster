package com.openklaster.core.vertx.authentication;

public class FailedAuthentication implements AuthenticationResult {

    private final Exception cause;

    @Override
    public Exception getCause() {
        return cause;
    }

    public FailedAuthentication(String message) {
        this(new FailedAuthenticationException(message));
    }

    public FailedAuthentication(Exception cause) {
        this.cause = cause;
    }

    @Override
    public boolean succeeded() {
        return false;
    }
}
