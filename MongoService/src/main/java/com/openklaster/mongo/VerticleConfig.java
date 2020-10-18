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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;

@Configuration
@ComponentScan
public class VerticleConfig {
    private JsonObject jsonConfig;

    public VerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("OpenKlasterAPI\\src\\main\\resources\\config-dev.json"));
            JSONObject jsonSimple = (JSONObject) object;
            //noinspection unchecked
            this.jsonConfig = new JsonObject(jsonSimple);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }


    @Bean
    @Autowired
    public MongoClient mongoClient(Vertx vertx) {
        JsonObject mongoOptions = jsonConfig.getJsonObject("database").getJsonObject("mongo");
        return MongoClient.createShared(vertx, mongoOptions);
    }

    @Bean
    @Autowired
    public MongoPersistenceService mongoPersistenceService(MongoClient mongoClient) {
        return new MongoPersistenceService(mongoClient);
    }


    @Bean
    @Autowired
    public CalculatorConfig calculatorConfig(MongoPersistenceService mongoPersistenceService,
                                             EnergySourceCalculatorParser energySourceCalculatorParser) {
        return new CalculatorConfig(mongoPersistenceService, energySourceCalculatorParser,
                null);
    }

    @Bean
    public EnergySourceCalculatorParser energySourceCalculatorParser() {
        return new EnergySourceCalculatorParser();
    }

    @Bean
    @Autowired
    public InstallationConfig installationConfig(MongoPersistenceService mongoPersistenceService,
                                                 InstallationParser installationParser) {
        return new InstallationConfig(mongoPersistenceService, installationParser, null);
    }

    @Bean
    public InstallationParser installationParser() {
        return new InstallationParser();
    }

    @Bean
    @Autowired
    public UserConfig userConfig(MongoPersistenceService mongoPersistenceService,
                                 UserParser userParser) {
        return new UserConfig(mongoPersistenceService, userParser, null);
    }

    @Bean
    public UserParser userParser() {
        return new UserParser();
    }
}
