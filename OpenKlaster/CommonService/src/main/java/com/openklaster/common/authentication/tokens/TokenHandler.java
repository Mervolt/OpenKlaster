package com.openklaster.common.authentication.tokens;

import com.openklaster.common.model.UserToken;

import java.util.List;

public interface TokenHandler {
    UserToken generateUserToken();

    UserToken generateSessionToken();

    TokenValidationResult validateToken(String token, UserToken userToken);

    TokenValidationResult validateToken(String token, List<UserToken> userToken);

    UserToken getRefreshedSessionToken(UserToken token);
}
