package com.openklaster.core.service.users;

import com.openklaster.common.authentication.password.BCryptPasswordHandler;
import com.openklaster.common.authentication.password.PasswordHandler;
import com.openklaster.common.authentication.tokens.BasicTokenHandler;
import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.model.SessionToken;
import com.openklaster.common.model.User;
import com.openklaster.common.tests.model.UserTestUtil;
import com.openklaster.core.authentication.AuthenticationClient;
import com.openklaster.core.authentication.BasicAuthenticationClient;
import com.openklaster.core.repository.InMemoryCrudRepository;
import com.openklaster.core.repository.CrudRepository;

import java.time.LocalDateTime;

public class UserManagerTest {

    protected static final String usernameKey = "username";
    protected static final String passwordKey = "password";
    protected static final String apiTokenKey = "apiToken";
    protected static final String emailKey = "email";
    protected static final String userTokensKey = "userTokens";
    protected static final String sessionTokenKey = "sessionToken";

    protected AuthenticatedUserManager authenticatedUserManager;
    protected AuthenticationClient authenticationClient;
    protected CrudRepository<User> userCrudRepository;
    protected PasswordHandler passwordHandler;
    protected TokenHandler tokenHandler;
    protected int tokenHandlerArg = 3;
    protected User testUser;
    protected User existingUser;

    protected  void commonSetup() {
        this.userCrudRepository = new InMemoryCrudRepository<>();
        this.passwordHandler = new BCryptPasswordHandler();
        this.tokenHandler = new BasicTokenHandler(tokenHandlerArg, tokenHandlerArg);
        this.authenticationClient = new BasicAuthenticationClient(passwordHandler, tokenHandler, userCrudRepository,  "techinicalToken");
        this.testUser = UserTestUtil.prepareUser("test");
        this.authenticatedUserManager = new AuthenticatedUserManager(authenticationClient, userCrudRepository);
        prepareAuthManager();
        repoSetup();
    }

    private void repoSetup() {
        existingUser = UserTestUtil.prepareUser("existing");
        existingUser.setSessionToken(new SessionToken("session", LocalDateTime.now().plusMinutes(1)));
        existingUser.setPassword(passwordHandler.hashPassword(existingUser.getPassword()));
        userCrudRepository.add(existingUser);
    }

    private void prepareAuthManager(){
        DeleteAllTokensManager deleteAllTokensManager = new DeleteAllTokensManager(userCrudRepository);
        this.authenticatedUserManager.addMethodHelper(deleteAllTokensManager.getMethodName(),deleteAllTokensManager);

        DeleteTokenManager deleteTokenManager = new DeleteTokenManager(userCrudRepository);
        this.authenticatedUserManager.addMethodHelper(deleteTokenManager.getMethodName(),deleteTokenManager);

        GenerateTokenManager generateTokenManager = new GenerateTokenManager(tokenHandler, userCrudRepository);
        this.authenticatedUserManager.addMethodHelper(generateTokenManager.getMethodName(),generateTokenManager);

        InformationManager informationManager = new InformationManager();
        this.authenticatedUserManager.addMethodHelper(informationManager.getMethodName(),informationManager);
    }
}
