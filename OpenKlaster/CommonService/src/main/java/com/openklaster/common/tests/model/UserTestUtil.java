package com.openklaster.common.tests.model;

import com.openklaster.common.model.User;
import com.openklaster.common.model.UserToken;
import org.apache.commons.lang3.RandomStringUtils;

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
        return new UserToken(RandomStringUtils.random(12), LocalDate.now());
    }
}
