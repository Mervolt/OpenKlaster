package com.openklaster.cassandra;

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
    private static final String CASSANDRA_PATH = "cassandra";
    private static final String PORT_FOR_CASSANDRA_PATH = "port";
    private static final String KEYSPACE_FOR_CASSANDRA_PATH = "keyspace";
    private static final String HOST_FOR_CASSANDRA_PATH = "host";

    private static final String LOAD_MEASUREMENT_PATH = "loadmeasurement";
    private static final String SOURCE_MEASUREMENT_PATH = "sourcemeasurement";
    private static final String ENERGY_PREDICTIONS_PATH = "energypredictions";
    private static final String WEATHER_CONDITIONS_PATH = "weatherconditions";

    private static final String ADDRESS_FOR_RESOURCE_PATH = "address";
    private static final String TABLE_FOR_RESOURCE_PATH = "table";


    private JsonObject jsonConfig;
    private JsonObject jsonCassandraConfig;

    public VerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("CassandraService\\src\\main\\resources\\config-dev.json"));
            JSONObject jsonSimple = (JSONObject) object;
            //noinspection unchecked
            this.jsonConfig = new JsonObject(jsonSimple);
            this.jsonCassandraConfig = jsonConfig.getJsonObject(CASSANDRA_PATH);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Lazy
    @Bean
    public CassandraClient cassandraClient(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") Vertx vertx) {
        CassandraClientOptions options = new CassandraClientOptions()
                .setPort(jsonCassandraConfig.getInteger(PORT_FOR_CASSANDRA_PATH))
                .setKeyspace(jsonCassandraConfig.getString(KEYSPACE_FOR_CASSANDRA_PATH))
                .setContactPoints(Collections.singletonList(jsonCassandraConfig.getString(HOST_FOR_CASSANDRA_PATH)));

        return CassandraClient.create(vertx, options);
    }

    @Lazy
    @Bean
    @Autowired
    public LoadMeasurementHandler loadMeasurementHandler(CassandraClient cassandraClient) {
        return new LoadMeasurementHandler(cassandraClient, jsonConfig.getJsonObject(LOAD_MEASUREMENT_PATH).getString(ADDRESS_FOR_RESOURCE_PATH),
                jsonConfig.getJsonObject(LOAD_MEASUREMENT_PATH).getString(TABLE_FOR_RESOURCE_PATH));
    }

    @Lazy
    @Bean
    @Autowired
    public SourceMeasurementHandler sourceMeasurementHandler(CassandraClient cassandraClient) {
        return new SourceMeasurementHandler(cassandraClient, jsonConfig.getJsonObject(SOURCE_MEASUREMENT_PATH).getString(ADDRESS_FOR_RESOURCE_PATH),
                jsonConfig.getJsonObject(SOURCE_MEASUREMENT_PATH).getString(TABLE_FOR_RESOURCE_PATH));
    }

    @Lazy
    @Bean
    @Autowired
    public EnergyPredictionsHandler energyPredictionsHandler(CassandraClient cassandraClient) {
        return new EnergyPredictionsHandler(cassandraClient, jsonConfig.getJsonObject(ENERGY_PREDICTIONS_PATH).getString(ADDRESS_FOR_RESOURCE_PATH),
                jsonConfig.getJsonObject(ENERGY_PREDICTIONS_PATH).getString(TABLE_FOR_RESOURCE_PATH));
    }

    @Lazy
    @Bean
    @Autowired
    public WeatherConditionsHandler weatherConditionsHandler(CassandraClient cassandraClient) {
        return new WeatherConditionsHandler(cassandraClient, jsonConfig.getJsonObject(WEATHER_CONDITIONS_PATH).getString(ADDRESS_FOR_RESOURCE_PATH),
                jsonConfig.getJsonObject(WEATHER_CONDITIONS_PATH).getString(TABLE_FOR_RESOURCE_PATH));
    }
}
