package com.openklaster.common.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.openklaster.common.model.ModelProperties.*;

@Data
public class User {
    @JsonAlias({"username", "_id"})
    @JsonProperty("_id")
    private String username;
    private String password;
    private String email;
    private List<UserToken> userTokens;
    private SessionToken sessionToken;

    public void addUserToken(UserToken token) {
        //I know i should add to userTokens instead of replacing
        //but even with changed Jackson deserializer to ArrayList for userTokens it still throws
        //UnsupportedOperationException when trying to add
        ArrayList<UserToken> newList;

        if (userTokens != null)
            newList = new ArrayList<>(userTokens);
        else
            newList = new ArrayList<>();

        newList.add(token);
        setUserTokens(newList);
    }

    public boolean deleteUserToken(String token) {
        ArrayList<UserToken> newList = new ArrayList<>(userTokens);
        boolean result = newList.removeIf(userToken -> userToken.getData().equals(token));
        setUserTokens(newList);
        return result;
    }

    public void deleteAllUserTokens() {
        setUserTokens(Collections.emptyList());
    }

    public JsonObject toUserInfo() {
        return new JsonObject()
                .put(usernameKey, username)
                .put(emailKey, email)
                .put(userTokensKey, userTokens);
    }
}
