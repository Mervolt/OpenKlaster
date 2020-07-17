package com.openklaster.core.vertx.service;

import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.messages.repository.Repository;
import com.openklaster.core.vertx.service.users.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;

public class UserManagementHandler extends EndpointService {

    //Note that new User Manager should be added to this map in order to work
    private Map<String, UserManager> userManagerMap;
    private final AuthenticationClient authenticationClient;
    private final TokenHandler tokenHandler;
    private final Repository<User> userRepository;

    public UserManagementHandler(NestedConfigAccessor config, AuthenticationClient authenticationClient,
                                 TokenHandler tokenHandler, Repository<User> userRepository) {
        super(config);
        logger = LoggerFactory.getLogger(UserManagementHandler.class);
        prepareManagers();
        this.authenticationClient = authenticationClient;
        this.tokenHandler = tokenHandler;
        this.userRepository = userRepository;
    }

    private void prepareManagers() {
        this.userManagerMap = new HashMap<>();

        RegisterManager registerManager = new RegisterManager(authenticationClient, userRepository);
        userManagerMap.put(registerManager.getMethodsName(), registerManager);

        LoginManager loginManager = new LoginManager(authenticationClient, userRepository);
        userManagerMap.put(loginManager.getMethodsName(), loginManager);

        InformationManager informationManager = new InformationManager(authenticationClient, userRepository);
        userManagerMap.put(informationManager.getMethodsName(), informationManager);

        GenerateTokenManager generateTokenManager = new GenerateTokenManager(authenticationClient, tokenHandler, userRepository);
        userManagerMap.put(generateTokenManager.getMethodsName(), generateTokenManager);

        DeleteTokenManager deleteTokenManager = new DeleteTokenManager(authenticationClient, userRepository);
        userManagerMap.put(deleteTokenManager.getMethodsName(), deleteTokenManager);

        DeleteAllTokensManager deleteAllTokensManager = new DeleteAllTokensManager(authenticationClient, userRepository);
        userManagerMap.put(deleteAllTokensManager.getMethodsName(), deleteAllTokensManager);
    }

    @Override
    public void configureEndpoints(EventBus eventBus) {
        MessageConsumer<JsonObject> consumer = eventBus.consumer(getEventBusAddress());
        consumer.handler(this::handlerMap);
    }

    private void handlerMap(Message<JsonObject> message) {
        String methodName = message.headers().get(METHOD_KEY);
        UserManager manager = userManagerMap.get(methodName);
        if (manager == null) {
            handleUnknownMethod(message, methodName);
        } else {
            manager.handleMessage(message);
        }
    }
}
