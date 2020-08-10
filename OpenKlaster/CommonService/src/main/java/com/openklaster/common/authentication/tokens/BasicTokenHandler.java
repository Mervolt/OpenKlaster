package com.openklaster.common.authentication.tokens;

import com.openklaster.common.model.SessionToken;
import com.openklaster.common.model.UserToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BasicTokenHandler implements TokenHandler {

    private final int charsCountPerType;
    private final TokenGenerator tokenGenerator;
    private final int sessionTokenMinutesLifetime;

    public BasicTokenHandler(int charsCountPerType, int sessionTokenLifetime) {
        this.charsCountPerType = charsCountPerType;
        this.tokenGenerator = new BasicTokenGenerator();
        this.sessionTokenMinutesLifetime = sessionTokenLifetime;
    }

    @Override
    public UserToken generateUserToken() {
        String tokenData = tokenGenerator.generateToken(charsCountPerType);
        return new UserToken(tokenData);
    }

    @Override
    public SessionToken generateSessionToken() {
        String tokenData = tokenGenerator.generateToken(charsCountPerType);
        return new SessionToken(tokenData, createMinutesExpirationDate(sessionTokenMinutesLifetime));
    }

    @Override
    public TokenValidationResult validateSessionToken(String tokenData, SessionToken sessionToken) {
        if (sessionToken.getData().equals(tokenData)) {
            return validateTokenDate(sessionToken);
        } else {
            return TokenValidationResult.INVALID;
        }
    }

    @Override
    public TokenValidationResult validateApiToken(String tokenData, List<UserToken> userTokens) {
        Optional<UserToken> matchingToken = findMatchingToken(tokenData, userTokens);
        if (matchingToken.isPresent()) {
            return TokenValidationResult.VALID;
        } else {
            return TokenValidationResult.INVALID;
        }
    }

    @Override
    public SessionToken getRefreshedSessionToken(SessionToken token) {
        return new SessionToken(token.getData(),LocalDateTime.now().plusMinutes(sessionTokenMinutesLifetime));
    }

    private TokenValidationResult validateTokenDate(SessionToken sessionToken) {
        if (sessionToken.getExpirationDate().isAfter(LocalDateTime.now())) {
            return TokenValidationResult.VALID;
        } else {
            return TokenValidationResult.EXPIRED;
        }
    }

    private Optional<UserToken> findMatchingToken(String token, List<UserToken> userTokens) {
        return userTokens.stream()
                .filter(userToken -> userToken.getData().equals(token))
                .findFirst();
    }

    private LocalDateTime createMinutesExpirationDate(int tokenMinutesLifetime) {
        return LocalDateTime.now().plusMinutes(tokenMinutesLifetime);
    }
}
