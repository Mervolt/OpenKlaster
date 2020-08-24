package com.openklaster.common.authentication.tokens;

import com.openklaster.common.model.SessionToken;
import com.openklaster.common.model.UserToken;

import java.util.List;

public interface TokenHandler {
    UserToken generateUserToken();

    SessionToken generateSessionToken();

    TokenValidationResult validateSessionToken(String tokenData, SessionToken userToken);

    TokenValidationResult validateApiToken(String tokenData, List<UserToken> userToken);

    SessionToken getRefreshedSessionToken(SessionToken token);
}
