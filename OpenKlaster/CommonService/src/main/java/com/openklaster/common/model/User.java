package com.openklaster.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class User {

    @JsonProperty("_id")
    private String username;
    private String password;
    private String email;
    private List<UserToken> userTokens;
    private UserToken sessionToken;

    public void addUserToken(UserToken token) {
        this.userTokens.add(token);
    }

    public boolean deleteToken(String token) {
        return userTokens.removeIf(userToken -> userToken.getData().equals(token));
    }
}
