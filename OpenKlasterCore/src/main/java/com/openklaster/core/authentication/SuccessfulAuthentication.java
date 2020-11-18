package com.openklaster.core.authentication;

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
