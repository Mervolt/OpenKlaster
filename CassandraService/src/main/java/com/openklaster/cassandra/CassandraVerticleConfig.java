package com.openklaster.cassandra;

import com.openklaster.cassandra.configUtil.CassandraLaunchHolder;
import com.openklaster.cassandra.configUtil.CassandraResourcesHolder;
import com.openklaster.cassandra.service.EnergyPredictionsHandler;
import com.openklaster.cassandra.service.LoadMeasurementHandler;
import com.openklaster.cassandra.service.SourceMeasurementHandler;
import com.openklaster.cassandra.service.WeatherConditionsHandler;
import com.openklaster.common.SuperVerticleConfig;
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
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Collections;
import java.util.Optional;

@Configuration
@ComponentScan
public class CassandraVerticleConfig extends SuperVerticleConfig {
    private JsonObject jsonConfig;
    private JsonObject jsonCassandraConfig;

    public CassandraVerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            InputStream configStream = new ClassPathResource(configPath).getInputStream();
            Object object = parser.parse(new InputStreamReader(configStream));
            JSONObject jsonSimple = (JSONObject) object;
            //noinspection unchecked
            this.jsonConfig = new JsonObject(jsonSimple);

            Optional<String> cassandraDbConfig = Optional.ofNullable(System.getenv(CassandraLaunchHolder.CASSANDRA_DB));
            this.jsonCassandraConfig = cassandraDbConfig.map(JsonObject::new)
                    .orElseGet(() -> jsonConfig.getJsonObject(CassandraLaunchHolder.CASSANDRA_PATH));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Lazy
    @Bean
    public CassandraClient cassandraClient(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") Vertx vertx) {
        CassandraClientOptions options = new CassandraClientOptions()
                .setPort(jsonCassandraConfig.getInteger(CassandraLaunchHolder.PORT_FOR_CASSANDRA_PATH))
                .setKeyspace(jsonCassandraConfig.getString(CassandraLaunchHolder.KEYSPACE_FOR_CASSANDRA_PATH))
                .setContactPoints(Collections.singletonList(jsonCassandraConfig.getString(CassandraLaunchHolder.HOST_FOR_CASSANDRA_PATH)));

        return CassandraClient.create(vertx, options);
    }

    @Lazy
    @Bean
    @Autowired
    public LoadMeasurementHandler loadMeasurementHandler(CassandraClient cassandraClient) {
        return new LoadMeasurementHandler(cassandraClient, jsonConfig.getJsonObject(CassandraResourcesHolder.LOAD_MEASUREMENT_PATH)
                .getString(CassandraResourcesHolder.ADDRESS_FOR_RESOURCE_PATH),
                jsonConfig.getJsonObject(CassandraResourcesHolder.LOAD_MEASUREMENT_PATH).getString(CassandraResourcesHolder.TABLE_FOR_RESOURCE_PATH));
    }

    @Lazy
    @Bean
    @Autowired
    public SourceMeasurementHandler sourceMeasurementHandler(CassandraClient cassandraClient) {
        return new SourceMeasurementHandler(cassandraClient, jsonConfig.getJsonObject(CassandraResourcesHolder.SOURCE_MEASUREMENT_PATH)
                .getString(CassandraResourcesHolder.ADDRESS_FOR_RESOURCE_PATH),
                jsonConfig.getJsonObject(CassandraResourcesHolder.SOURCE_MEASUREMENT_PATH).getString(CassandraResourcesHolder.TABLE_FOR_RESOURCE_PATH));
    }

    @Lazy
    @Bean
    @Autowired
    public EnergyPredictionsHandler energyPredictionsHandler(CassandraClient cassandraClient) {
        return new EnergyPredictionsHandler(cassandraClient, jsonConfig.getJsonObject(CassandraResourcesHolder.ENERGY_PREDICTIONS_PATH)
                .getString(CassandraResourcesHolder.ADDRESS_FOR_RESOURCE_PATH),
                jsonConfig.getJsonObject(CassandraResourcesHolder.ENERGY_PREDICTIONS_PATH).getString(CassandraResourcesHolder.TABLE_FOR_RESOURCE_PATH));
    }

    @Lazy
    @Bean
    @Autowired
    public WeatherConditionsHandler weatherConditionsHandler(CassandraClient cassandraClient) {
        return new WeatherConditionsHandler(cassandraClient, jsonConfig.getJsonObject(CassandraResourcesHolder.WEATHER_CONDITIONS_PATH)
                .getString(CassandraResourcesHolder.ADDRESS_FOR_RESOURCE_PATH),
                jsonConfig.getJsonObject(CassandraResourcesHolder.WEATHER_CONDITIONS_PATH).getString(CassandraResourcesHolder.TABLE_FOR_RESOURCE_PATH));
    }
}
