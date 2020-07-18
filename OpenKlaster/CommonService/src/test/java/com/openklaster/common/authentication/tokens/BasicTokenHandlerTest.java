package com.openklaster.common.authentication.tokens;

import com.openklaster.common.model.UserToken;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class BasicTokenHandlerTest {

    TokenHandler tokenHandler;
    int charsCountPerType = 3;
    int tokenDaysLifetime = 7;
    int sessionTokenLifetime = 30;

    @Before
    public void setup() {
        this.tokenHandler = new BasicTokenHandler(charsCountPerType, tokenDaysLifetime, sessionTokenLifetime);
    }

    @Test
    public void validateValidTokenSingletonVersion() {
        String tokenData = "test";
        UserToken userToken = new UserToken(tokenData, LocalDateTime.now().plusDays(1));
        TokenValidationResult result = tokenHandler.validateToken(tokenData, userToken);

        assertEquals(TokenValidationResult.VALID, result);
    }

    @Test
    public void validateInvalidTokenSingletonVersion() {
        String tokenData = "test";
        String otherTokenData = "test2";
        UserToken userToken = new UserToken(otherTokenData, LocalDateTime.now().plusDays(1));
        TokenValidationResult result = tokenHandler.validateToken(tokenData, userToken);

        assertEquals(TokenValidationResult.INVALID, result);
    }

    @Test
    public void validateExpiredTokenSingletonVersion() {
        String tokenData = "test";
        UserToken userToken = new UserToken(tokenData, LocalDateTime.now().minusDays(1));
        TokenValidationResult result = tokenHandler.validateToken(tokenData, userToken);

        assertEquals(TokenValidationResult.EXPIRED, result);
    }

    @Test
    public void invalidHasPrecedenceOverExpiration() {
        String tokenData = "test";
        String otherTokenData = "test2";
        UserToken userToken = new UserToken(otherTokenData, LocalDateTime.now().minusDays(1));
        TokenValidationResult result = tokenHandler.validateToken(tokenData, userToken);

        assertEquals(TokenValidationResult.INVALID, result);
    }

    @Test
    public void validateTokenEmptyList() {
        List<UserToken> list = Collections.emptyList();
        String tokenData = "test";

        TokenValidationResult result = tokenHandler.validateToken(tokenData,list);

        assertEquals(TokenValidationResult.INVALID,result);
    }

    @Test
    public void validateTokenList() {
        String tokenData = "test";

        UserToken token = new UserToken(tokenData, LocalDateTime.now().plusDays(1));
        List<UserToken> list = Collections.singletonList(token);

        TokenValidationResult result = tokenHandler.validateToken(tokenData,list);

        assertEquals(TokenValidationResult.VALID,result);
    }
}