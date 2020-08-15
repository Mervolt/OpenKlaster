package com.openklaster.common.authentication.tokens;

import com.openklaster.common.model.SessionToken;
import com.openklaster.common.model.UserToken;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class BasicTokenHandlerTest {

    TokenHandler tokenHandler;
    int charsCountPerType = 3;
    int sessionTokenLifetime = 30;

    @Before
    public void setup() {
        this.tokenHandler = new BasicTokenHandler(charsCountPerType, sessionTokenLifetime);
    }

    @Test
    public void validateValidTokenSingletonVersion() {
        String tokenData = "test";
        SessionToken userToken = new SessionToken(tokenData, LocalDateTime.now().plusMinutes(10) );
        TokenValidationResult result = tokenHandler.validateSessionToken(tokenData, userToken);

        assertEquals(TokenValidationResult.VALID, result);
    }

    @Test
    public void validateInvalidTokenSingletonVersion() {
        String tokenData = "test";
        String otherTokenData = "test2";
        SessionToken userToken = generateSessionToken(otherTokenData);
        TokenValidationResult result = tokenHandler.validateSessionToken(tokenData, userToken);

        assertEquals(TokenValidationResult.INVALID, result);
    }

    @Test
    public void validateExpiredTokenSingletonVersion() {
        String tokenData = "test";
        SessionToken userToken = generateExpiredSessionToken(tokenData);
        TokenValidationResult result = tokenHandler.validateSessionToken(tokenData, userToken);

        assertEquals(TokenValidationResult.EXPIRED, result);
    }

    @Test
    public void invalidHasPrecedenceOverExpiration() {
        String tokenData = "test";
        String otherTokenData = "test2";
        SessionToken userToken = generateExpiredSessionToken(otherTokenData);
        TokenValidationResult result = tokenHandler.validateSessionToken(tokenData, userToken);

        assertEquals(TokenValidationResult.INVALID, result);
    }

    @Test
    public void validateTokenEmptyList() {
        List<UserToken> list = Collections.emptyList();
        String tokenData = "test";

        TokenValidationResult result = tokenHandler.validateApiToken(tokenData,list);

        assertEquals(TokenValidationResult.INVALID,result);
    }

    @Test
    public void validateTokenList() {
        String tokenData = "test";

        UserToken token = new UserToken(tokenData);
        List<UserToken> list = Collections.singletonList(token);

        TokenValidationResult result = tokenHandler.validateApiToken(tokenData,list);

        assertEquals(TokenValidationResult.VALID,result);
    }

    private SessionToken generateSessionToken(String data){
        return new SessionToken(data, LocalDateTime.now().plusMinutes(10));
    }

    private SessionToken generateExpiredSessionToken(String data){
        return new SessionToken(data, LocalDateTime.now().minusMinutes(10));
    }
}