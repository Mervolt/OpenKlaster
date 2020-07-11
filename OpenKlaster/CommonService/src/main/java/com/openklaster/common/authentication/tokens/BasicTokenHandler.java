package com.openklaster.common.authentication.tokens;

import com.openklaster.common.model.UserToken;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BasicTokenHandler implements TokenHandler {

    private final int charsCountPerType;
    private final TokenGenerator tokenGenerator;
    private final int tokenDaysLifetime;
    private final int sessionTokenMinutesLifetime;

    BasicTokenHandler(int charsCountPerType, int tokenDaysLifetime, int sessionTokenLifetime) {
        this.charsCountPerType = charsCountPerType;
        this.tokenGenerator = new BasicTokenGenerator();
        this.tokenDaysLifetime = tokenDaysLifetime;
        this.sessionTokenMinutesLifetime = sessionTokenLifetime;
    }

    @Override
    public UserToken generateUserToken() {
        String tokenData = tokenGenerator.generateToken(charsCountPerType);
        return new UserToken(tokenData, createExpirationDate(tokenDaysLifetime));
    }

    private LocalDate createExpirationDate(int tokenDaysLifetime) {
        return LocalDate.now().plusDays(tokenDaysLifetime);
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
        return new UserToken(token.getData(),LocalDate.now().plus(Duration.ofMinutes(sessionTokenMinutesLifetime)));
    }

    private TokenValidationResult validateTokenDate(UserToken userToken) {
        if (userToken.getExpirationDate().isAfter(LocalDate.now())) {
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
