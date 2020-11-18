package com.openklaster.filerepository;

import com.openklaster.common.SuperVerticleConfig;
import com.openklaster.filerepository.properties.FileRepositoryProperties;
import com.openklaster.filerepository.service.ChartsHandler;
import com.openklaster.filerepository.service.SelectableDates;
import io.vertx.core.file.FileSystem;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
@ComponentScan
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
    public ChartsHandler chartFileRepositoryHandler(FileSystem vertxFileSystem) {
        return new ChartsHandler(vertxFileSystem,
                jsonConfig.getJsonObject(FileRepositoryProperties.CHART_FILE_REPOSITORY).getString(FileRepositoryProperties.ADDRESS),
                jsonConfig.getJsonObject(FileRepositoryProperties.CHART_FILE_REPOSITORY).getString(FileRepositoryProperties.PATH));
    }

    @Lazy
    @Bean
    @Autowired
    public SelectableDates selectableDatesHandler(FileSystem vertxFileSystem) {
        return new SelectableDates(vertxFileSystem,
                jsonConfig.getJsonObject(FileRepositoryProperties.SELECTABLE_DATES).getString(FileRepositoryProperties.ADDRESS),
                jsonConfig.getJsonObject(FileRepositoryProperties.SELECTABLE_DATES).getString(FileRepositoryProperties.PATH));
    }
}
