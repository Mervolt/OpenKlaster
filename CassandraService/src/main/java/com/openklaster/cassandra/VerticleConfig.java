package com.openklaster.cassandra;

import com.openklaster.cassandra.properties.CassandraProperties;
import com.openklaster.cassandra.service.EnergyPredictionsHandler;
import com.openklaster.cassandra.service.LoadMeasurementHandler;
import com.openklaster.cassandra.service.SourceMeasurementHandler;
import com.openklaster.cassandra.service.WeatherConditionsHandler;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.Vertx;
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
import java.util.Collections;

@Configuration
@ComponentScan
public class VerticleConfig {
    private JsonObject jsonConfig;
    private JsonObject jsonCassandraConfig;

    public VerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("CassandraService\\src\\main\\resources\\config-dev.json"));
            JSONObject jsonSimple = (JSONObject) object;
            //noinspection unchecked
            this.jsonConfig = new JsonObject(jsonSimple);
            this.jsonCassandraConfig = jsonConfig.getJsonObject("cassandra");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Lazy
    @Bean
    public CassandraClient cassandraClient(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") Vertx vertx) {
        CassandraClientOptions options = new CassandraClientOptions()
                .setPort(jsonCassandraConfig.getInteger("port"))
                .setKeyspace(jsonCassandraConfig.getString("keyspace"))
                .setContactPoints(Collections.singletonList(jsonCassandraConfig.getString("host")));

        return CassandraClient.create(vertx, options);
    }

    @Lazy
    @Bean
    @Autowired
    public LoadMeasurementHandler loadMeasurementHandler(CassandraClient cassandraClient) {
        return new LoadMeasurementHandler(cassandraClient, jsonConfig.getJsonObject("loadmeasurement"));
    }

    @Lazy
    @Bean
    @Autowired
    public SourceMeasurementHandler sourceMeasurementHandler(CassandraClient cassandraClient) {
        return new SourceMeasurementHandler(cassandraClient, jsonConfig.getJsonObject("loadmeasurement"));
    }

    @Lazy
    @Bean
    @Autowired
    public EnergyPredictionsHandler energyPredictionsHandler(CassandraClient cassandraClient) {
        return new EnergyPredictionsHandler(cassandraClient, jsonConfig.getJsonObject("loadmeasurement"));
    }

    @Lazy
    @Bean
    @Autowired
    public WeatherConditionsHandler weatherConditionsHandler(CassandraClient cassandraClient) {
        return new WeatherConditionsHandler(cassandraClient, jsonConfig.getJsonObject("loadmeasurement"));
    }
}
