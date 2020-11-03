package com.openklaster.core;

import com.openklaster.common.authentication.password.BCryptPasswordHandler;
import com.openklaster.common.authentication.password.PasswordHandler;
import com.openklaster.common.authentication.tokens.BasicTokenHandler;
import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.model.Installation;
import com.openklaster.common.model.LoadMeasurement;
import com.openklaster.common.model.SourceMeasurement;
import com.openklaster.common.model.User;
import com.openklaster.core.authentication.AuthenticationClient;
import com.openklaster.core.authentication.BasicAuthenticationClient;
import com.openklaster.core.repository.CassandraRepository;
import com.openklaster.core.repository.CrudRepository;
import com.openklaster.core.repository.DbServiceHandler;
import com.openklaster.core.repository.MongoCrudRepository;
import com.openklaster.core.service.BasicUserRetriever;
import com.openklaster.core.service.EndpointService;
import com.openklaster.core.service.UserRetriever;
import com.openklaster.core.service.installations.InstallationModelManager;
import com.openklaster.core.service.installations.InstallationServiceHandler;
import com.openklaster.core.service.measurements.MeasurementManager;
import com.openklaster.core.service.measurements.MeasurementServiceHandler;
import com.openklaster.core.service.users.*;
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
    private static final String SECURITY_PATH = "security";
    private static final String TOKENS_FOR_SECURITY_PATH = "tokens";
    private static final String CHARS_PER_TYPE_FOR_TOKEN_SECURITY_PATH = "charsPerType";
    private static final String SESSION_TOKEN_LIFETIME_FOR_TOKEN_SECURITY_PATH = "sessionTokenLifetime";

    private static final String SOURCE_MEASUREMENT_FOR_CASSANDRA_PATH = "sourcemeasurement";
    private static final String LOAD_MEASUREMENT_FOR_CASSANDRA_PATH = "loadmeasurement";
    private static final String ADDRESS_FOR_MEASUREMENT_CASSANDRA_PATH = "address";

    private static final String USERS_FOR_MONGO_PATH = "users";
    private static final String ADDRESS_FOR_USERS_MONGO_PATH = "address";
    private static final String INSTALLATION_FOR_MONGO_PATH = "installation";
    private static final String ADDRESS_FOR_INSTALLATION_MONGO_PATH = "address";

    private static final String LOAD_MEASUREMENTS_FOR_IN_EVENTBUS_PATH = "loadMeasurements";
    private static final String SOURCE_MEASUREMENTS_FOR_IN_EVENTBUS_PATH = "sourceMeasurements";
    private static final String USER_MANAGEMENT_FOR_IN_EVENTBUS_PATH = "userManagement";
    private static final String INSTALLATIONS_FOR_IN_EVENTBUS_PATH = "installations";
    private static final String ADDRESS_FOR_RESOURCE_IN_EVENTBUS_PATH = "address";


    private JsonObject jsonSecurityConfig;
    private JsonObject jsonMongoConfig;
    private JsonObject jsonCassandraConfig;
    private JsonObject jsonInEventbusConfig;

    public VerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("OpenKlasterCore\\src\\main\\resources\\config-dev.json"));
            JSONObject jsonSimple = (JSONObject) object;
            //noinspection unchecked
            JsonObject jsonObject = new JsonObject(jsonSimple);
            this.jsonSecurityConfig = jsonObject.getJsonObject(SECURITY_PATH).getJsonObject(TOKENS_FOR_SECURITY_PATH);
            this.jsonMongoConfig = jsonObject.getJsonObject("eventbus").getJsonObject("out").getJsonObject("mongo");
            this.jsonCassandraConfig = jsonObject.getJsonObject("eventbus").getJsonObject("out").getJsonObject("cassandra");
            this.jsonInEventbusConfig = jsonObject.getJsonObject("eventbus").getJsonObject("in");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public TokenHandler basicTokenHandler() {
        int charsPerType = jsonSecurityConfig.getInteger(CHARS_PER_TYPE_FOR_TOKEN_SECURITY_PATH);
        int sessionTokenLifetime = jsonSecurityConfig.getInteger(SESSION_TOKEN_LIFETIME_FOR_TOKEN_SECURITY_PATH);
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
        return new DbServiceHandler<>(eventBus, User.class, jsonMongoConfig.getJsonObject(USERS_FOR_MONGO_PATH).getString(ADDRESS_FOR_USERS_MONGO_PATH));
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
        return new DbServiceHandler<>(eventBus, Installation.class, jsonMongoConfig.getJsonObject(INSTALLATION_FOR_MONGO_PATH)
                .getString(ADDRESS_FOR_INSTALLATION_MONGO_PATH));
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
        return new DbServiceHandler<>(eventBus, LoadMeasurement.class,
                jsonCassandraConfig.getJsonObject(LOAD_MEASUREMENT_FOR_CASSANDRA_PATH).getString(ADDRESS_FOR_MEASUREMENT_CASSANDRA_PATH));
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
                jsonCassandraConfig.getJsonObject(SOURCE_MEASUREMENT_FOR_CASSANDRA_PATH).getString(ADDRESS_FOR_MEASUREMENT_CASSANDRA_PATH));
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
                jsonInEventbusConfig.getJsonObject(INSTALLATIONS_FOR_IN_EVENTBUS_PATH).getString(ADDRESS_FOR_RESOURCE_IN_EVENTBUS_PATH));
    }

    @Lazy
    @Bean
    @Autowired
    public EndpointService userEndpointService(AuthenticationClient authenticationClient, TokenHandler tokenHandler,
                                               CrudRepository<User> userCrudRepository, ManagerContainer managerContainer,
                                               ManagerContainerHelper managerContainerHelper, AuthenticatedUserManager authenticatedUserManager) {
        return new UserManagementHandler(authenticationClient, tokenHandler, userCrudRepository,
                jsonInEventbusConfig.getJsonObject(USER_MANAGEMENT_FOR_IN_EVENTBUS_PATH).getString(ADDRESS_FOR_RESOURCE_IN_EVENTBUS_PATH),
                managerContainer, managerContainerHelper, authenticatedUserManager);
    }

    @Lazy
    @Bean
    @Autowired
    public EndpointService loadMeasurementEndpointService(MeasurementManager<LoadMeasurement> loadMeasurementMeasurementManager) {
        return new MeasurementServiceHandler<>(loadMeasurementMeasurementManager,
                jsonInEventbusConfig.getJsonObject(LOAD_MEASUREMENTS_FOR_IN_EVENTBUS_PATH).getString(ADDRESS_FOR_RESOURCE_IN_EVENTBUS_PATH));
    }


    @Lazy
    @Bean
    @Autowired
    public EndpointService sourceMeasurementEndpointService(MeasurementManager<SourceMeasurement> sourceMeasurementMeasurementManager) {
        return new MeasurementServiceHandler<>(sourceMeasurementMeasurementManager,
                jsonInEventbusConfig.getJsonObject(SOURCE_MEASUREMENTS_FOR_IN_EVENTBUS_PATH).getString(ADDRESS_FOR_RESOURCE_IN_EVENTBUS_PATH));
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