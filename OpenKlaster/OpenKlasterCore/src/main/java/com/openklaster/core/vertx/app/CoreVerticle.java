package com.openklaster.core.vertx.app;

import com.openklaster.common.authentication.password.BCryptPasswordHandler;
import com.openklaster.common.authentication.password.PasswordHandler;
import com.openklaster.common.authentication.tokens.BasicTokenHandler;
import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.authentication.BasicAuthenticationClient;
import com.openklaster.core.vertx.messages.repository.MongoRepository;
import com.openklaster.core.vertx.messages.repository.Repository;
import com.openklaster.core.vertx.service.EndpointService;
import com.openklaster.core.vertx.service.UserManagementHandler;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static com.openklaster.core.vertx.app.CoreVerticleProperties.*;

public class CoreVerticle extends AbstractVerticle {

    private ConfigRetriever configRetriever;
    private static final Logger logger = LoggerFactory.getLogger(CoreVerticle.class);
    private EventBus eventBus;
    private NestedConfigAccessor configAccessor;
    private List<EndpointService> servicesList;


    public CoreVerticle(Vertx vertx, ConfigRetriever configRetriever) {
        this.vertx = vertx;
        this.configRetriever = configRetriever;
        this.eventBus = vertx.eventBus();
    }

    @Override
    public void start(Promise<Void> promise) {
        this.configRetriever.getConfig(result -> {
            if (result.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(result.result());
                handlePostConfig(promise);
            } else {
                logger.error("Could not retrieve CoreVerticle config");
                logger.error(result.cause());
                vertx.close();
                promise.complete();
            }
        });
    }

    private void handlePostConfig(Promise<Void> promise) {
        configureEndpoints();
        promise.complete();

    }

    private void configureEndpoints() {
        EndpointService userService = configureUserManagement();
        // Todo
        userService.configureEndpoints(eventBus);
        this.servicesList = Arrays.asList(userService);//more services to be added here
    }

    private EndpointService configureUserManagement() {
        TokenHandler tokenHandler = configureNewTokenHandler(configAccessor.getPathConfigAccessor(tokenConfigPath));
        PasswordHandler passwordHandler = configureNewPasswordHandler();
        Repository<User> userRepository = configureNewUserRepository();
        AuthenticationClient authenticationClient = new BasicAuthenticationClient(passwordHandler,
                tokenHandler, userRepository);
        return new UserManagementHandler(configAccessor.getPathConfigAccessor(userConfigPath),
                authenticationClient, tokenHandler, userRepository);
    }

    private Repository<User> configureNewUserRepository() {
        return new MongoRepository<>(User.class, eventBus, configAccessor.getString(mongoUserConfigPath));
    }

    private PasswordHandler configureNewPasswordHandler() {
        return new BCryptPasswordHandler();
    }

    private TokenHandler configureNewTokenHandler(NestedConfigAccessor config) {
        int charsPerType = config.getInteger("charsPerType");
        int apiTokenLifetime = config.getInteger("apiTokenLifetime");
        int sessionTokenLifetime = config.getInteger("sessionTokenLifetime");

        return new BasicTokenHandler(charsPerType, apiTokenLifetime, sessionTokenLifetime);
    }
}
