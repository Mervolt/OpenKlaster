package com.openklaster.core.vertx.app;

import com.openklaster.common.authentication.password.BCryptPasswordHandler;
import com.openklaster.common.authentication.password.PasswordHandler;
import com.openklaster.common.authentication.tokens.BasicTokenHandler;
import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.config.ConfigFilesManager;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.model.Installation;
import com.openklaster.common.model.LoadMeasurement;
import com.openklaster.common.model.SourceMeasurement;
import com.openklaster.common.model.User;
import com.openklaster.common.verticle.OpenklasterVerticle;
import com.openklaster.core.vertx.VerticleConfig;
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
import com.openklaster.core.vertx.service.users.UserManagementHandler;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.List;

import static com.openklaster.core.vertx.app.CoreVerticleProperties.*;

public class CoreVerticle extends OpenklasterVerticle {

    private static final Logger logger = LoggerFactory.getLogger(CoreVerticle.class);
    private EventBus eventBus;
    private NestedConfigAccessor configAccessor;
    private List<EndpointService> servicesList;
    private ApplicationContext ctx;

    public CoreVerticle() {
        super();
    }

    public CoreVerticle(boolean isDevModeOn) {
        super(isDevModeOn);
    }

    @Override
    public void init(Vertx vertx, Context context) {
        ctx = new AnnotationConfigApplicationContext(VerticleConfig.class);
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();

        ConfigFilesManager configFilesManager = new ConfigFilesManager(this.configFilenamePrefix);
        configFilesManager.getConfig(vertx).getConfig(result -> {
            if (result.succeeded()) {
                this.configAccessor = new NestedConfigAccessor(result.result());
                handlePostConfig();
            } else {
                logger.error("Could not retrieve CoreVerticle config");
                logger.error(result.cause().getMessage());
                vertx.close();
            }
        });
    }

    private void handlePostConfig() {
        configureEndpoints();
    }

    //TODO DI, some beans to configure ??? It will be hard as some things depends on configs which are loaded
    // some time after verticle starts
    private void configureEndpoints() {
        TokenHandler tokenHandler = configureNewTokenHandler(configAccessor.getPathConfigAccessor(tokenConfigPath));
        PasswordHandler passwordHandler = configureNewPasswordHandler();
        CrudRepository<User> userCrudRepository = configureNewUserRepository();
        AuthenticationClient authenticationClient = new BasicAuthenticationClient(passwordHandler,
                tokenHandler, userCrudRepository);
        CrudRepository<Installation> installationCrudRepository = configureNewInstallationRepository();
        UserRetriever userRetriever = new BasicUserRetriever(userCrudRepository, installationCrudRepository);
        CassandraRepository<LoadMeasurement> loadMeasurementCassandraRepository = configureLoadMeasurementRepository();
        CassandraRepository<SourceMeasurement> sourceMeasurementCassandraRepository = configureSourceMeasurementRepository();

        MeasurementManager<LoadMeasurement> loadMeasurementMeasurementManager =
                configureNewLoadMeasurementManager(authenticationClient, userRetriever, loadMeasurementCassandraRepository);
        MeasurementManager<SourceMeasurement> sourceMeasurementMeasurementManager =
                configureNewSourceMeasurementManager(authenticationClient, userRetriever, sourceMeasurementCassandraRepository);

        EndpointService userService = configureUserManagement(authenticationClient, tokenHandler, userCrudRepository);
        EndpointService installationService = configureInstallationManagement(authenticationClient,
                installationCrudRepository, userRetriever);
        EndpointService powerSourceMeasurementService = configureSourceMeasurementService(sourceMeasurementMeasurementManager);
        EndpointService powerLoadMeasurementService = configureLoadMeasurementService(loadMeasurementMeasurementManager);


        userService.configureEndpoints(eventBus);
        installationService.configureEndpoints(eventBus);
        powerSourceMeasurementService.configureEndpoints(eventBus);
        powerLoadMeasurementService.configureEndpoints(eventBus);
        this.servicesList = Arrays.asList(userService, installationService, powerSourceMeasurementService,
                powerLoadMeasurementService);//more services to be added here
    }

    private CassandraRepository<SourceMeasurement> configureSourceMeasurementRepository() {
        DbServiceHandler<SourceMeasurement> dbServiceHandler = new DbServiceHandler<>(eventBus, SourceMeasurement.class,
                configAccessor.getString(cassandraSourceMeasurementAddressConfigPath));
        return new CassandraRepository<>(dbServiceHandler);
    }

    private CassandraRepository<LoadMeasurement> configureLoadMeasurementRepository() {
        DbServiceHandler<LoadMeasurement> dbServiceHandler = new DbServiceHandler<>(eventBus, LoadMeasurement.class,
                configAccessor.getString(cassandraLoadMeasurementAddressConfigPath));
        return new CassandraRepository<>(dbServiceHandler);
    }

    private MeasurementManager<SourceMeasurement> configureNewSourceMeasurementManager(AuthenticationClient authenticationClient,
                                                                                       UserRetriever userRetriever,
                                                                                       CassandraRepository<SourceMeasurement> sourceMeasurementCassandraRepository) {
        return new MeasurementManager<>(authenticationClient, userRetriever, SourceMeasurement.class, sourceMeasurementCassandraRepository);
    }


    private MeasurementManager<LoadMeasurement> configureNewLoadMeasurementManager(AuthenticationClient authClient,
                                                                                   UserRetriever userRetriever,
                                                                                   CassandraRepository<LoadMeasurement> loadMeasurementCassandraRepository) {
        return new MeasurementManager<>(authClient, userRetriever, LoadMeasurement.class, loadMeasurementCassandraRepository);
    }

    private CrudRepository<Installation> configureNewInstallationRepository() {
        DbServiceHandler<Installation> installationDbHandler =
                new DbServiceHandler<>(eventBus, Installation.class,
                        configAccessor.getString(mongoInstallationAddressConfigPath));
        return new MongoCrudRepository<>(installationDbHandler);
    }

    private EndpointService configureLoadMeasurementService(
            MeasurementManager<LoadMeasurement> loadMeasurementMeasurementManager) {

        return new MeasurementServiceHandler<>(
                configAccessor.getPathConfigAccessor(loadMeasurementConfigPath), loadMeasurementMeasurementManager);
    }

    private EndpointService configureSourceMeasurementService(
            MeasurementManager<SourceMeasurement> sourceMeasurementMeasurementManager) {
        return new MeasurementServiceHandler<>(
                configAccessor.getPathConfigAccessor(sourceMeasurementConfigPath), sourceMeasurementMeasurementManager);
    }

    private EndpointService configureInstallationManagement(AuthenticationClient authenticationClient,
                                                            CrudRepository<Installation> installationCrudRepository,
                                                            UserRetriever userRetriever) {
        InstallationModelManager installationModelManager =
                new InstallationModelManager(authenticationClient, installationCrudRepository, userRetriever);
        return new InstallationServiceHandler(configAccessor.getPathConfigAccessor(installationConfigPath),
                installationModelManager);
    }

    private EndpointService configureUserManagement(AuthenticationClient authenticationClient, TokenHandler tokenHandler,
                                                    CrudRepository<User> userCrudRepository) {
        return new UserManagementHandler(configAccessor.getPathConfigAccessor(userConfigPath),
                authenticationClient, tokenHandler, userCrudRepository);
    }

    private CrudRepository<User> configureNewUserRepository() {
        DbServiceHandler<User> userDbHandler =
                new DbServiceHandler<>(eventBus, User.class, configAccessor.getString(mongoUserAddressConfigPath));
        return new MongoCrudRepository<>(userDbHandler);
    }

    private PasswordHandler configureNewPasswordHandler() {
        return new BCryptPasswordHandler();
    }

    private TokenHandler configureNewTokenHandler(NestedConfigAccessor config) {
        int charsPerType = config.getInteger(tokenGeneratorCharsPerTypeKey);
        int sessionTokenLifetime = config.getInteger(sessionTokenLifetimeKey);

        return new BasicTokenHandler(charsPerType, sessionTokenLifetime);
    }
}
