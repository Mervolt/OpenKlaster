package com.openklaster.core.vertx.authentication;

public class FailedAuthentication implements AuthenticationResult {

    private final Throwable cause;

    @Override
    public Throwable getCause() {
        return cause;
    }

    public FailedAuthentication(String message) {
        this(new FailedAuthenticationException(message));
    }

    public FailedAuthentication(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public boolean succeeded() {
        return false;
    }
}
