package com.openklaster.core.authentication;

public interface AuthenticationResult {
    boolean succeeded();
    Exception getCause();
}
