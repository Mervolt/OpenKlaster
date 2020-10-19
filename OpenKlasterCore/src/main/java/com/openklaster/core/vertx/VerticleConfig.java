package com.openklaster.core.vertx;

import com.openklaster.common.authentication.password.BCryptPasswordHandler;
import com.openklaster.common.authentication.password.PasswordHandler;
import com.openklaster.common.authentication.tokens.BasicTokenHandler;
import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.model.Installation;
import com.openklaster.common.model.LoadMeasurement;
import com.openklaster.common.model.SourceMeasurement;
import com.openklaster.common.model.User;
import com.openklaster.core.vertx.authentication.AuthenticationClient;
import com.openklaster.core.vertx.authentication.BasicAuthenticationClient;
import com.openklaster.core.vertx.messages.repository.CassandraRepository;
import com.openklaster.core.vertx.messages.repository.CrudRepository;
import com.openklaster.core.vertx.messages.repository.DbServiceHandler;
import com.openklaster.core.vertx.messages.repository.MongoCrudRepository;
import com.openklaster.core.vertx.service.BasicUserRetriever;
import com.openklaster.core.vertx.service.EndpointService;
import com.openklaster.core.vertx.service.UserRetriever;
import com.openklaster.core.vertx.service.installations.InstallationModelManager;
import com.openklaster.core.vertx.service.installations.InstallationServiceHandler;
import com.openklaster.core.vertx.service.measurements.MeasurementManager;
import com.openklaster.core.vertx.service.measurements.MeasurementServiceHandler;
import com.openklaster.core.vertx.service.users.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.FileReader;
import java.io.IOException;

@Configuration
@ComponentScan
public class VerticleConfig {
    private JsonObject jsonObject;
    private JsonObject jsonSecurityConfig;

    public VerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("OpenKlasterCore\\src\\main\\resources\\config-dev.json"));
            JSONObject jsonSimple = (JSONObject) object;
            //noinspection unchecked
            this.jsonObject = new JsonObject(jsonSimple);
            this.jsonSecurityConfig = jsonObject.getJsonObject("security").getJsonObject("tokens");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public TokenHandler basicTokenHandler() {
        int charsPerType = jsonSecurityConfig.getInteger("charsPerType");
        int sessionTokenLifetime = jsonSecurityConfig.getInteger("sessionTokenLifetime");
        return new BasicTokenHandler(charsPerType, sessionTokenLifetime);
    }

    @Bean
    public PasswordHandler bCryptPasswordHandler() {
        return new BCryptPasswordHandler();
    }

    @Lazy
    @Bean
    @Autowired
    public DbServiceHandler<User> userDbServiceHandler(EventBus eventBus) {
        return new DbServiceHandler<>(eventBus, User.class, jsonObject.getJsonObject("eventbus").getJsonObject("out")
                .getJsonObject("mongo").getJsonObject("users").getString("address"));
    }

    @Lazy
    @Bean
    @Autowired
    public CrudRepository<User> userCrudRepository(DbServiceHandler<User> dbServiceHandler) {
        return new MongoCrudRepository<>(dbServiceHandler);
    }

    @Lazy
    @Bean
    @Autowired
    public AuthenticationClient authenticationClient(PasswordHandler passwordHandler, TokenHandler tokenHandler,
                                                     CrudRepository<User> userCrudRepository) {
        return new BasicAuthenticationClient(passwordHandler, tokenHandler, userCrudRepository);
    }

    @Lazy
    @Bean
    @Autowired
    public DbServiceHandler<Installation> installationDbServiceHandler(EventBus eventBus) {
        return new DbServiceHandler<>(eventBus, Installation.class, jsonObject.getJsonObject("eventbus").getJsonObject("out")
                .getJsonObject("mongo").getJsonObject("installation").getString("address"));
    }

    @Lazy
    @Bean
    @Autowired
    public CrudRepository<Installation> installationCrudRepository(DbServiceHandler<Installation> dbServiceHandler) {
        return new MongoCrudRepository<>(dbServiceHandler);
    }

    @Lazy
    @Bean
    @Autowired
    public UserRetriever basicUserRetriever(CrudRepository<User> userRepository, CrudRepository<Installation> installationRepository) {
        return new BasicUserRetriever(userRepository, installationRepository);
    }

    @Lazy
    @Bean
    @Autowired
    public DbServiceHandler<LoadMeasurement> loadMeasurementDbServiceHandler(EventBus eventBus) {
        return new DbServiceHandler<>(eventBus, LoadMeasurement.class, jsonObject.getJsonObject("eventbus").getJsonObject("out")
                .getJsonObject("cassandra").getJsonObject("loadmeasurement").getString("address"));
    }

    @Lazy
    @Bean
    @Autowired
    public CassandraRepository<LoadMeasurement> loadMeasurementCassandraRepository(DbServiceHandler<LoadMeasurement> dbServiceHandler) {
        return new CassandraRepository<>(dbServiceHandler);
    }

    @Lazy
    @Bean
    @Autowired
    public DbServiceHandler<SourceMeasurement> sourceMeasurementDbServiceHandler(EventBus eventBus) {
        return new DbServiceHandler<>(eventBus, SourceMeasurement.class,
                jsonObject.getJsonObject("eventbus").getJsonObject("out").getJsonObject("cassandra")
                        .getJsonObject("sourcemeasurement").getString("address"));
    }

    @Lazy
    @Bean
    @Autowired
    public CassandraRepository<SourceMeasurement> configureSourceMeasurementRepository(DbServiceHandler<SourceMeasurement> dbServiceHandler) {
        return new CassandraRepository<>(dbServiceHandler);
    }

    @Lazy
    @Bean
    @Autowired
    public MeasurementManager<SourceMeasurement> sourceMeasurementMeasurementManager(AuthenticationClient authenticationClient,
                                                                                     UserRetriever userRetriever,
                                                                                     CassandraRepository<SourceMeasurement> sourceMeasurementCassandraRepository) {
        return new MeasurementManager<>(authenticationClient, userRetriever, SourceMeasurement.class, sourceMeasurementCassandraRepository);
    }

    @Lazy
    @Bean
    @Autowired
    public MeasurementManager<LoadMeasurement> loadMeasurementMeasurementManager(AuthenticationClient authClient,
                                                                                 UserRetriever userRetriever,
                                                                                 CassandraRepository<LoadMeasurement> loadMeasurementCassandraRepository) {
        return new MeasurementManager<>(authClient, userRetriever, LoadMeasurement.class, loadMeasurementCassandraRepository);
    }

    @Lazy
    @Bean
    @Autowired
    public InstallationModelManager installationModelManager(AuthenticationClient authenticationClient,
                                                             CrudRepository<Installation> installationCrudRepository,
                                                             UserRetriever userRetriever) {
        return new InstallationModelManager(authenticationClient, installationCrudRepository, userRetriever);
    }

    @Lazy
    @Bean
    @Autowired
    public EndpointService installationEndpointService(InstallationModelManager installationModelManager) {
        return new InstallationServiceHandler(installationModelManager,
                jsonObject.getJsonObject("eventbus").getJsonObject("in").getJsonObject("installations").getString("address"));
    }


    @Lazy
    @Bean
    @Autowired
    public EndpointService userEndpointService(AuthenticationClient authenticationClient, TokenHandler tokenHandler,
                                               CrudRepository<User> userCrudRepository, ManagerContainer managerContainer,
                                               ManagerContainerHelper managerContainerHelper, AuthenticatedUserManager authenticatedUserManager) {
        return new UserManagementHandler(authenticationClient, tokenHandler, userCrudRepository,
                jsonObject.getJsonObject("eventbus").getJsonObject("in").getJsonObject("userManagement").getString("address"),
                managerContainer, managerContainerHelper, authenticatedUserManager);
    }

    @Lazy
    @Bean
    @Autowired
    public EndpointService loadMeasurementEndpointService(MeasurementManager<LoadMeasurement> loadMeasurementMeasurementManager) {
        return new MeasurementServiceHandler<>(loadMeasurementMeasurementManager,
                jsonObject.getJsonObject("eventbus").getJsonObject("in").getJsonObject("loadMeasurements").getString("address"));
    }


    @Lazy
    @Bean
    @Autowired
    public EndpointService sourceMeasurementEndpointService(MeasurementManager<SourceMeasurement> sourceMeasurementMeasurementManager) {
        return new MeasurementServiceHandler<>(sourceMeasurementMeasurementManager,
                jsonObject.getJsonObject("eventbus").getJsonObject("in").getJsonObject("sourceMeasurements").getString("address"));
    }

    @Lazy
    @Bean
    @Autowired
    public RegisterManager registerManager(AuthenticationClient authenticationClient, CrudRepository<User> userCrudRepository) {
        return new RegisterManager(authenticationClient, userCrudRepository);
    }

    @Lazy
    @Bean
    @Autowired
    public LoginManager loginManager(AuthenticationClient authenticationClient, CrudRepository<User> userCrudRepository) {
        return new LoginManager(authenticationClient, userCrudRepository);
    }

    @Lazy
    @Bean
    @Autowired
    public UpdateUserManager updateUserManager(AuthenticationClient authenticationClient, CrudRepository<User> userCrudRepository) {
        return new UpdateUserManager(authenticationClient, userCrudRepository);
    }

    @Bean
    @Autowired
    public InformationManager informationManager() {
        return new InformationManager();
    }

    @Lazy
    @Bean
    @Autowired
    public GenerateTokenManager generateTokenManager(TokenHandler tokenHandler, CrudRepository<User> userCrudRepository) {
        return new GenerateTokenManager(tokenHandler, userCrudRepository);
    }

    @Lazy
    @Bean
    @Autowired
    public DeleteTokenManager deleteTokenManager(CrudRepository<User> userCrudRepository) {
        return new DeleteTokenManager(userCrudRepository);
    }

    @Lazy
    @Bean
    @Autowired
    public DeleteAllTokensManager deleteAllTokensManager(CrudRepository<User> userCrudRepository) {
        return new DeleteAllTokensManager(userCrudRepository);
    }

    @Lazy
    @Bean
    @Autowired
    public ManagerContainer managerContainer(LoginManager loginManager, RegisterManager registerManager,
                                             UpdateUserManager updateUserManager) {
        return new ManagerContainer(loginManager, registerManager, updateUserManager);
    }

    @Lazy
    @Bean
    @Autowired
    public ManagerContainerHelper managerContainerHelper(InformationManager informationManager, GenerateTokenManager generateTokenManager,
                                                         DeleteTokenManager deleteTokenManager, DeleteAllTokensManager deleteAllTokensManager) {
        return new ManagerContainerHelper(informationManager, generateTokenManager, deleteTokenManager, deleteAllTokensManager);
    }

    @Lazy
    @Bean
    @Autowired
    public AuthenticatedUserManager authenticatedUserManager(AuthenticationClient authenticationClient, CrudRepository<User> userCrudRepository) {
        return new AuthenticatedUserManager(authenticationClient, userCrudRepository);
    }
}
