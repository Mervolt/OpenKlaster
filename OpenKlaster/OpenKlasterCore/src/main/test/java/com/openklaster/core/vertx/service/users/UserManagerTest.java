package com.openklaster.core.vertx.service.users;

import com.openklaster.common.authentication.password.BCryptPasswordHandler;
import com.openklaster.common.authentication.password.PasswordHandler;
import com.openklaster.common.authentication.tokens.BasicTokenHandler;
import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.model.User;
import com.openklaster.common.tests.model.UserTestUtil;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.authentication.BasicAuthenticationClient;
import com.openklaster.core.vertx.messages.repository.InMemoryCrudRepository;
import com.openklaster.core.vertx.messages.repository.CrudRepository;

public class UserManagerTest {

    protected static final String usernameKey = "username";
    protected static final String passwordKey = "password";
    protected static final String apiTokenKey = "apiToken";
    protected static final String emailKey = "email";
    protected static final String userTokensKey = "userTokens";
    protected static final String sessionTokenKey = "sessionToken";

    protected UserManager userManager;
    protected AuthenticationClient authenticationClient;
    protected CrudRepository<User> userCrudRepository;
    protected PasswordHandler passwordHandler;
    protected TokenHandler tokenHandler;
    protected int tokenHandlerArg = 3;
    protected User testUser;
    protected User existingUser;

    protected void commonSetup() {
        this.userCrudRepository = new InMemoryCrudRepository<>();
        this.passwordHandler = new BCryptPasswordHandler();
        this.tokenHandler = new BasicTokenHandler(tokenHandlerArg, tokenHandlerArg, tokenHandlerArg);
        this.authenticationClient = new BasicAuthenticationClient(passwordHandler, tokenHandler, userCrudRepository);
        this.testUser = UserTestUtil.prepareUser("test");

        repoSetup();
    }

    private void repoSetup() {
        existingUser = UserTestUtil.prepareUser("existing");
        existingUser.setPassword(passwordHandler.hashPassword(existingUser.getPassword()));
        userCrudRepository.add(existingUser);
    }

}