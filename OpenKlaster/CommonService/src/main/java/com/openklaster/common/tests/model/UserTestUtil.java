package com.openklaster.common.tests.model;

import com.openklaster.common.authentication.ITokenGenerator;
import com.openklaster.common.authentication.TokenGenerator;
import com.openklaster.common.model.User;
import com.openklaster.common.model.UserToken;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class UserTestUtil {

    public static User prepareUser(String name) {
        User testUser = new User();
        testUser.setUsername(name);
        testUser.setPassword("1234");
        testUser.setEmail("test@test.com");
        testUser.setUserTokens(prepareTokens());
        return testUser;
    }

    private static List<UserToken> prepareTokens() {
        return Arrays.asList(prepareToken(), prepareToken(), prepareToken());
    }

    private static UserToken prepareToken() {
        ITokenGenerator generator = new TokenGenerator();

        return new UserToken(generator.generateToken(3), LocalDate.now());
    }
}
