package com.openklaster.core.vertx.service.users;

import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.messages.repository.CrudRepository;
import com.openklaster.core.vertx.service.AuthManager;
import com.openklaster.core.vertx.service.EndpointService;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.openklaster.common.messages.BusMessageReplyUtils.METHOD_KEY;

public class UserManagementHandler extends EndpointService {

    private AuthenticatedUserManager authUserManager;
    //Note that new User Manager should be added to this map in order to work
    private Map<String, UserManager> userManagerMap;
    private final AuthenticationClient authenticationClient;
    private final TokenHandler tokenHandler;
    private final CrudRepository<User> userCrudRepository;
    private final ManagerContainer managers;
    private final ManagerContainerHelper managerHelpers;

    public UserManagementHandler(AuthenticationClient authenticationClient, TokenHandler tokenHandler,
                                 CrudRepository<User> userCrudRepository, String busAddress, ManagerContainer managers,
                                 ManagerContainerHelper managerHelpers, AuthenticatedUserManager authManager) {
        super(busAddress);
        logger = LoggerFactory.getLogger(UserManagementHandler.class);

        this.authenticationClient = authenticationClient;
        this.tokenHandler = tokenHandler;
        this.userCrudRepository = userCrudRepository;
        this.managers = managers;
        this.managerHelpers = managerHelpers;
        this.authUserManager = authManager;
        prepareManagers();
    }

    private void prepareManagers() {
        this.userManagerMap = new HashMap<>();

        managers.retrieveManagers().forEach(manager -> userManagerMap.put(manager.getMethodName(), manager));
        managerHelpers.retrieveManagerHelpers().forEach(managerHelper -> authUserManager.addMethodHelper(managerHelper.getMethodName(), managerHelper));
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
            if (!authUserManager.hasMessageHandler(methodName)) {
                handleUnknownMethod(message, methodName);
            } else {
                authUserManager.handleMessage(message, methodName);
            }
        } else {
            manager.handleMessage(message);
        }
    }
}
