package com.openklaster.common.authentication.tokens;

import com.openklaster.common.model.UserToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BasicTokenHandler implements TokenHandler {

    private final int charsCountPerType;
    private final TokenGenerator tokenGenerator;
    private final int tokenDaysLifetime;
    private final int sessionTokenMinutesLifetime;

    public BasicTokenHandler(int charsCountPerType, int tokenDaysLifetime, int sessionTokenLifetime) {
        this.charsCountPerType = charsCountPerType;
        this.tokenGenerator = new BasicTokenGenerator();
        this.tokenDaysLifetime = tokenDaysLifetime;
        this.sessionTokenMinutesLifetime = sessionTokenLifetime;
    }

    @Override
    public UserToken generateUserToken() {
        String tokenData = tokenGenerator.generateToken(charsCountPerType);
        return new UserToken(tokenData, createDaysExpirationDate(tokenDaysLifetime));
    }

    @Override
    public UserToken generateSessionToken() {
        String tokenData = tokenGenerator.generateToken(charsCountPerType);
        return new UserToken(tokenData, createMinutesExpirationDate(sessionTokenMinutesLifetime));
    }

    private LocalDateTime createDaysExpirationDate(int tokenDaysLifetime) {
        return LocalDateTime.now().plusDays(tokenDaysLifetime);
    }

    private LocalDateTime createMinutesExpirationDate(int tokenMinutesLifetime) {
        return LocalDateTime.now().plusMinutes(tokenMinutesLifetime);
    }

    @Override
    public TokenValidationResult validateToken(String token, UserToken userToken) {

        if (userToken.getData().equals(token)) {
            return validateTokenDate(userToken);
        } else {
            return TokenValidationResult.INVALID;
        }
    }

    @Override
    public TokenValidationResult validateToken(String token, List<UserToken> userTokens) {
        Optional<UserToken> matchingToken = findMatchingToken(token, userTokens);
        if (matchingToken.isPresent()) {
            return validateTokenDate(matchingToken.get());
        } else {
            return TokenValidationResult.INVALID;
        }
    }

    @Override
    public UserToken getRefreshedSessionToken(UserToken token) {
        return new UserToken(token.getData(),LocalDateTime.now().plusMinutes(sessionTokenMinutesLifetime));
    }

    private TokenValidationResult validateTokenDate(UserToken userToken) {
        if (userToken.getExpirationDate().isAfter(LocalDateTime.now())) {
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
}
