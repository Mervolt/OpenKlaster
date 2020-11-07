package com.openklaster.filerepository;

import com.openklaster.common.SuperVerticleConfig;
import com.openklaster.filerepository.properties.FileRepositoryProperties;
import com.openklaster.filerepository.service.ChartFileRepositoryHandler;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileRepositoryVerticleConfig extends SuperVerticleConfig {
    private JsonObject jsonConfig;

    public FileRepositoryVerticleConfig() {
        JSONParser parser = new JSONParser();
        try {
            InputStream configStream = new ClassPathResource(configPath).getInputStream();
            Object object = parser.parse(new InputStreamReader(configStream));
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
    public ChartFileRepositoryHandler chartFileRepositoryHandler(FileSystem vertxFileSystem) {
        return new ChartFileRepositoryHandler(vertxFileSystem, jsonConfig.getJsonObject(FileRepositoryProperties.CHART_FILE_REPOSITORY)
                .getString(FileRepositoryProperties.ADDRESS));
    }
}
