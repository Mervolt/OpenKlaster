package com.openklaster.core.vertx.authentication;

import com.openklaster.common.authentication.password.PasswordHandler;
import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.authentication.tokens.TokenValidationResult;
import com.openklaster.common.model.SessionToken;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.messages.repository.CrudRepository;

public class BasicAuthenticationClient implements AuthenticationClient {
    private final PasswordHandler passwordHandler;
    private final TokenHandler tokenHandler;
    private final CrudRepository<User> userCrudRepository;

    public BasicAuthenticationClient(PasswordHandler passwordHandler,
                                     TokenHandler tokenHandler, CrudRepository<User> userCrudRepository) {
        this.passwordHandler = passwordHandler;
        this.tokenHandler = tokenHandler;
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public AuthenticationResult authenticateWithApiToken(User user, String tokenData) {
        try {
            TokenValidationResult result = tokenHandler.validateApiToken(tokenData, user.getUserTokens());
            return authenticationTokenResult(result, user);
        } catch (Exception e) {
            return new FailedAuthentication(e);
        }
    }

    @Override
    public AuthenticationResult authenticateWithSessionToken(User user, String tokenData) {
        try{
            TokenValidationResult result = tokenHandler.validateSessionToken(tokenData, user.getSessionToken());
            if (result == TokenValidationResult.VALID) {
                SessionToken refreshedToken = tokenHandler.getRefreshedSessionToken(user.getSessionToken());
                persistSessionToken(user, refreshedToken);
                return new SuccessfulSessionAuthentication(refreshedToken);
            } else {
                return new FailedAuthentication(failedSessionTokenAuth(user.getUsername()));
            }
        } catch (Exception e ) {
            return new FailedAuthentication(e);
        }
    }

    private void persistSessionToken(User user, SessionToken refreshedToken) {
        user.setSessionToken(refreshedToken);
        userCrudRepository.update(user);
    }

    private AuthenticationResult authenticationTokenResult(TokenValidationResult result, User user) {
        switch (result) {
            case INVALID:
                return new FailedAuthentication(failedApiTokenAuth(user.getUsername()));
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
                return new SuccessfulSessionAuthentication(tokenHandler.generateSessionToken());
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
        return String.format("Incorrect password for user %s", username);
    }

    private String failedApiTokenAuth(String username) {
        return String.format("Invalid Api token for user %s", username);
    }

    private String failedSessionTokenAuth(String username) {
        return String.format("Invalid Session token for user %s", username);
    }

    private String expiredTokenAuth(String username) {
        return String.format("Expired token for user %s", username);
    }

    private String unknownTokenAuth(String username) {
        return String.format("Unknown token validation result for user %s", username);
    }

}
