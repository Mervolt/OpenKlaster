package com.openklaster.common.tests.model;

import com.openklaster.common.model.SessionToken;
import com.openklaster.common.model.User;
import com.openklaster.common.model.UserToken;

import java.util.ArrayList;
import java.util.List;

public class UserBuilder {

    private User user;

    public static UserBuilder of(String username) {
        return new UserBuilder(username);
    }

    public UserBuilder setEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder setPassword(String password) {
        user.setPassword(password);
        return this;
    }

    public UserBuilder setSessionToken(SessionToken token) {
        user.setSessionToken(token);
        return this;
    }

    public UserBuilder addApiToken(UserToken apiToken) {
        user.addUserToken(apiToken);
        return this;
    }

    public UserBuilder setApiTokens(List<UserToken> tokens) {
        user.setUserTokens(tokens);
        return this;
    }

    private UserBuilder(String username) {
        user = new User();
        user.setUsername(username);
        user.setUserTokens(new ArrayList<>());
    }

    public User build() {
        return user;
    }
}
