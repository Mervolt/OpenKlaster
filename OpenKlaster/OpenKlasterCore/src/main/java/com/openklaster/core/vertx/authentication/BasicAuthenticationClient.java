package com.openklaster.core.vertx.authentication;

import com.openklaster.common.authentication.password.PasswordHandler;
import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.authentication.tokens.TokenValidationResult;
import com.openklaster.common.model.User;

public class BasicAuthenticationClient implements AuthenticationClient {
    private final PasswordHandler passwordHandler;
    private final TokenHandler tokenHandler;

    public BasicAuthenticationClient(PasswordHandler passwordHandler,
                                     TokenHandler tokenHandler) {
        this.passwordHandler = passwordHandler;
        this.tokenHandler = tokenHandler;
    }

    @Override
    public AuthenticationResult authenticateWithToken(User user, String token) {
        try {
            TokenValidationResult result = tokenHandler.validateToken(token, user.getUserTokens());
            return authenticationTokenResult(result, user);
        } catch (Exception e) {
            return new FailedAuthentication(e);
        }
    }

    @Override
    public AuthenticationResult authenticateWithSessionToken(User user, String token) {
        AuthenticationResult result = authenticateWithToken(user, token);
        if (result.succeeded()) {
            result = new SuccessfulSessionAuthentication(tokenHandler.getRefreshedSessionToken(user.getSessionToken()));
        }
        return result;
    }

    private AuthenticationResult authenticationTokenResult(TokenValidationResult result, User user) {
        switch (result) {
            case INVALID:
                return new FailedAuthentication(failedTokenAuth(user.getUsername()));
            case EXPIRED:
                return new FailedAuthentication(expiredTokenAuth(user.getUsername()));
            case VALID:
                return new SuccessfulAuthentication();
            default:
                return new FailedAuthentication(unknownTokenAuth(user.getUsername()));
        }
    }

    @Override
    public AuthenticationResult authenticateWithPassword(User user, String password) {
        try {
            if (passwordHandler.authenticatePassword(password, user.getPassword())) {
                return new SuccessfulAuthentication();
            } else {
                return new FailedAuthentication(failedPasswordAuth(user.getUsername()));
            }
        } catch (Exception e) {
            return new FailedAuthentication(e);
        }
    }

    @Override
    public String hashUserPassword(String plainPassword) {
        return passwordHandler.hashPassword(plainPassword);
    }

    private String failedPasswordAuth(String username) {
        return String.format("Incorrect password for user %s.", username);
    }

    private String failedTokenAuth(String username) {
        return String.format("Invalid token for user %s.", username);
    }

    private String expiredTokenAuth(String username) {
        return String.format("Expired token for user %s.", username);
    }

    private String unknownTokenAuth(String username) {
        return String.format("Unknown token validation result for user %s.", username);
    }

}
