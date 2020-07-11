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
}
