package com.openklaster.core.vertx.app;

import com.openklaster.common.authentication.password.BCryptPasswordHandler;
import com.openklaster.common.authentication.password.PasswordHandler;
import com.openklaster.common.authentication.tokens.BasicTokenHandler;
import com.openklaster.common.authentication.tokens.TokenHandler;
import com.openklaster.common.config.NestedConfigAccessor;
import com.openklaster.common.model.*;
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
                configureNewLoadMeasurementManager(authenticationClient,userRetriever,loadMeasurementCassandraRepository);
        MeasurementManager<SourceMeasurement> sourceMeasurementMeasurementManager =
                configureNewSourceMeasurementManager(authenticationClient,userRetriever, sourceMeasurementCassandraRepository);

        EndpointService userService = configureUserManagement(authenticationClient, tokenHandler, userCrudRepository);
        EndpointService installationService = configureInstallationManagement(authenticationClient,
                installationCrudRepository, userRetriever);
        EndpointService powerSourceMeasurementService = configureSourceMeasurementService(sourceMeasurementMeasurementManager);
        EndpointService powerLoadMeasurementService = configureLoadMeasurementService(loadMeasurementMeasurementManager);


        userService.configureEndpoints(eventBus);
        this.servicesList = Arrays.asList(userService, installationService, powerSourceMeasurementService,
                powerLoadMeasurementService);//more services to be added here
    }

    private CassandraRepository<SourceMeasurement> configureSourceMeasurementRepository() {
        DbServiceHandler<SourceMeasurement> dbServiceHandler = new DbServiceHandler<>(eventBus,SourceMeasurement.class,
                configAccessor.getString(cassandraSourceMeasurementAddressConfigPath));
        return new CassandraRepository<>(dbServiceHandler);
    }

    private CassandraRepository<LoadMeasurement> configureLoadMeasurementRepository() {
        DbServiceHandler<LoadMeasurement> dbServiceHandler = new DbServiceHandler<>(eventBus,LoadMeasurement.class,
                configAccessor.getString(cassandraLoadMeasurementAddressConfigPath));
        return new CassandraRepository<>(dbServiceHandler);
    }

    private MeasurementManager<SourceMeasurement> configureNewSourceMeasurementManager(AuthenticationClient authenticationClient,
                                                                                       UserRetriever userRetriever,
                                                                                       CassandraRepository<SourceMeasurement> sourceMeasurementCassandraRepository) {
        return new MeasurementManager<>(authenticationClient,userRetriever,SourceMeasurement.class,sourceMeasurementCassandraRepository);
    }


    private MeasurementManager<LoadMeasurement> configureNewLoadMeasurementManager(AuthenticationClient authClient,
                                                                                   UserRetriever userRetriever,
                                                                                   CassandraRepository<LoadMeasurement> loadMeasurementCassandraRepository) {
        return new MeasurementManager<>(authClient,userRetriever,LoadMeasurement.class,loadMeasurementCassandraRepository);
    }

    private CrudRepository<Installation> configureNewInstallationRepository() {
        DbServiceHandler<Installation> installationDbHandler =
                new DbServiceHandler<>(eventBus, Installation.class,
                        configAccessor.getString(mongoInstallationAddressConfigPath));
        return new MongoCrudRepository<>(installationDbHandler);
    }

    private EndpointService configureLoadMeasurementService(
            MeasurementManager<LoadMeasurement> loadMeasurementMeasurementManager) {

        return new MeasurementServiceHandler<LoadMeasurement>(
                configAccessor.getPathConfigAccessor(loadMeasurementConfigPath), loadMeasurementMeasurementManager);
    }

    private EndpointService configureSourceMeasurementService(
            MeasurementManager<SourceMeasurement> sourceMeasurementMeasurementManager) {
        return new MeasurementServiceHandler<SourceMeasurement>(
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
        int charsPerType = config.getInteger("charsPerType");
        int apiTokenLifetime = config.getInteger("apiTokenLifetime");
        int sessionTokenLifetime = config.getInteger("sessionTokenLifetime");

        return new BasicTokenHandler(charsPerType, apiTokenLifetime, sessionTokenLifetime);
    }
}
