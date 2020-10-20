package com.openklaster.core.vertx.authentication;

import com.openklaster.common.model.User;

public interface AuthenticationClient {
    AuthenticationResult authenticateWithApiToken(User user, String token);

    AuthenticationResult authenticateWithSessionToken(User user, String token);

    AuthenticationResult authenticateWithTechnicalToken(String token);

    AuthenticationResult authenticateWithPassword(User user, String password);

    String hashUserPassword(String plainPassword);
}
