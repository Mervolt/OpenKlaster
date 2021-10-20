package com.openklaster.app.services;

import com.openklaster.app.model.entities.user.SessionTokenEntity;
import com.openklaster.app.model.entities.user.TokenEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TokensService {

    private final static int charsCountPerType = 6;
    private final TokenGenerator tokenGenerator;
    private final static int sessionTokenMinutesLifetime = 30;

    public TokensService() {
        this.tokenGenerator = new TokenGenerator();
    }

    public TokenEntity generateUserToken() {
        String tokenData = tokenGenerator.generateToken(charsCountPerType);
        return new TokenEntity(tokenData);
    }

    public SessionTokenEntity generateSessionToken() {
        String tokenData = tokenGenerator.generateToken(charsCountPerType);
        return new SessionTokenEntity(tokenData, createMinutesExpirationDate());
    }

    public TokenValidationResult validateSessionToken(String tokenData, SessionTokenEntity sessionToken) {
        if (sessionToken == null) {
            return TokenValidationResult.INVALID;
        }
        if (sessionToken.getData().equals(tokenData)) {
            return validateTokenDate(sessionToken);
        } else {
            return TokenValidationResult.INVALID;
        }
    }

    public TokenValidationResult validateApiToken(String tokenData, List<TokenEntity> userTokens) {
        Optional<TokenEntity> matchingToken = findMatchingToken(tokenData, userTokens);
        if (matchingToken.isPresent()) {
            return TokenValidationResult.VALID;
        } else {
            return TokenValidationResult.INVALID;
        }
    }


    public SessionTokenEntity getRefreshedSessionToken(SessionTokenEntity token) {
        return new SessionTokenEntity(token.getData(), LocalDateTime.now().plusMinutes(sessionTokenMinutesLifetime));
    }

    private TokenValidationResult validateTokenDate(SessionTokenEntity sessionToken) {
        if (sessionToken.getExpirationDate().isAfter(LocalDateTime.now())) {
            return TokenValidationResult.VALID;
        } else {
            return TokenValidationResult.EXPIRED;
        }
    }

    private Optional<TokenEntity> findMatchingToken(String token, List<TokenEntity> userTokens) {
        return userTokens.stream()
                .filter(userToken -> userToken.getData().equals(token))
                .findFirst();
    }

    private LocalDateTime createMinutesExpirationDate() {
        return LocalDateTime.now().plusMinutes(TokensService.sessionTokenMinutesLifetime);
    }
}


enum TokenValidationResult {
    VALID,
    INVALID,
    EXPIRED
}

