package com.openklaster.mongo;

import com.openklaster.mongo.config.CalculatorConfig;
import com.openklaster.mongo.config.InstallationConfig;
import com.openklaster.mongo.config.UserConfig;
import com.openklaster.mongo.parser.EnergySourceCalculatorParser;
import com.openklaster.mongo.parser.InstallationParser;
import com.openklaster.mongo.parser.UserParser;
import com.openklaster.mongo.service.MongoPersistenceService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import java.io.FileReader;
import java.io.IOException;

@Configuration
@ComponentScan
public class VerticleConfig {
    private static final String DATABASE_PATH = "database";
    private static final String MONGO_FOR_DATABASE_PATH = "mongo";
    private static final String CALCULATOR_PATH = "calculator";
    private static final String INSTALLATION_PATH = "installation";
    private static final String USER_PATH = "user";
    private static final String COLLECTION_FOR_USER_PATH = "mongo";
    private static final String COLLECTION_NAME_FOR_USER_PATH = "collectionName";
    private static final String EVENTBUS_FOR_USER_PATH = "bus";
    private static final String EVENTBUS_ADDRESS_FOR_USER_PATH = "address";
    private JsonObject jsonConfig;

    public VerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("MongoService\\src\\main\\resources\\config-dev.json"));
            JSONObject jsonSimple = (JSONObject) object;
            //noinspection unchecked
            this.jsonConfig = new JsonObject(jsonSimple);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Lazy
    @Bean
    @Autowired
    public MongoClient mongoClient(Vertx vertx) {
        JsonObject mongoOptions = jsonConfig.getJsonObject(DATABASE_PATH).getJsonObject(MONGO_FOR_DATABASE_PATH);
        return MongoClient.createShared(vertx, mongoOptions);
    }

    @Lazy
    @Bean
    @Autowired
    public MongoPersistenceService mongoPersistenceService(MongoClient mongoClient) {
        return new MongoPersistenceService(mongoClient);
    }

    @Lazy
    @Bean
    @Autowired
    public CalculatorConfig calculatorConfig(MongoPersistenceService mongoPersistenceService,
                                             EnergySourceCalculatorParser energySourceCalculatorParser) {
        JsonObject jsonConfigCalculator = jsonConfig.getJsonObject(CALCULATOR_PATH);
        return new CalculatorConfig(mongoPersistenceService, energySourceCalculatorParser,
                retrieveCollectionNameFromEntityConfig(jsonConfigCalculator),
                retrieveBusAddressFromEntityConfig(jsonConfigCalculator));
    }

    @Bean
    public EnergySourceCalculatorParser energySourceCalculatorParser() {
        return new EnergySourceCalculatorParser();
    }

    @Lazy
    @Bean
    @Autowired
    public InstallationConfig installationConfig(MongoPersistenceService mongoPersistenceService,
                                                 InstallationParser installationParser) {
        JsonObject jsonConfigInstallation = jsonConfig.getJsonObject(INSTALLATION_PATH);
        return new InstallationConfig(mongoPersistenceService, installationParser,
                retrieveCollectionNameFromEntityConfig(jsonConfigInstallation),
                retrieveBusAddressFromEntityConfig(jsonConfigInstallation));
    }

    @Bean
    public InstallationParser installationParser() {
        return new InstallationParser();
    }

    @Lazy
    @Bean
    @Autowired
    public UserConfig userConfig(MongoPersistenceService mongoPersistenceService,
                                 UserParser userParser) {
        JsonObject jsonConfigUser = jsonConfig.getJsonObject(USER_PATH);
        return new UserConfig(mongoPersistenceService, userParser,
                retrieveCollectionNameFromEntityConfig(jsonConfigUser),
                retrieveBusAddressFromEntityConfig(jsonConfigUser));
    }

    @Bean
    public UserParser userParser() {
        return new UserParser();
    }

    private String retrieveCollectionNameFromEntityConfig(JsonObject jsonObject) {
        return jsonObject.getJsonObject(COLLECTION_FOR_USER_PATH).getString(COLLECTION_NAME_FOR_USER_PATH);
    }

    private String retrieveBusAddressFromEntityConfig(JsonObject jsonObject) {
        return jsonObject.getJsonObject(EVENTBUS_FOR_USER_PATH).getString(EVENTBUS_ADDRESS_FOR_USER_PATH);
    }

}
