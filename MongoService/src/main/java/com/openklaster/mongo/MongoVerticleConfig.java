package com.openklaster.mongo;

import com.openklaster.common.SuperVerticleConfig;
import com.openklaster.mongo.config.CalculatorConfig;
import com.openklaster.mongo.config.InstallationConfig;
import com.openklaster.mongo.config.UserConfig;
import com.openklaster.mongo.configUtil.MongoHolder;
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
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Configuration
@ComponentScan
public class MongoVerticleConfig extends SuperVerticleConfig {
    private JsonObject jsonConfig;

    public MongoVerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            File configFile = new ClassPathResource(configPath).getFile();
            Object object = parser.parse(new FileReader(configFile));
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
        JsonObject mongoOptions = jsonConfig.getJsonObject(MongoHolder.DATABASE_PATH).getJsonObject(MongoHolder.MONGO_FOR_DATABASE_PATH);
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
        JsonObject jsonConfigCalculator = jsonConfig.getJsonObject(MongoHolder.CALCULATOR_PATH);
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
        JsonObject jsonConfigInstallation = jsonConfig.getJsonObject(MongoHolder.INSTALLATION_PATH);
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
        JsonObject jsonConfigUser = jsonConfig.getJsonObject(MongoHolder.USER_PATH);
        return new UserConfig(mongoPersistenceService, userParser,
                retrieveCollectionNameFromEntityConfig(jsonConfigUser),
                retrieveBusAddressFromEntityConfig(jsonConfigUser));
    }

    @Bean
    public UserParser userParser() {
        return new UserParser();
    }

    private String retrieveCollectionNameFromEntityConfig(JsonObject jsonObject) {
        return jsonObject.getJsonObject(MongoHolder.COLLECTION_FOR_USER_PATH).getString(MongoHolder.COLLECTION_NAME_FOR_USER_PATH);
    }

    private String retrieveBusAddressFromEntityConfig(JsonObject jsonObject) {
        return jsonObject.getJsonObject(MongoHolder.EVENTBUS_FOR_USER_PATH).getString(MongoHolder.EVENTBUS_ADDRESS_FOR_USER_PATH);
    }

}
